open! Core
open! Hardcaml
open! Comb
open! Signal

let tx_fifo_depth = 8
let rx_fifo_depth = 8
let xoff_threshold = 7

let rec div_pos_rec ~a ~b ~m ~max =
  if b*m > max then
    a, zero (width a)
  else 
    let a,d = div_pos_rec ~a ~b ~m:(m lsl 1) ~max in
    let larger = a >=:. b*m in
    ( mux2 larger (a -:. b*m) a, mux2 larger (d +:. m) d )

let div_pos ~a ~b ~max = div_pos_rec ~a ~b ~m:1 ~max

let rec div_neg_rec ~a ~b ~m ~min =
  if -b*m < min then
    a, zero (width a)
  else 
    let a, d = div_neg_rec ~a ~b ~m:(m lsl 1) ~min in
    let smaller = a <=+. -b*m in
    ( mux2 smaller (a +:. b*m) a, mux2 smaller (d +:. m) d )

let div_neg ~a ~b ~min = 
  let m,d = div_neg_rec ~a ~b ~m:1 ~min in
  let negative = (m <+. 0) in
  ( mux2 negative (m +:. b) m, mux2 (m <=+. 0) (d +:. 1) d )

let create scope ({clock; clear; uart_tx_ready; uart_rx_data; uart_rx_overflow} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let data = Solver.shift_in ~clock ~clear ~n:3 uart_rx_data in
  let eof = data.valid &: (data.value ==:. 0xffffff) in
  let eof_sticky = reg_fb spec ~width:1 ~enable:data.valid ~f:(fun x -> (x |: eof)) in
  let data_valid = data.valid &: ~:(eof |: eof_sticky) in

  let left = (data.value.:[7,0])  ==:. (int_of_char 'L') in
  let v = (data.value.:[18,8]) in
  let n = Always.Variable.reg ~clear_to:(of_int_trunc ~width:7 50) ~enable:data_valid spec ~width:7 in
  let (add_m, add_d) = div_pos ~a:((uresize ~width:11 n.value) +: v) ~b:100 ~max:1099 in
  let (sub_m, sub_d_1) = div_neg ~a:((uresize ~width:11 n.value) -: v) ~b:100 ~min:(-999) in
  let sub_d = mux2 (no_bits_set n.value) (decr sub_d_1) (sub_d_1) in
  Always.(compile [
    if_ left [n <-- sel_bottom ~width:7 sub_m] [n <-- sel_bottom ~width:7 add_m]
  ]);
  let res1 = reg_fb
            spec
            ~width:16
            ~enable:(data_valid &: (no_bits_set (mux2 left sub_m add_m)))
            ~f:incr in
  let res2 = reg_fb
            spec
            ~width:16
            ~enable:data_valid
            ~f:(fun x -> x +: (uresize ~width:16 (mux2 left sub_d add_d))) in

  let uart_tx_data = Solver.shift_out ~clock ~clear ~send:eof_sticky ~ready:uart_tx_ready (res2 @: res1) in

  { uart_tx_data=uart_tx_data; uart_rx_ready=vdd; leds=(clear @: uart_rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"day1" create
;;
