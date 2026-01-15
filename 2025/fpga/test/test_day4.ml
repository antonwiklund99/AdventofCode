open! Core
open! Core_unix
open! Char
open! Re
open! Hardcaml
open! Hardcaml_waveterm
open! Hardcaml_test_harness

module Harness = Cyclesim_harness.Make (Aoc_2025.Solver.I) (Aoc_2025.Solver.O)

let day4_tb file_name (sim : Harness.Sim.t) =
  let inputs = Cyclesim.inputs sim in
  let outputs = Cyclesim.outputs sim in
  let cycle ?n () = Cyclesim.cycle ?n sim in

  let input_data = In_channel.read_lines file_name in
  let eof_value = 0xFF in

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
  let rec send_lines = function
    | [] -> ()
    | [s] -> String.iter ~f:(fun c -> send_byte (int_of_char c)) s
    | s::ss -> String.iter ~f:(fun c -> send_byte (int_of_char c)) (s ^ "\n");
               send_lines ss
  in

  send_lines input_data;
  send_byte eof_value;

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
  let buffer = Buffer.create 256 in
  let rec read_loop seen_newline =
    let c = char_of_int (read_byte ()) in
    Buffer.add_char buffer c;
    if c = '\n' && seen_newline then
      ()
    else
      read_loop (seen_newline || (c = '\n'))
  in
  read_loop false;
  print_string (Buffer.contents buffer);
;;

let waves_config =
  Waves_config.to_directory "/tmp/"
  |> Waves_config.as_wavefile_format ~format:Vcd
;;

let%expect_test "Sample test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day4.hierarchical
    (day4_tb "../../../../../../data/sample4");
  [%expect {|
    00013
    00043
    Saved waves to /tmp/test_day4_ml_Sample_test.vcd
    |}]
;;
let%expect_test "Real input test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day4.hierarchical
    (day4_tb "../../../../../../data/data4");
  [%expect {|
    01437
    08765
    Saved waves to /tmp/test_day4_ml_Real_input_test.vcd
    |}]
;;
