DESIGN ?= blinky

build:
	dune build src
	dune build bin

fpga_build: build 
	bin/generate.exe ${DESIGN} > src/generated/top.v
	vivado/build.sh		

fpga_program: fpga_build
	vivado/program_fpga.tcl

clean:
	dune clean
	rm -f src/generated/top.v

clean_vivado:
	rm -rf vivado/build/*

.PHONY: build fpga_build fpga_program clean
