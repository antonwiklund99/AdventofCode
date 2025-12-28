open! Core
open! Core_unix
open! Char
open! Re
open! Hardcaml
open! Hardcaml_waveterm
open! Hardcaml_test_harness

let clock_freq_hz = 12_000_000
let baud_rate = 115200
let cycles_per_baud = clock_freq_hz / baud_rate

module Harness = Cyclesim_harness.Make (Aoc_2025.Solver.I) (Aoc_2025.Solver.O)

let day2_tb file_name (sim : Harness.Sim.t) =
  let inputs = Cyclesim.inputs sim in
  let outputs = Cyclesim.outputs sim in
  let cycle ?n () = Cyclesim.cycle ?n sim in

  let input_data = In_channel.read_all file_name in
  (* let eof_value = [0xFF; 0xFF; 0xFF] in *)

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
    while (not (Bits.to_bool (!(outputs.uart_rx_ready)))) do
      cycle ();
    done;
    cycle ();
  in
  let send_num n =
    let bytes = List.map (List.range 0 5) ~f:(fun i -> (n lsr (8*i)) land 0xFF) in
    List.iter ~f:send_byte bytes;
  in
  let range_regex = Re.Pcre.regexp "(\\d+)-(\\d+)" in
  Re.all range_regex input_data
  |> List.iter~f:(fun group ->
      send_num (Re.Group.get group 1 |> int_of_string);
      send_num (Re.Group.get group 2 |> int_of_string));

  List.iter ~f:send_byte [0xFF; 0xFF; 0xFF; 0xFF; 0xFF; 0xFF; 0xFF; 0xFF; 0xFF; 0xFF];
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
  let res = Array.of_list (List.map (List.range 0 10) ~f:read_byte) in
  let part1 = res.(0) + (res.(1) lsl 8) + (res.(2) lsl 16) + (res.(3) lsl 24) + (res.(4) lsl 32) in
  let part2 = res.(5) + (res.(6) lsl 8) + (res.(7) lsl 16) + (res.(8) lsl 24) + (res.(9) lsl 32) in
  print_s [%message "Part 1" (part1 : int)];
  print_s [%message "Part 2" (part2 : int)];
  cycle ~n:2 ()
;;

let waves_config =
  Waves_config.to_directory "/tmp/"
  |> Waves_config.as_wavefile_format ~format:Vcd
;;

let%expect_test "Sample test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day2.hierarchical
    (day2_tb "../../../../../../data/sample2");
  [%expect {|
    ("Part 1" (part1 1227775554))
    ("Part 2" (part2 4174379265))
    Saved waves to /tmp/test_day2_ml_Sample_test.vcd
    |}]
;;
let%expect_test "Real input test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day2.hierarchical
    (day2_tb "../../../../../../data/data2");
  [%expect {|
    ("Part 1" (part1 35367539282))
    ("Part 2" (part2 45814076230))
    Saved waves to /tmp/test_day2_ml_Real_input_test.vcd
    |}]
;;
