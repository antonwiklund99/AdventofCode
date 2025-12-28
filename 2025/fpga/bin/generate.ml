open! Core
open! Hardcaml
open! Aoc_2025 

let generate_top_rtl (module Solver : Solver.Solver) =
  let module Aoc_top = Aoc_top.Make(Solver) in
  let module C = Circuit.With_interface (Aoc_top.I) (Aoc_top.O) in
  let scope = Scope.create ~auto_label_hierarchical_ports:true () in
  let circuit = C.create_exn ~name:"top" (Aoc_top.create scope) in
  let rtl_circuits =
    Rtl.create ~database:(Scope.circuit_database scope) Verilog [ circuit ]
  in
  let rtl = Rtl.full_hierarchy rtl_circuits |> Rope.to_string in
  print_endline rtl
;;

let blinky_rtl_command =
  Command.basic
    ~summary:"blinky top"
    [%map_open.Command
      let () = return () in
      fun () -> generate_top_rtl (module Aoc_2025.Blinky)]
;;

let uart_test_rtl_command =
  Command.basic
    ~summary:"uart test top"
    [%map_open.Command
      let () = return () in
      fun () -> generate_top_rtl (module Aoc_2025.Uart_test)]
;;

let day1_rtl_command =
  Command.basic
    ~summary:"day 1 top"
    [%map_open.Command
      let () = return () in
      fun () -> generate_top_rtl (module Aoc_2025.Day1)]
;;

let day2_rtl_command =
  Command.basic
    ~summary:"day 2 top"
    [%map_open.Command
      let () = return () in
      fun () -> generate_top_rtl (module Aoc_2025.Day2)]
;;

let () =
  Command_unix.run
    (Command.group ~summary:"" [ "blinky",    blinky_rtl_command
                               ; "uart-test", uart_test_rtl_command
                               ; "day1",      day1_rtl_command
                               ; "day2",      day2_rtl_command ])
;;
