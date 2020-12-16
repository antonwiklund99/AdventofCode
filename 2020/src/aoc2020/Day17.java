package aoc2020;

import java.io.*;
import java.util.*;

public class Day17 {
	private List<String> input = new ArrayList<>();
	
	Day17() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data17.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {
		
	}
	
	private void part2() {
		
	}
	
	public static void main(String[] args) throws Exception {
		Day17 day = new Day17();
		day.part1();
		day.part2();
	}
}