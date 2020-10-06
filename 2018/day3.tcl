#!/usr/bin/tclsh

source "file_access.tcl"
set _what_parts [read_args $argv]
set _claim_list [read_file "data3"]

set _failed_ids [list create]
# part 1
if {[lindex $_what_parts 0] == "true" || [lindex $_what_parts 1]} {
	puts "Running part 1"
	set _count 0
	foreach _line $_claim_list {
		if {[regexp {^\S} $_line]} {
			regexp {^(\S\d*) @ (\d*),(\d*): (\d*)x(\d*)} $_line _match _num _width _height _size_x _size_y
			#puts "$_line---$_count"
			#loops through each placed inch and checks if it has already been used
			for {set y $_height} {$y < [expr $_height+$_size_y]} {incr y} {
				for {set x $_width} {$x < [expr $_width+$_size_x]} {incr x} {
					if {[info exists _grid($x,$y)]} {
						if {$_grid($x,$y) == 1} {
							set _grid($x,$y) x
							set _count [expr $_count + 1]
							lappend $_failed_ids $_num
						}
					} else {
						set _grid($x,$y) 1
					}
				}
			}
		}
	}
	puts "Answer: $_count"
}

# part 2
if {[lindex $_what_parts 1] == "true"} {
	puts "Running part 2"
	foreach _line $_claim_list {
		if {[regexp {^\S} $_line] && [lsearch $_failed_ids $_line] == -1} {
			regexp {^(\S\d*) @ (\d*),(\d*): (\d*)x(\d*)} $_line _match _num _width _height _size_x _size_y
			#puts "Checking $_num for overlap"
			set fail 0
			for {set y $_height} {$y < [expr $_height+$_size_y]} {incr y} {
				for {set x $_width} {$x < [expr $_width+$_size_x]} {incr x} {
					if {[info exists _grid($x,$y)]} {
						if {$_grid($x,$y) > 1} {
							set fail 1
							break
						}
					}
				}
				if {$fail==1} {
					break
				}
			}
			if {$fail==0} {
				puts "No overlap for $_num"
				break
			}
		}
	}
}

