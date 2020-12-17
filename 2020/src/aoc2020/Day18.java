package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 {
	private List<String> input = new ArrayList<>();
	
	Day18() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data18.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {
		
	}
	
	private void part2() {
		
	}
	
	public static void main(String[] args) throws Exception {
		Day18 day = new Day18();
		day.part1();
		day.part2();
	}
}