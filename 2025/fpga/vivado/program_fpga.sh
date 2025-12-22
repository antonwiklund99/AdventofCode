#!/bin/bash
set -euo pipefail

VIVADO_DIR=$(dirname "$0")
vivado -mode batch -source $VIVADO_DIR/program_fpga.tcl -tclargs "$1"
