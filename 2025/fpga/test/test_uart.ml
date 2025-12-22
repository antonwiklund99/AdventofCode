open! Core
open! Hardcaml
open! Hardcaml_waveterm
open! Hardcaml_test_harness

let clock_freq_hz = 12_000_000
let baud_rate = 115200
let cycles_per_baud = clock_freq_hz / baud_rate

module Uart = Aoc_2025.Uart.Make (struct
    let baud_rate = baud_rate
    let clock_freq_hz = clock_freq_hz
    let tx_fifo_depth = 16
    let rx_fifo_depth = 256
    let xoff_threshold = 128
  end)
module Harness = Cyclesim_harness.Make (Uart.I) (Uart.O)

let tx_tb (sim : Harness.Sim.t) =
  let inputs = Cyclesim.inputs sim in
  let outputs = Cyclesim.outputs sim in

  inputs.uart_rx := Bits.vdd;

  let cycle ?n () = Cyclesim.cycle ?n sim in
  let drive_tx n =
    cycle ~n:cycles_per_baud (); (* wait some time so block will start sending immediately *)

    (* Send byte *)
    inputs.tx_data.value := Bits.of_int_trunc ~width:8 n;
    inputs.tx_data.valid := Bits.vdd;
    cycle ();
    inputs.tx_data.value := Bits.of_int_trunc ~width:8 0;
    inputs.tx_data.valid := Bits.gnd;

    (* Read UART *)
    cycle ~n:(cycles_per_baud/2) ();
    let start_bit = Bits.to_int_trunc !(outputs.uart_tx) in
    if start_bit <> 0 then
      print_s [%message "ERROR: start_bit not low"];

    let x = ref 0 in
    for bit = 0 to 7 do
      cycle ~n:cycles_per_baud ();
      x := !x lor (Bits.to_int_trunc !(outputs.uart_tx) lsl bit);
    done;

    cycle ~n:(cycles_per_baud) ();
    let stop_bit = Bits.to_int_trunc !(outputs.uart_tx) in
    if stop_bit <> 1 then
      print_s [%message "ERROR: stop_bit not high"];

    x;
  in

  (* Reset the design *)
  inputs.clear := Bits.vdd;
  cycle ~n:4 ();
  inputs.clear := Bits.gnd;
  cycle ~n:2 ();

  (* Input some data *)
  let results = List.map (List.range 0 256) ~f:(fun x -> 
    let result = drive_tx x in
    (x, !result)
  ) in
  let mismatches = List.filter results ~f:(fun (sent, received) -> sent <> received) in
  if List.is_empty mismatches then
    print_s [%message "All tests passed" (List.length results : int)]
  else
    print_s [%message "Mismatches found" (mismatches : (int * int) list)];

  cycle ~n:2 ()
;;

let rx_tb (sim : Harness.Sim.t) =
  let inputs = Cyclesim.inputs sim in
  let outputs = Cyclesim.outputs sim in

  let cycle ?n () = Cyclesim.cycle ?n sim in
  let drive_rx n =
    inputs.rx_ready := Bits.gnd;

    (* Start bit *)
    inputs.uart_rx := Bits.gnd;

    (* Data *)
    for bit = 0 to 7 do
      cycle ~n:cycles_per_baud ();
      inputs.uart_rx := (Bits.of_int_trunc ~width:1 (n lsr bit));
    done;

    (* Stop bit *)
    cycle ~n:(cycles_per_baud) ();
    inputs.uart_rx := Bits.vdd;
  
    if not (Bits.to_bool !(outputs.rx_data.valid)) then
      print_s [%message "ERROR: rx_data.valid not high after sent byte"];
    let rx_byte = Bits.to_int_trunc !(outputs.rx_data.value) in
    inputs.rx_ready := Bits.vdd;
    cycle ();
    inputs.rx_ready := Bits.gnd;

    cycle ~n:(cycles_per_baud) ();

    rx_byte;
  in

  (* Reset the design *)
  inputs.clear := Bits.vdd;
  cycle ~n:4 ();
  inputs.clear := Bits.gnd;
  cycle ~n:2 ();

  (* Input some data *)
  let results = List.map (List.range 0 256) ~f:(fun x -> 
    let result = drive_rx x in
    (x, result)
  ) in
  let mismatches = List.filter results ~f:(fun (sent, received) -> sent <> received) in
  if List.is_empty mismatches then
    print_s [%message "All tests passed" (List.length results : int)]
  else
    print_s [%message "Mismatches found" (mismatches : (int * int) list)];

  cycle ~n:2 ()
;;

let waves_config =
  Waves_config.to_directory "/tmp/"
  |> Waves_config.as_wavefile_format ~format:Vcd
;;

let%expect_test "TX test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Uart.create tx_tb;
  [%expect {|
    ("All tests passed" ("List.length results" 256))
    Saved waves to /tmp/test_uart_ml_TX_test.vcd
    |}]
;;

let%expect_test "RX test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Uart.create rx_tb;
  [%expect {|
    ("All tests passed" ("List.length results" 256))
    Saved waves to /tmp/test_uart_ml_RX_test.vcd
    |}]
;;

