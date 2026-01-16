open! Core
open! Hardcaml
open! Comb
open! Signal

(* We will always have time to go through memory before next data is ready so no backpressure is needed *)
let tx_fifo_depth = 8
let rx_fifo_depth = 8
let xoff_threshold = 7

let max_nof_intervals = 256 (* max number of intervals allowed *)
let mem_addr_width = Int.ceil_log2 max_nof_intervals 
let mem_data_width = 56*2

module State = struct
  type t =
    | Read_start 
    | Read_end 
    | Find_insert_index
    | Shift_intervals
    | Part2
    | Part1
    | Try_find_interval
    | Send_result
  [@@deriving sexp_of, compare ~localize, enumerate]
end

module Interval = struct
  type 'a t =
    { start : 'a [@bits 56]
    ; end_   : 'a [@bits 56]
    }
  [@@deriving sexp_of, hardcaml]
end

let inside_interval (interval : Signal.t Interval.t) n =
  (interval.start <=: n) &: (interval.end_ >=: n)
;;
let intervals_overlap (a : Signal.t Interval.t) (b : Signal.t Interval.t) = 
  (a.start <=: b.end_) &: (b.start <=: a.end_)
;;
let merge_intervals (a : Signal.t Interval.t) (b : Signal.t Interval.t) : (Signal.t Interval.t) =
  { start = mux2 (a.start <: b.start) a.start b.start
  ; end_ = mux2 (a.end_ >: b.end_) a.end_ b.end_
  }
;;

let create scope ({clock; clear; uart_tx_ready; uart_rx_data; uart_rx_overflow} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let uart_rx_ready = Always.Variable.wire ~default:gnd () in
  let data = Solver.shift_in ~clock ~clear ~ready:(uart_rx_ready.value) ~n:7 uart_rx_data in
  let search_num = Always.Variable.reg spec ~width:56 in
  let sm = Always.State_machine.create (module State) ~enable:vdd spec in
  let res1 = Always.Variable.reg spec ~width:16 in
  let res2 = Always.Variable.reg spec ~width:56 in
  let send = Always.Variable.wire ~default:gnd () in
  let nof_intervals = Always.Variable.reg spec ~width:mem_addr_width in
  let mem_wr_en = Always.Variable.reg spec ~width:1 in
  let mem_wr_addr = Always.Variable.reg spec ~width:mem_addr_width in
  let mem_wr_data = Always.Variable.reg spec ~width:mem_data_width in
  let mem_rd_addr = Always.Variable.reg spec ~width:mem_addr_width in
  let current_data = Always.Variable.reg spec ~width:mem_data_width in

  let%hw mem_rd_data =
    (Ram.create
       ~name:"interval_memory"
       ~collision_mode:Read_before_write
       ~size:(Int.pow 2 mem_addr_width)
       ~write_ports:
         [| { write_clock = clock
            ; write_enable = mem_wr_en.value 
            ; write_address = mem_wr_addr.value
            ; write_data = mem_wr_data.value
            }
         |]
       ~read_ports:
         [| { read_clock = clock
            ; read_enable = vdd
            ; read_address = mem_rd_addr.value
            }
         |]
       ()).(0)
  in

  let current_interval = Interval.Of_signal.unpack current_data.value in
  let read_interval = Interval.Of_signal.unpack mem_rd_data in

  let find_index interval_start logic =
    let open Always in
    proc ([
      mem_rd_addr <-- mem_rd_addr.value +:. 1;
      (* If start of interval is before end of read interval it should either be merged with
         or placed before the current read interval *)
      when_ ((interval_start <=: read_interval.end_) |: (mem_rd_addr.value >: nof_intervals.value)) [
        proc logic;
      ];
    ])
  in

  let read_num_into_current_data logic =
    let open Always in
    proc [
      uart_rx_ready <-- vdd;
      when_ (data.valid) [
        current_data <-- data.value @: current_data.value.:+[56, Some 56];
        proc logic;
      ];
    ]
  in

  Always.(compile [
    mem_wr_en <-- gnd;
    mem_rd_addr <-- (zero mem_addr_width);

    sm.switch [
      Read_start, [
        read_num_into_current_data [
          if_ (all_bits_set data.value) [
            (* End of interval input *)
            mem_rd_addr <-- mem_rd_addr.value +:. 1;
            sm.set_next Part2;
          ] [
            sm.set_next Read_end;
          ];
        ]
      ];

      Read_end, [
        read_num_into_current_data [
          mem_rd_addr <-- mem_rd_addr.value +:. 1;
          sm.set_next Find_insert_index
        ];
      ];

      Find_insert_index, [
        (* Search for the index to insert this interval *)
        find_index current_interval.start [
          mem_wr_addr <-- mem_rd_addr.value -:. 1;
          if_ (mem_rd_addr.value >: nof_intervals.value) [
            (* We are at the end of the memory so just write this interval *)
            nof_intervals <-- nof_intervals.value +:. 1;
            mem_wr_en <-- vdd;
            mem_wr_data <-- current_data.value;
            sm.set_next Read_start;
          ] [
            if_ (intervals_overlap current_interval read_interval) [
              mem_wr_addr <-- mem_rd_addr.value -:. 2;
              current_data <-- Interval.Of_signal.pack (merge_intervals current_interval read_interval);
            ] [
              current_data <-- mem_rd_data;
              mem_wr_en <-- vdd;
              mem_wr_data <-- current_data.value;
            ];
            sm.set_next Shift_intervals;
          ];
        ];
      ];

      Shift_intervals, [
        (* Keep trying to merge intervals, if not possible write the current interval, after that it will continue shifting up the 
           intervals *)
        mem_rd_addr <-- mem_rd_addr.value +:. 1;
        if_ (intervals_overlap current_interval read_interval) [
          current_data <-- Interval.Of_signal.pack (merge_intervals current_interval read_interval);
        ] [
          current_data <-- mem_rd_data;
          mem_wr_en <-- vdd;
          mem_wr_addr <-- mem_wr_addr.value +:. 1;
          mem_wr_data <-- current_data.value;

          when_ (mem_rd_addr.value >: nof_intervals.value) [
            nof_intervals <-- mem_wr_addr.value +:. 2;
            sm.set_next Read_start;
          ];
        ];
      ];

      Part2, [
        res2 <-- res2.value +: (read_interval.end_ -: read_interval.start +:. 1);
        mem_rd_addr <-- mem_rd_addr.value +:. 1;
        when_ (mem_rd_addr.value ==: nof_intervals.value) [
          sm.set_next Part1;
        ];
      ];

      Part1, [
        uart_rx_ready <-- vdd;
        search_num <-- data.value;
        when_ (data.valid) [
          mem_rd_addr <-- mem_rd_addr.value +:. 1;
          if_ (all_bits_set data.value) [
            sm.set_next Send_result;
          ] [
            sm.set_next Try_find_interval
          ]
        ];
      ];

      Try_find_interval, [
        find_index search_num.value [
          when_ (inside_interval read_interval search_num.value) [
            res1 <-- res1.value +:. 1;
          ];
          sm.set_next Part1;
        ]
      ];

      Send_result, [
        send <-- vdd;
      ];
    ]
  ]);

  let uart_tx_data = Solver.shift_out_num_solution ~clock ~clear ~send:send.value ~ready:uart_tx_ready res1.value res2.value in
  { uart_tx_data=uart_tx_data; uart_rx_ready=uart_rx_ready.value; leds=(clear @: uart_rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"day5" create
;;
