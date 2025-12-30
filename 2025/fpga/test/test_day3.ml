open! Core
open! Core_unix
open! Char
open! Re
open! Hardcaml
open! Hardcaml_waveterm
open! Hardcaml_test_harness

module Harness = Cyclesim_harness.Make (Aoc_2025.Solver.I) (Aoc_2025.Solver.O)

let day3_tb file_name (sim : Harness.Sim.t) =
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
  let head = function
    | [] -> failwith "empty list"
    | h::_ -> h
  in

  send_byte (String.length (head input_data));
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
  let res = Array.of_list (List.map (List.range 0 12) ~f:read_byte) in
  let part1 = res.(0) + (res.(1) lsl 8) + (res.(2) lsl 16) + (res.(3) lsl 24) in
  let part2 = res.(4) + (res.(5) lsl 8) + (res.(6) lsl 16) + (res.(7) lsl 24) +
              (res.(8) lsl 32) + (res.(9) lsl 40) + (res.(10) lsl 48) + (res.(11) lsl 56) in
  print_s [%message "Part 1" (part1 : int)];
  print_s [%message "Part 2" (part2 : int)];
  cycle ~n:2 ()
;;

let waves_config =
  Waves_config.to_directory "/tmp/"
  |> Waves_config.as_wavefile_format ~format:Vcd
;;

let%expect_test "Sample test" =
  Harness.run_advanced ~create:Aoc_2025.Day3.hierarchical
    (day3_tb "../../../../../../data/sample3");
  [%expect {|
    ("Part 1" (part1 357))
    ("Part 2" (part2 3121910778619))
    |}]
;;
let%expect_test "Real input test" =
  Harness.run_advanced ~create:Aoc_2025.Day3.hierarchical
    (day3_tb "../../../../../../data/data3");
  [%expect {|
    ("Part 1" (part1 17179))
    ("Part 2" (part2 170025781683941))
    |}]
;;
