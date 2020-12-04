package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {
	private List<String> input = new ArrayList<>();
	
	Day5() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data5.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {
		
	}
	
	private void part2() {
		
	}
	
	public static void main(String[] args) throws Exception {
		Day5 day = new Day5();
		day.part1();
		day.part2();
	}
}