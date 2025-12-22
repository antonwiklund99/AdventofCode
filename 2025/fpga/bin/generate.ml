open! Core
open! Hardcaml
open! Aoc_2025 

let generate_blinky_rtl () =
  let module C = Circuit.With_interface (Blinky.I) (Blinky.O) in
  let scope = Scope.create ~auto_label_hierarchical_ports:true () in
  let circuit = C.create_exn ~name:"top" (Blinky.hierarchical scope) in
  let rtl_circuits =
    Rtl.create ~database:(Scope.circuit_database scope) Verilog [ circuit ]
  in
  let rtl = Rtl.full_hierarchy rtl_circuits |> Rope.to_string in
  print_endline rtl
;;

let generate_uart_test_rtl () =
  let module C = Circuit.With_interface (Uart_test.I) (Uart_test.O) in
  let scope = Scope.create ~auto_label_hierarchical_ports:true () in
  let circuit = C.create_exn ~name:"top" (Uart_test.hierarchical scope) in
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
      fun () -> generate_blinky_rtl ()]
;;

let uart_test_rtl_command =
  Command.basic
    ~summary:"uart test top"
    [%map_open.Command
      let () = return () in
      fun () -> generate_uart_test_rtl ()]
;;

let () =
  Command_unix.run
    (Command.group ~summary:"" [ "blinky",    blinky_rtl_command
                               ; "uart-test", uart_test_rtl_command ])
;;
