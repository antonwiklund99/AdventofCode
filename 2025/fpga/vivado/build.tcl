set build_dir [lindex $argv 0]
puts "Build directory: $build_dir"

set vivado_dir [file normalize [info nameofexecutable]]

read_verilog $build_dir/top.v
read_xdc $build_dir/Cmod-A7-Master.xdc

synth_design -top top -part xc7a35tcpg236-1
write_checkpoint -force $build_dir/post_synth.dcp
report_utilization -file $build_dir/post_synth_util.rpt

opt_design
place_design
write_checkpoint -force $build_dir/post_place.dcp

route_design
write_checkpoint -force $build_dir/post_route.dcp
write_bitstream -force $build_dir/out.bit

