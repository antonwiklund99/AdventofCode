package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {
	private List<String> input = new ArrayList<>();
	
	Day16() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data16.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {
		
	}
	
	private void part2() {
		
	}

	public static void main(String[] args) throws Exception {
		Day16 day = new Day16();
		day.part1();
		day.part2();
	}
}