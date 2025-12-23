open! Core
open! Hardcaml

val clock_freq_hz : int
val baud_rate     : int

module Make (Solver : Solver.Solver) : sig
  module I : sig
    type 'a t = {
      clock   : 'a;
      btn     : 'a;
      uart_rx : 'a;
    } [@@deriving hardcaml]
  end

  module O : sig
    type 'a t = {
      uart_tx : 'a;
      leds    : 'a;
    } [@@deriving hardcaml]
  end

  val create : Scope.t -> Signal.t I.t -> Signal.t O.t
end
