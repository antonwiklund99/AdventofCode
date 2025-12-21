open! Core
open! Hardcaml
open! Signal
open! Comb

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

let create _scope ({clock; btn; uart_rx} : _ I.t) : _ O.t =
  let sync_input x = pipeline (Reg_spec.create ~clock ()) ~n:3 x in
  let clear = sync_input btn in
  let uart_rx_sync = sync_input uart_rx in
  let n = num_bits_to_represent(clock_freq_hz) in
  let spec = Signal.Reg_spec.create ~clock ~clear () in
  let counter = Signal.reg_fb spec ~enable:Signal.vdd  ~width:n ~f:(mod_counter ~max:(clock_freq_hz)) in
  let led = Signal.reg_fb spec ~enable:(counter ==: (zero n))  ~width:2  ~clear_to:(one 2) ~f:(rotr ~by:1) in
  { uart_tx=uart_rx_sync; led=led }
;;

let hierarchical scope =
  let module Scoped = Hierarchy.In_scope (I) (O) in
  Scoped.hierarchical ~scope ~name:"blinky" create
;;
