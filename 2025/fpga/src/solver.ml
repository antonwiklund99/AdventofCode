open! Core
open! Hardcaml
open! Signal

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

let shift_out ~clock ~clear ~send ~ready bytes =
  let spec = Reg_spec.create ~clock ~clear () in
  let n = (width bytes) / 8 in
  let send_d = reg spec send in
  let sending = Always.Variable.reg ~enable:vdd spec ~width:1 in
  let data = Always.Variable.reg ~enable:vdd spec ~width:(width bytes) in
  let counter = Always.Variable.reg ~enable:vdd spec ~width:(num_bits_to_represent n) in
  Always.(compile [
    if_ (~:(sending.value)) [
      data <-- bytes;
      when_ (~:(send_d) &: send) [
        sending <-- vdd;
      ]
    ] [
      when_ (ready) [
        data <-- (zero 8) @: (drop_bottom ~width:8 data.value);
        counter <-- mod_counter ~max:(n-1) (counter.value);
        when_ (counter.value ==:. n - 1) [
          sending <-- gnd;
        ]
      ]
    ];
  ]);
  { With_valid.valid=sending.value; value=(sel_bottom ~width:8 data.value) }
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
