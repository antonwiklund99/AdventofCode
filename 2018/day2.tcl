#!/usr/bin/tclsh

source "file_access.tcl"
set _id_list [read_file "data2"]
set _what_parts [read_args $argv]

# part 1
if {[lindex $_what_parts 0] == "true"} {
	puts "Running part 1"
	set _two_count 0
	set _three_count 0

	foreach _box_id $_id_list {
		set _letter_dict [dict create]
		foreach letter [split $_box_id {}] {
			if {[dict exists $_letter_dict $letter]} {
				dict set _letter_dict $letter [expr [dict get $_letter_dict $letter] + 1]
			} else {
				dict set _letter_dict $letter 1
			}
		}
		if {[lsearch [dict values $_letter_dict] 2] != -1} {
				set _two_count [expr $_two_count + 1]
				#puts "$_two_count --- Added to two count"
		}
		if {[lsearch [dict values $_letter_dict] 3] != -1} {
				set _three_count [expr $_three_count + 1]
				#puts "$_three_count --- Added to three count"
		}
	}
	puts "two count * three count = checksum"
	puts "$_two_count * $_three_count = [expr $_two_count*$_three_count]"
}

# part 2
if {[lindex $_what_parts 1] == "true"} {
	puts "Running part 2"
	set _tried_ids [list create]

	foreach _box_id $_id_list {
		foreach _compare_id $_id_list {
			if {[lsearch $_tried_ids $_compare_id] == -1 && $_box_id != $_compare_id} {
				set _diff 0
				for {set n 0} {$n < [string length $_box_id]} {incr n} {
					if {[lindex [split $_box_id {}] $n] != [lindex [split $_compare_id {}] $n]} {
						if {$_diff == 0} {
							set _diff 1
							set _diff_place $n
						} else {
							set _diff 2
							break
						}
					}
				}

				if {$_diff == 1} {
					puts "Found only one difference between $_box_id and $_compare_id"
					puts "Answer: [string replace $_box_id $n $n ""]"
					exit
				}
			}
		}
		lappend _tried_ids $_box_id
	}
}

