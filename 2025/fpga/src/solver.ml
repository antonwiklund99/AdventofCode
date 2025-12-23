open! Core
open! Hardcaml
open! Signal

(* TODO: EOF *)
let shift_in ~clock ~clear ~n ?(ready = vdd) (signal : _ With_valid.t) =
  let spec = Reg_spec.create ~clock ~clear () in
  let valid_in = ready &: signal.valid in
  let counter =
    reg_fb spec ~width:(num_bits_to_represent n) ~enable:valid_in ~f:(mod_counter ~max:(n - 1))
  in
  let shreg =
    reg_fb
      spec
      ~width:(n * width signal.value)
      ~enable:valid_in
      ~f:(fun x -> drop_bottom ~width:(width signal.value) (signal.value @: x))
  in
  let valid = reg spec (valid_in &: (counter ==:. n - 1)) in
  { With_valid.valid; value = shreg }
;;

module I = struct
  type 'a t =
    { clock            : 'a
    ; clear            : 'a
    ; uart_tx_ready    : 'a
    ; uart_rx_data     : 'a Uart.Byte_with_valid.t
    ; uart_rx_overflow : 'a
    }
  [@@deriving hardcaml]
end

module O = struct
  type 'a t =
    { uart_tx_data  : 'a Uart.Byte_with_valid.t
    ; uart_rx_ready : 'a
    ; leds : 'a[@bits 2]
    }
  [@@deriving hardcaml]
end

module type Solver = sig
  val tx_fifo_depth : int
  val rx_fifo_depth : int
  val xoff_threshold : int
  val hierarchical : Scope.t -> Signal.t I.t -> Signal.t O.t
end
