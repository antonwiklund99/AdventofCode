open! Core
open! Core_unix
open! Char
open! Re
open! Hardcaml
open! Hardcaml_waveterm
open! Hardcaml_test_harness

module Harness = Cyclesim_harness.Make (Aoc_2025.Solver.I) (Aoc_2025.Solver.O)

let day5_tb file_name (sim : Harness.Sim.t) =
  let inputs = Cyclesim.inputs sim in
  let outputs = Cyclesim.outputs sim in
  let cycle ?n () = Cyclesim.cycle ?n sim in

  let input_data = In_channel.read_all file_name in
  let eof_value = List.init 7 ~f:(fun _ -> 0xFF) in

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
    let timeout_cnt = ref 0 in
    while (not (Bits.to_bool (!(outputs.uart_rx_ready)))) do
      if Int.(!timeout_cnt > 256) then
        failwith "timeout send byte";
      cycle ();
      incr timeout_cnt;
    done;
    cycle ();
  in
  let send_num n =
    let bytes = List.map (List.range 0 7) ~f:(fun i -> (n lsr (8*i)) land 0xFF) in
    List.iter ~f:send_byte bytes;
  in
  let range_regex = Re.Pcre.regexp "(\\d+)-(\\d+)" in
  Re.all range_regex input_data
  |> List.iter~f:(fun group ->
      send_num (Re.Group.get group 1 |> int_of_string);
      send_num (Re.Group.get group 2 |> int_of_string));
  List.iter ~f:send_byte eof_value;
  let num_regex = Re.Pcre.regexp ~flags:[`MULTILINE]  "^(\\d+)$" in
  Re.all num_regex input_data
  |> List.iter ~f:(fun group -> send_num (Re.Group.get group 1 |> int_of_string));
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

  cycle ~n:2 ()
;;

let waves_config =
  Waves_config.to_directory "/tmp/"
  |> Waves_config.as_wavefile_format ~format:Vcd
;;

let%expect_test "Sample test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day5.hierarchical
    (day5_tb "../../../../../../data/sample5");
  [%expect {|
    00004
    00000000000000104
    Saved waves to /tmp/test_day5_ml_Sample_test.vcd
    |}]
;;
let%expect_test "Real input test" =
  Harness.run_advanced ~waves_config ~trace:`Everything ~create:Aoc_2025.Day5.hierarchical
    (day5_tb "../../../../../../data/data5");
  [%expect {|
    00896
    00346240317247002
    Saved waves to /tmp/test_day5_ml_Real_input_test.vcd
    |}]
;;
