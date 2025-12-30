open! Core
open! Hardcaml
open! Comb
open! Signal

let tx_fifo_depth = 16
let rx_fifo_depth = 128
let xoff_threshold = 100

module State = struct
  type t =
    | Idle 
    | Generate 
  [@@deriving sexp_of, compare ~localize, enumerate]
end

let create scope ({clock; clear; uart_tx_ready; uart_rx_data; uart_rx_overflow} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let uart_rx_ready = Always.Variable.wire ~default:gnd () in
  let data = Solver.shift_in ~clock ~clear ~ready:(uart_rx_ready.value) ~n:10 uart_rx_data in
  let eof = data.valid &: (all_bits_set data.value) in
  let eof_sticky = reg_fb spec ~width:1 ~enable:data.valid ~f:(fun x -> (x |: eof)) in

  (* Generate numbers *)
  let current = Always.Variable.reg ~enable:vdd spec ~width:36 in
  let current_valid = Always.Variable.wire ~default:gnd () in
  let last = Always.Variable.reg ~enable:vdd spec ~width:36 in
  let sm = Always.State_machine.create (module State) ~enable:vdd spec in
  Always.(compile [
    sm.switch [
      Idle, [
        uart_rx_ready <-- vdd;
        when_ (data.valid &: ~:(eof |: eof_sticky)) [
          sm.set_next Generate; 
          current <-- data.value.:[35,0];
          last    <-- data.value.:[75,40];
        ];
      ];

      Generate, [
        current_valid <-- vdd;
        current <-- current.value +:. 1;
        when_ (last.value ==: current.value) [
          sm.set_next Idle
        ];
      ];
    ]
  ]);

  (* Convert to BCD *)
  let bcd = Bcd.bin_2_bcd ~clock ~clear {With_valid.valid=current_valid.value; value=current.value} in
  let max_digits = (width bcd.value) / 4 in
  let bcd_bin = reg spec ~enable:vdd current.value in

  (* Find repeating patterns *)
  let rec patterns n x =
    if n <= 1 then
      [(0,0)]
    else if x > n/2 then
      []
    else if (n % x) = 0 then
      (x, n/x - 1)::(patterns n (x + 1))
    else
      patterns n (x + 1);
  in
  let repeat_pattern n =
    let rec repeat_pattern_rec p first prev_match =
      match (p) with
      | [] -> []
      | h::t ->
          let m = (prev_match &: (first ==: (uresize ~width:(4*n) h))) in
          m::(repeat_pattern_rec t first m);
      in
    if n = 0 then
      Array.of_list [gnd]
    else
      let parts = (split_lsb ~exact:false ~part_width:(4*n) bcd.value) in
      let first = List.hd parts in
      match (first) with
        | Some (first) -> Array.of_list (repeat_pattern_rec parts first vdd)
        | None -> failwith "empty list";
  in
  (* Create table for each possible number of digits of possible repeating patterns (pattern length, repeat times - 1) *)
  let pattern_table = Array.of_list (List.map (List.range 0 (max_digits+1)) ~f:(fun n -> patterns n 1)) in

  (* Create a matrix of calculations for repeating patterns, index into it is [pattern length][repeat times - 1] *)
  let repeat_match_matrix = Array.of_list (List.map (List.range 0 (max_digits / 2 + 1)) ~f:repeat_pattern) in

  (* Create list of signals for each number of digits that calculates if any pattern for that number of digits is found *)
  let match_any_lst = List.map (List.range 1 (max_digits+1))
                            ~f:(fun i -> List.map (pattern_table.(i)) ~f:(fun (x,y) -> repeat_match_matrix.(x).(y))
                                         |> reduce ~f:(|:)) in

  (* Create list of signals for each number of digits that calculates if it can be split in two and repeats *)
  let match_split_lst = List.map (List.range 1 (max_digits+1))
                            ~f:(fun i -> if (i % 2) = 0 then repeat_match_matrix.(i/2).(1) else gnd) in

  let match_any = reg spec ~enable:vdd (mux (decr bcd.ndigits) (match_any_lst @ [gnd])) in
  let match_split = reg spec ~enable:vdd (mux (decr bcd.ndigits) (match_split_lst @ [gnd])) in
  let match_valid = reg spec ~enable:vdd bcd.valid in
  let match_id = reg spec ~enable:vdd bcd_bin in

  (* Add to result accumulators *)
  let res1 = reg_fb
            spec
            ~width:40
            ~enable:(match_split &: match_valid)
            ~f:(fun x -> x +: (uresize ~width:40 match_id)) in
  let res2 = reg_fb
            spec
            ~width:40
            ~enable:(match_any &: match_valid)
            ~f:(fun x -> x +: (uresize ~width:40 match_id)) in

  let uart_tx_data = Solver.shift_out ~clock ~clear ~send:eof_sticky ~ready:uart_tx_ready (res2 @: res1) in

  { uart_tx_data=uart_tx_data; uart_rx_ready=uart_rx_ready.value; leds=(clear @: uart_rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"day2" create
;;
