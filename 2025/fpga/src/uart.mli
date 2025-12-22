open! Core
open! Hardcaml
open! Signal

module Byte_with_valid : Interface.S with type 'a t = 'a With_valid.t

module Make (Config : sig
    val baud_rate : int
    val clock_freq_hz : int
    val tx_fifo_depth : int
    val rx_fifo_depth : int
    val xoff_threshold : int
  end): sig

  module I : sig
    type 'a t =
      { clock    : 'a
      ; clear    : 'a
      ; uart_rx  : 'a
      ; rx_ready : 'a
      ; tx_data  : 'a Byte_with_valid.t
      }
    [@@deriving hardcaml]
  end

  module O : sig
    type 'a t =
      { uart_tx     : 'a
      ; tx_ready    : 'a
      ; rx_data     : 'a Byte_with_valid.t
      ; rx_overflow : 'a
      }
    [@@deriving hardcaml]
  end

  val create : Scope.t -> Signal.t I.t -> Signal.t O.t
end
