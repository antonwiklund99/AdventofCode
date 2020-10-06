#!/usr/bin/tclsh

proc _sort {a b} {
	regexp {^\[1518-(\d*)-(\d*) (\d*):(\d*)} $a _match_a _month_a _day_a _hour_a _minute_a
	regexp {^\[1518-(\d*)-(\d*) (\d*):(\d*)} $b _match_b _month_b _day_b _hour_b _minute_b
	if {$_month_a > $_month_b} {
		return 1
	} elseif {$_month_b > $_month_a} {
		return -1
	}
	if {$_day_a > $_day_b} {
		return 1
	} elseif {$_day_b > $_day_a} {
		return -1
	}
	if {$_hour_a > $_hour_b} {
		return 1
	} elseif {$_hour_b > $_hour_a} {
		return -1
	}
	if {$_minute_a > $_minute_b} {
		return 1
	} elseif {$_minute_b > $_minute_a} {
		return -1
	}
}

source "file_access.tcl"
set _what_parts [read_args $argv]
set _input [lsearch -all -inline -not -exact [read_file "data4"] {}]
set _sorted_list [lsort -command _sort $_input]

set _highest_guard [list 0 0]
set _highest_minute [list 0 0]
set _guard_dict [dict create]
set _minute_dict [dict create]

# part 1
if {[lindex $_what_parts 0] == "true"} {
	puts "Running part 1"
	foreach _line $_sorted_list {
		if {[regexp {Guard \S(\d*) begins shift} $_line _match _guard]} {
			set _current_guard $_guard
			#puts "GUARD ID-$_current_guard"
		} elseif {[regexp {(\d*):(\d*)\] falls} $_line _match _sleep_start_hour _sleep_start_minute]} {
			if {[regexp {(\d*):0(\d)\] falls} $_line _match _sleep_start_hour _sleep_start_minute]} {
				#puts "zero in minute"
			}
			#puts "Start-HOUR:0 MINUTE:$_sleep_start_minute"
		} elseif {[regexp {(\d*):(\d*)\] wakes} $_line _match _sleep_end_hour _sleep_end_minute]} {
			if {[regexp {(\d*):0(\d)\] wakes} $_line _match _sleep_end_hour _sleep_end_minute]} {
				#puts "zero in minute"
			}
			set _sleep_time [expr $_sleep_end_minute - $_sleep_start_minute]

			if {[dict exists $_guard_dict $_current_guard]} {
				dict set _guard_dict $_current_guard [expr [dict get $_guard_dict $_current_guard] + $_sleep_time]
			} else {
				dict set _guard_dict $_current_guard $_sleep_time
			}

			for {set _current_minute $_sleep_start_minute} {$_current_minute < $_sleep_end_minute} {incr _current_minute} {
				if {[dict exists $_minute_dict $_current_guard $_current_minute]} {
					dict set _minute_dict $_current_guard $_current_minute [expr [dict get $_minute_dict $_current_guard $_current_minute] + 1]
					#puts "minute: $_current_minute count: [dict get $_minute_dict $_current_guard $_current_minute]"
				} else {
					dict set _minute_dict $_current_guard $_current_minute 1
					#puts "minute: $_current_minute count: [dict get $_minute_dict $_current_guard $_current_minute]"
				}
			}

			#puts "Stops-HOUR:0 MINUTE:$_sleep_end_minute Time: $_sleep_time minutes"
			if {[dict get $_guard_dict $_guard] > [lindex $_highest_guard 0]} {
				set _highest_guard [list [dict get $_guard_dict $_guard] $_guard]
			}
		}
	}
	foreach _minute [dict get $_minute_dict [lindex $_highest_guard 1]] {
		if {[dict get $_minute_dict [lindex $_highest_guard 1] $_minute] > [lindex $_highest_minute 0]} {
			set _highest_minute [list [dict get $_minute_dict [lindex $_highest_guard 1] $_minute] $_minute]
		}
	}
	puts "[lindex $_highest_guard 1] * [lindex $_highest_minute 1] = [expr [lindex $_highest_guard 1] * [lindex $_highest_minute 1]]"
}

# part 2
if {[lindex $_what_parts 1] == "true"} {
	puts "Running part 2"
	set _highest_guard [list 0 0 0]
	set _guards [dict create]
	foreach _line $_sorted_list {
		if {[regexp {Guard \S(\d*) begins shift} $_line _match _guard]} {
			set _current_guard $_guard
			#puts "GUARD ID-$_current_guard"
		} elseif {[regexp {(\d*):(\d*)\] falls} $_line _match _sleep_start_hour _sleep_start_minute]} {
			if {[regexp {(\d*):0(\d)\] falls} $_line _match _sleep_start_hour _sleep_start_minute]} {
				#puts "zero in minute"
			}
			#puts "Start-HOUR:0 MINUTE:$_sleep_start_minute"
		} elseif {[regexp {(\d*):(\d*)\] wakes} $_line _match _sleep_end_hour _sleep_end_minute]} {
			if {[regexp {(\d*):0(\d)\] wakes} $_line _match _sleep_end_hour _sleep_end_minute]} {
				#puts "zero in minute"
			}
			set _sleep_time [expr $_sleep_end_minute-$_sleep_start_minute]
			#puts "GUARD $_current_guard --- START $_sleep_start_minute --- STOP $_sleep_end_minute --- TIME $_sleep_time"
			for {set _current_minute $_sleep_start_minute} {$_current_minute < $_sleep_end_minute} {incr _current_minute} {
				if {[dict exists $_guards $_current_guard $_current_minute]} {
					dict set _guards $_current_guard $_current_minute [expr [dict get $_guards $_current_guard $_current_minute]+1]
					#puts "GUARD:$_current_guard--MINUTE:$_current_minute--COUNT:[dict get $_guards $_current_guard $_current_minute]"
				} else {
					dict set _guards $_current_guard $_current_minute 1
					#puts "GUARD:$_current_guard--MINUTE:$_current_minute--COUNT:[dict get $_guards $_current_guard $_current_minute]"
				}
				if {[dict get $_guards $_current_guard $_current_minute] > [lindex $_highest_guard 0]} {
						set _highest_guard [list [dict get $_guards $_current_guard $_current_minute] $_current_minute $_current_guard]
				}
			}
		}
	}
	puts "[lindex $_highest_guard 2] * [lindex $_highest_minute 1] = [expr [lindex $_highest_guard 2] * [lindex $_highest_guard 1]]"
}







