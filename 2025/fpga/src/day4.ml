open! Core
open! Hardcaml
open! Comb
open! Signal

let tx_fifo_depth = 8
let rx_fifo_depth = 8
let xoff_threshold = 7

let line_length = 136 (* works for smaller lengths too *)
let fifo_depth = line_length + 2

module State = struct
  type t =
    | Read_matrix 
    | Part1
    | Part2
    | Send_result
  [@@deriving sexp_of, compare ~localize, enumerate]
end

let clear_nth_bit ~n x =
  let w = width x in
  let mask = ~:(log_shift (of_int_trunc ~width:w 1) ~by:n ~f:sll) in
  x &: mask
;;


let create scope ({clock; clear; uart_tx_ready; uart_rx_data; uart_rx_overflow} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let sm = Always.State_machine.create (module State) ~enable:vdd spec in
  let m = Always.Variable.reg spec ~width:8 in
  let n = Always.Variable.reg spec ~width:8 in
  let res1 = Always.Variable.reg spec ~width:16 in
  let res2 = Always.Variable.reg spec ~width:16 in
  let p2_done = Always.Variable.reg spec ~width:1 in
  let last = Always.Variable.wire ~default:gnd () in
  let send = Always.Variable.wire ~default:gnd () in
  let prev_line = Always.Variable.reg spec ~width:line_length in
  let current_line = Always.Variable.reg spec ~width:line_length in
  let current_col = Always.Variable.reg spec ~width:8 in
  let current_row = Always.Variable.reg spec ~width:8 in
  let fifo_wr = Always.Variable.wire ~default:gnd () in
  let fifo_rd = Always.Variable.wire ~default:gnd () in

  let%tydi { q = next_line; _ } =
    Fifo.create
      ~showahead:true
      ~scope:(Scope.sub_scope scope "line_fifo")
      ~capacity:fifo_depth
      ~clock
      ~clear
      ~wr:fifo_wr.value
      ~d:prev_line.value
      ~rd:fifo_rd.value
      ()
  in

  let get_bit i x = mux i (split_lsb ~part_width:1 x) in
  let left line = mux2 (current_col.value ==:. 0) gnd (get_bit (current_col.value -:. 1) line) in
  let center line = get_bit current_col.value line in
  let right line = mux2 (current_col.value ==: (n.value -:. 1)) gnd (get_bit (current_col.value +:. 1) line) in

  let down = mux2 (current_row.value ==:. 0) (zero 3) ((left prev_line.value) @: (center prev_line.value) @: (right prev_line.value)) in
  let middle = (left current_line.value) @: (right current_line.value) in
  let up = mux2 (current_row.value ==: (m.value -:. 1)) (zero 3) ((left next_line) @: (center next_line) @: (right next_line)) in
  let adjacent = down @: middle @: up in
  let removeable = (get_bit current_col.value current_line.value) &: ((popcount adjacent) <:. 4) in

  (* Keep a window of 3 lines to check, shift lines in and out of FIFO *)
  let move_to_next () =
    let open Always in
    proc ([
      current_col <-- current_col.value +:. 1;
      when_ (current_col.value ==: (n.value -:. 1)) [
        fifo_rd <-- vdd;
        fifo_wr <-- vdd;
        current_col <-- (zero 8);
        current_row <-- current_row.value +:. 1;
        prev_line <-- current_line.value;
        current_line <-- next_line;
        when_ (current_row.value ==: (m.value -:. 1)) [
          last <-- vdd;
          current_row <-- (zero 8);
        ];
      ];
    ])
  in

  Always.(compile [
    sm.switch [
      Read_matrix, [
        when_ (uart_rx_data.valid) [
          when_ (uart_rx_data.value ==: of_char '@') [ 
            prev_line <-- (lsbs prev_line.value) @: vdd;
            n <-- n.value +:. 1;
          ];
          when_ (uart_rx_data.value ==: of_char '.') [ 
            prev_line <-- (lsbs prev_line.value) @: gnd;
            n <-- n.value +:. 1;
          ];
          when_ (uart_rx_data.value ==: of_char '\n') [
            (* End of line *)
            fifo_wr <-- vdd;
            prev_line <-- (zero line_length);
            n <-- (zero 8);
            m <-- m.value +:. 1;
          ];
          when_ (uart_rx_data.value ==: of_string "8'hff") [
            (* End of input *)
            fifo_wr <-- vdd;
            fifo_rd <-- vdd;
            prev_line <-- (zero line_length);
            current_line <-- next_line;
            m <-- m.value +:. 1;
            sm.set_next Part1; 
          ];
        ];
      ];

      Part1, [
        when_ removeable [
          res1 <-- res1.value +:. 1;
        ];
        move_to_next ();
        when_ (current_row.value ==:. 0) [
          fifo_wr <-- gnd; (* first time here prev_line is not valid, so it should not be written *)
        ]; 
        when_ (last.value) [
          sm.set_next Part2;
        ];
      ];

      Part2, [
        move_to_next ();
        when_ removeable [
          res2 <-- res2.value +:. 1;
          p2_done <-- gnd;
          if_ fifo_rd.value [
            prev_line <-- clear_nth_bit ~n:(current_col.value) current_line.value;
          ][
            current_line <-- clear_nth_bit ~n:(current_col.value) current_line.value;
          ]
        ];
        when_ (last.value) [
          p2_done <-- vdd;
          (* iterate through FIFO until we have not removed any rolls for a full cycle *)
          when_ (p2_done.value &: ~:(removeable)) [
            sm.set_next Send_result
          ];
        ];
      ];

      Send_result, [
        send <-- vdd;
      ];
    ]
  ]);

  let uart_tx_data = Solver.shift_out_num_solution ~clock ~clear ~send:send.value ~ready:uart_tx_ready res1.value res2.value in
  { uart_tx_data=uart_tx_data; uart_rx_ready=vdd; leds=(clear @: uart_rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"day4" create
;;
