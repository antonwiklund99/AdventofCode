set bitstream_file [lindex $argv 0]

# Open hardware manager to program the device
open_hw_manager

# Connect to the device (Ensure correct JTAG connection)
connect_hw_server
open_hw_target

# Program the device with the generated bitstream
puts "Program FPGA with $bitstream_file"
set_property PROGRAM.FILE $bitstream_file [lindex [get_hw_devices] 0]
program_hw_devices [lindex [get_hw_devices] 0]
