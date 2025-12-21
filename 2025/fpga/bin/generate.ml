open! Core
open! Hardcaml
open! Aoc_2025 

let generate_top_rtl () =
  let module C = Circuit.With_interface (Blinky.I) (Blinky.O) in
  let scope = Scope.create ~auto_label_hierarchical_ports:true () in
  let circuit = C.create_exn ~name:"top" (Blinky.hierarchical scope) in
  let rtl_circuits =
    Rtl.create ~database:(Scope.circuit_database scope) Verilog [ circuit ]
  in
  let rtl = Rtl.full_hierarchy rtl_circuits |> Rope.to_string in
  print_endline rtl
;;

let top_rtl_command =
  Command.basic
    ~summary:""
    [%map_open.Command
      let () = return () in
      fun () -> generate_top_rtl ()]
;;

let () =
  Command_unix.run
    (Command.group ~summary:"" [ "blinky", top_rtl_command ])
;;
