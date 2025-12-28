open! Core
open! Hardcaml
open! Comb
open! Signal

let clock_freq_hz = Aoc_top.clock_freq_hz
let tx_fifo_depth = 8
let rx_fifo_depth = 8
let xoff_threshold = 7

let create scope ({clock; clear; uart_tx_ready; uart_rx_data; _} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let n = num_bits_to_represent(clock_freq_hz) in
  let counter = Signal.reg_fb spec ~enable:Signal.vdd  ~width:n ~f:(mod_counter ~max:(clock_freq_hz)) in
  let leds = Signal.reg_fb spec ~enable:(counter ==: (zero n)) ~width:2 ~clear_to:(one 2) ~f:(rotr ~by:1) in
  { uart_tx_data=uart_rx_data; uart_rx_ready=uart_tx_ready; leds=leds}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"blinky" create
;;
