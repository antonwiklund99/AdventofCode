open! Core
open! Hardcaml
open! Comb
open! Signal

let clock_freq_hz = Aoc_top.clock_freq_hz
let tx_fifo_depth = 16
let rx_fifo_depth = 256
let xoff_threshold = 128

let create scope ({clock; clear; uart_tx_ready; uart_rx_data; uart_rx_overflow} : _ Solver.I.t) : _ Solver.O.t =
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let n = num_bits_to_represent (clock_freq_hz/10) in
  let counter = Signal.reg_fb spec ~enable:Signal.vdd  ~width:n ~f:(mod_counter ~max:(clock_freq_hz/10)) in
  let tick = (counter ==: (zero n)) in
  let uart_tx_valid = uart_rx_data.valid &: tick in
  let uart_rx_ready = uart_tx_ready &: tick in
  { uart_tx_data={valid=uart_tx_valid; value=uart_rx_data.value}; uart_rx_ready=uart_rx_ready; leds=(clear @: uart_rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (Solver.I) (Solver.O) in
  Scoped.hierarchical ~scope ~name:"uart_test" create
;;
