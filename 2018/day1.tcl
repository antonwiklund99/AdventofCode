#!/usr/bin/tclsh

source "file_access.tcl"
set _input [read_file "data1"]
set _what_parts [read_args $argv]

# part 1
if {[lindex $_what_parts 0] == "true"} {
	puts "Running part 1"
	set _freq_diffs [regsub -all {[^\d+-]*} $_input ""]
	puts "Sum of frequencies = [expr $_freq_diffs]"
}

#part 2
if {[lindex $_what_parts 1] == "true"} {
	puts "Running part 2"
	set _freq 0
	set _freq_dict [dict create 0 1]
	while {1} {
		foreach _line [split $_input {}] {
			if {[regexp {\d} $_line]} {
				set _freq [expr $_freq + $_line]
				if {[dict exists $_freq_dict $_freq]} {
					puts "First frequency reached twice = $_freq"
					exit
				} else {
						dict set _freq_dict $_freq 1
				}
			}
		}
	}
}



