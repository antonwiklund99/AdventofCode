open! Core
open! Hardcaml
open! Comb
open! Signal

let clock_freq_hz = 12_000_000
let baud_rate = 115200

module Make (Solver : Solver.Solver) = struct

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
      leds    : 'a[@bits 2];
    } [@@deriving hardcaml]
  end

  module Byte_with_valid = Uart.Byte_with_valid

  module Uart = Uart.Make (struct
      let baud_rate = baud_rate
      let clock_freq_hz = clock_freq_hz
      let tx_fifo_depth = Solver.tx_fifo_depth
      let rx_fifo_depth = Solver.rx_fifo_depth
      let xoff_threshold = Solver.xoff_threshold
    end)

  let create scope ({clock; btn; uart_rx} : _ I.t) : _ O.t =
    let open Signal in

    let spec = Reg_spec.create ~clock () in
    let sync_input x = pipeline spec ~n:3 x in
    let clear = sync_input btn in
    let uart_rx_sync = sync_input uart_rx in
    let initialized = Signal.reg_fb spec ~enable:vdd ~width:1 ~f:(fun x -> x |: clear) in

    let uart_rx_ready = wire 1 in
    let uart_tx_data = Byte_with_valid.Of_signal.wires () in 
    let%tydi { uart_tx; tx_ready=uart_tx_ready; rx_data=uart_rx_data; rx_overflow=uart_rx_overflow } = 
      Uart.create scope
        { Uart.I.
          clock
        ; clear
        ; uart_rx = uart_rx_sync
        ; rx_ready = uart_rx_ready
        ; tx_data = uart_tx_data
        } in

    let solver_o =
      Solver.hierarchical scope
        { clock
        ; clear
        ; uart_tx_ready
        ; uart_rx_data
        ; uart_rx_overflow
        } in
    uart_rx_ready <-- solver_o.uart_rx_ready;
    Byte_with_valid.Of_signal.assign uart_tx_data solver_o.uart_tx_data;

    {uart_tx=(uart_tx |: ~:(initialized)); leds=solver_o.leds}
  ;;
end
