#!/usr/bin/tclsh

proc reaction {_string} {
	set letters "abcdefghijklmnopqrstuvwxyz"
	foreach letter [split $letters {}] {
		set pattern1 "$letter[string toupper $letter]"
		set pattern2 "[string toupper $letter]$letter"
		set _string [regsub -all $pattern1 $_string ""]
		set _string [regsub -all $pattern2 $_string ""]
	}
	return $_string
}

proc remove_letters {_string letter} {
	set _string [regsub -all $letter $_string ""]
	set _string [regsub -all [string toupper $letter] $_string ""]
	return $_string
}

proc fully_react {_string} {
	while {1} {
		set _before [string length $_string]
		set _string [reaction $_string]
		set _after [string length $_string]
		if {$_before==$_after} {
			return $_string
		}
	}
}

source "file_access.tcl"
set _input [open data5 r]
set _string [regsub -all {\n} [read $_input] {}]
close $_input
set _what_parts [read_args $argv]


if {[lindex $_what_parts 0] == "true"} {
	puts "Running part 1"
	set _result [fully_react $_string]
	puts "Done ---- Length after reaction: [string length $_result]"
}

if {[lindex $_what_parts 1] == "true"} {
	puts "Running part 2"
	set _string [fully_react $_string]
	set shortest 0
	set letters "abcdefghijklmnopqrstuvwxyz"
	foreach letter [split $letters {}] {
		set _changed [fully_react [remove_letters $_string $letter]]
		puts "Letter: $letter --- Length: [string length $_changed]"
		if {[string length $_changed] < $shortest || $shortest == 0} {
			set shortest [string length $_changed]
			set shortest_letter $letter
		}
	}
	puts "Shortest: $shortest_letter ---- Length: $shortest"
}
