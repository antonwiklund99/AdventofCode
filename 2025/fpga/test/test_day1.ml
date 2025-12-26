open! Core
open! Core_unix
open! Char
open! Hardcaml
open! Hardcaml_waveterm
open! Hardcaml_test_harness

let clock_freq_hz = 12_000_000
let baud_rate = 115200
let cycles_per_baud = clock_freq_hz / baud_rate

module Harness = Cyclesim_harness.Make (Aoc_2025.Solver.I) (Aoc_2025.Solver.O)

let day1_tb filename (sim : Harness.Sim.t) =
  let inputs = Cyclesim.inputs sim in
  let outputs = Cyclesim.outputs sim in
  let cycle ?n () = Cyclesim.cycle ?n sim in

  let input_data = In_channel.read_lines filename in
  let eof_value = [0xFF; 0xFF; 0xFF] in

  inputs.uart_tx_ready := Bits.gnd;
  inputs.uart_rx_overflow := Bits.gnd;

  (* Reset the design *)
  inputs.clear := Bits.vdd;
  cycle ~n:4 ();
  inputs.clear := Bits.gnd;
  cycle ~n:2 ();

  let send_byte x =
      inputs.uart_rx_data.value := Bits.of_int_trunc ~width:8 x;
      inputs.uart_rx_data.valid := Bits.vdd;
      cycle ();
  in
  let send_command cmd =
    let dir = cmd.[0] in
    let num_str = String.sub cmd ~pos:1 ~len:(String.length cmd - 1) in
    let num = int_of_string num_str in
    List.iter ~f:send_byte [(int_of_char dir); num land 0xFF; (num lsr 8) land 0xFF]
  in

  List.iter ~f:send_command input_data;
  List.iter ~f:send_byte eof_value;
  inputs.uart_rx_data.value := Bits.zero 8;
  inputs.uart_rx_data.valid := Bits.gnd;

  let read_byte _ =
    inputs.uart_tx_ready := Bits.vdd;
    while (not (Bits.to_bool (!(outputs.uart_tx_data.valid)))) do
      cycle();
    done;
    let x = Bits.to_int_trunc  !(outputs.uart_tx_data.value) in
    cycle();
    inputs.uart_tx_ready := Bits.gnd;
    x;
  in
  let res = Array.of_list (List.map (List.range 0 4) ~f:read_byte) in
  let part1 = res.(0) + (res.(1) lsl 8) in
  let part2 = res.(2) + (res.(3) lsl 8) in
  print_s [%message "Part 1" (part1 : int)];
  print_s [%message "Part 2" (part2 : int)];
  cycle ~n:2 ()
;;

let waves_config =
  Waves_config.to_directory "/tmp/"
  |> Waves_config.as_wavefile_format ~format:Vcd
;;

let%expect_test "Sample test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day1.hierarchical
    (day1_tb "../../../../../../data/sample1");
  [%expect {|
    ("Part 1" (part1 3))
    ("Part 2" (part2 6))
    Saved waves to /tmp/test_day1_ml_Sample_test.vcd
    |}]
;;
let%expect_test "Real input test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day1.hierarchical
    (day1_tb "../../../../../../data/data1");
  [%expect {|
    ("Part 1" (part1 1036))
    ("Part 2" (part2 6228))
    Saved waves to /tmp/test_day1_ml_Real_input_test.vcd
    |}]
;;
