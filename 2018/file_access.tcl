proc read_file {_input_file_path} {

    set _input [open $_input_file_path r]
    set _data  [read $_input]

    close $_input
    return [split $_data "\n"]
}

proc read_args {_argv} {
    set part1 "false"
    set part2 "false"
    foreach arg $_argv {
        if {$arg == "part1"} {
            set part1 "true"
        } elseif {$arg == "part2"} {
            set part2 "true"
        }
    }
    if {$part1 == "false" && $part2 == "false"} {
        puts "No part specified, running both part 1 and 2"
        set part1 "true"
        set part2 "true"
    }
    return [list $part1 $part2]
}
