open! Core
open! Hardcaml
open! Comb
open! Signal

module I = struct 
  type 'a t = {
    clock   : 'a;
    btn     : 'a;
    uart_rx : 'a;
  } [@@deriving hardcaml]
end

module O = struct 
  type 'a t = {
    uart_tx : 'a;
    led     : 'a[@bits 2];
  } [@@deriving hardcaml]
end

let clock_freq_hz = 12_000_000
let baud_rate = 115200

module Byte_with_valid = Uart.Byte_with_valid

module Uart = Uart.Make (struct
    let baud_rate = baud_rate
    let clock_freq_hz = clock_freq_hz
    let tx_fifo_depth = 16
    let rx_fifo_depth = 1024
    let xoff_threshold = 512
  end)

let create scope ({clock; btn; uart_rx} : _ I.t) : _ O.t =
  let open Signal in

  let sync_input x = pipeline (Reg_spec.create ~clock ()) ~n:3 x in
  let clear = sync_input btn in
  let uart_rx_sync = sync_input uart_rx in

  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let n = num_bits_to_represent (clock_freq_hz/10) in
  let counter = Signal.reg_fb spec ~enable:Signal.vdd  ~width:n ~f:(mod_counter ~max:(clock_freq_hz/10)) in
  let tick = (counter ==: (zero n)) in

  let rx_ready = wire 1 in
  let tx_data = Byte_with_valid.Of_signal.wires () in 
  let uart_o = 
    Uart.create scope
      { Uart.I.
        clock
      ; clear
      ; uart_rx = uart_rx_sync
      ; rx_ready = rx_ready
      ; tx_data = tx_data
      } in
  rx_ready <-- (uart_o.tx_ready &: tick);
  tx_data.value  <-- (uart_o.rx_data.value);
  tx_data.valid <-- (uart_o.rx_data.valid &: tick);

  { uart_tx=uart_o.uart_tx; led=(clear @: uart_o.rx_overflow)}
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (I) (O) in
  Scoped.hierarchical ~scope ~name:"uart_test" create
;;
