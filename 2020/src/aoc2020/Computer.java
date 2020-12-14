package aoc2020;

import java.util.*;

public class Computer {
	private long acc = 0;
	private int pc = 0;
	private String[] instructions;
	
	Computer(List<String> instructions) {
		this.instructions = (String[]) instructions.toArray(String[] ::new); 
	}
	
	public long runToTerminate() {
		pc = 0;
		acc = 0;
		Set<Integer> visited = new HashSet<>();
		while (!visited.contains(pc) && pc < instructions.length) {
			visited.add(pc);
			step();
		}
		return acc;
	}
	
	public long fix() {
		for (int i = 0; i < instructions.length; i++) {
			String opcode = instructions[i].substring(0,3);
			long arg = Long.parseLong(instructions[i].substring(4));
			if (opcode.equals("nop")) {
				instructions[i] = "jmp" + " " + arg;
				runToTerminate();
				instructions[i] = "nop" + " " + arg;
			} else if (opcode.equals("jmp")) {
				instructions[i] = "nop" + " " + arg;
				runToTerminate();
				instructions[i] = "jmp" + " " + arg;
			}
			if (pc >= instructions.length) {
				return acc;
			}
		}
		return -1;
	}
	
	public void step() {
		String opcode = instructions[pc].substring(0,3);
		long arg = Long.parseLong(instructions[pc].substring(4));
		
		switch (opcode) {
		case "nop":
			pc++;
			break;
		case "acc":
			acc += arg;
			pc++;
			break;
		case "jmp":
			pc += arg;
			break;
		default:
			System.out.println("UNKNOWN OPCODE IN " + instructions[pc]);
		}
	}
}
