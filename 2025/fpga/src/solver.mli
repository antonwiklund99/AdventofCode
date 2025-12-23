open! Core
open! Hardcaml

val shift_in
  :  clock:Signal.t
  -> clear:Signal.t
  -> n:int
  -> ?ready:Signal.t
  -> Signal.t With_valid.t
  -> Signal.t With_valid.t

module I : sig
  type 'a t =
    { clock            : 'a
    ; clear            : 'a
    ; uart_tx_ready    : 'a
    ; uart_rx_data     : 'a Uart.Byte_with_valid.t
    ; uart_rx_overflow : 'a
    }
  [@@deriving hardcaml]
end

module O : sig
  type 'a t =
    { uart_tx_data  : 'a Uart.Byte_with_valid.t
    ; uart_rx_ready : 'a
    ; leds          : 'a
    }
  [@@deriving hardcaml]
end

module type Solver = sig
  val tx_fifo_depth : int
  val rx_fifo_depth : int
  val xoff_threshold : int
  val hierarchical : Scope.t -> Signal.t I.t -> Signal.t O.t
end
