package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {
	private List<String> input = new ArrayList<>();
	
	Day8() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data8.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {	
		Computer comp = new Computer(input);
		System.out.println(comp.runToTerminate());
	}
	
	private void part2() {
		Computer comp = new Computer(input);
		System.out.println(comp.fix());
	}
	
	public static void main(String[] args) throws Exception {
		Day8 day = new Day8();
		day.part1();
		day.part2();
	}
}
