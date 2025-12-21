#!/bin/bash
vivado -mode batch -source program_fpga.tcl -tclargs "$1"
