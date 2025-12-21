open! Core
open! Hardcaml

val clock_freq_hz : int

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
    led     : 'a;
  } [@@deriving hardcaml]
end

val hierarchical : Scope.t -> Signal.t I.t -> Signal.t O.t
