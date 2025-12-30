open! Core
open! Hardcaml
open! Comb
open! Signal

let tx_fifo_depth = 8
let rx_fifo_depth = 8
let xoff_threshold = 7

module State = struct
  type t =
    | Idle 
    | Read_digits
    | Newline
    | Send_result
  [@@deriving sexp_of, compare ~localize, enumerate]
end

let create scope ({clock; clear; uart_tx_ready; uart_rx_data; uart_rx_overflow} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let sm = Always.State_machine.create (module State) ~enable:vdd spec in
  let remaining = Always.Variable.reg ~enable:vdd spec ~width:8 in
  let num_digits = Always.Variable.reg ~enable:vdd spec ~width:8 in
  let digits_2 = Array.init 2 ~f:(fun _ -> Always.Variable.reg spec ~width:4) in
  let digits_12 = Array.init 12 ~f:(fun _ -> Always.Variable.reg spec ~width:4) in
  let digits_valid = Always.Variable.reg ~enable:vdd spec ~width:1 in
  let send = Always.Variable.reg ~enable:vdd spec ~width:1 in

  (* The algorithm works like this:
     For each new digit check if it is larger than any of the current digits and if inserting
     it would still yield a full solution.
     If so insert it and discard the current digits after where it was inserted *)
  let try_add_digit (digits : Always.Variable.t array) digit =
    let open Always in
    let size = Array.length digits in
    let size_s = Signal.of_int_trunc ~width:8 size in
    let n = Always.Variable.reg spec ~width:8 in
    (* Find the largest digit that can be replaced by the current digit (digits are stored MSB first) *)
    let insert_index = List.init size ~f:(fun i -> (n.value <=:. i) |:
                                                   (((size_s <: remaining.value) |: ((size_s -: remaining.value) <=:. i))
                                                      &: (digits.(i).value <: digit)))
                       |> concat_lsb
                       |> trailing_zeros
    in
    proc (
      List.concat [
        List.init size ~f:(fun i -> when_ (insert_index ==:. i) [ digits.(i) <-- digit; n <--. (i + 1); ]);
        [when_ ~:(sm.is Read_digits) [n <--. 0;]]
      ]
    );
  in

  Always.(compile [
    digits_valid <-- gnd;
    send <-- gnd;

    when_ (uart_rx_data.valid) [
      try_add_digit digits_2 (Bcd.char_2_digit uart_rx_data.value);
      try_add_digit digits_12 (Bcd.char_2_digit uart_rx_data.value);
    ];

    sm.switch [
      Idle, [
        when_ (uart_rx_data.valid) [
          (* First byte is the number of digits per line *)
          num_digits <-- uart_rx_data.value;
          remaining <-- uart_rx_data.value;
          sm.set_next Read_digits; 
        ];
      ];

      Read_digits, [
        when_ (uart_rx_data.valid) [
          remaining <-- remaining.value -:. 1;
          when_ (remaining.value ==:. 1) [
            digits_valid <-- vdd;
            sm.set_next Newline;
          ]
        ];
      ];

      Newline, [
        remaining <-- num_digits.value; (* reset remaining *)
        when_ (uart_rx_data.valid) [
          (* 0xFF indicates end of input *)
          if_ (uart_rx_data.value ==:. 0xFF) [ sm.set_next Send_result; ] [ sm.set_next Read_digits; ];
        ]
      ];

      Send_result, [
        send <-- vdd;
      ];
    ]
  ]);
  
  (* Convert to binary *)
  let bin_2 = Bcd.bcd_2_bin ~clock ~clear ~valid:digits_valid.value (List.rev_map (List.of_array digits_2) ~f:(fun x -> x.value)) in
  let bin_12 = Bcd.bcd_2_bin ~clock ~clear ~valid:digits_valid.value (List.rev_map (List.of_array digits_12) ~f:(fun x -> x.value)) in

  (* Add to result accumulators *)
  let res1 = reg_fb
            spec
            ~width:32
            ~enable:bin_2.valid
            ~f:(fun x -> x +: (uresize ~width:32 bin_2.value)) in
  let res2 = reg_fb
            spec
            ~width:64
            ~enable:bin_12.valid
            ~f:(fun x -> x +: (uresize ~width:64 bin_12.value)) in

  let uart_tx_data = Solver.shift_out ~clock ~clear ~send:send.value ~ready:uart_tx_ready (res2 @: res1) in

  { uart_tx_data=uart_tx_data; uart_rx_ready=vdd; leds=(clear @: uart_rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"day3" create
;;
