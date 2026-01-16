Solutions for Advent of code 2025 implemented in Hardcaml on a Cmod A7-35T (Artix-7 FPGA).

All implemented solutions have a passing testbench and is working when run on the board. 

Each solution implements the Solver interface which is instatiated in the top with a UART module. The input data is sent and results read back from the FPGA through the USB2UART module on the board, the FPGA uses XON/XOFF flow control to give backpressure if needed.

### Run testbench
`dune test`

### Build and run on FPGA
Possible designs are: blinky, uart_test (looped UART for testing XON/XOFF) and day1 -> day5
```
make fpga_build DESIGN=<design>
vivado/program_fpga.sh <path to bitstream>
python3 run.py <design>
```
