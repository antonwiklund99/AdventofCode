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
		int max = 0;
		for (String line: input) {
			int upper = 127;
			int lower = 0;
			for (int i = 0; i < 7; i++) {
				if (line.charAt(i) == 'F') {
					upper -= (upper - lower)/2 + 1;
				} else {
					lower += (upper - lower)/2 + 1;
				}
			}
			int row = lower;
			upper = 7;
			lower = 0;
			for (int i = 7; i < 10; i++) {
				if (line.charAt(i) == 'L') {
					upper -= (upper - lower)/2 + 1;
				} else {
					lower = (upper - lower)/2 + 1;
				}
			}
			max = Math.max(max, 8*row + upper);
		}
		System.out.println(max);
	}
	
	private void part2() {
		Set<Integer> ids = new HashSet<>();
		int max = 0;
		int min = Integer.MAX_VALUE;
		for (String line: input) {
			int upper = 127;
			int lower = 0;
			for (int i = 0; i < 7; i++) {
				if (line.charAt(i) == 'F') {
					upper -= (upper - lower)/2 + 1;
				} else {
					lower += (upper - lower)/2 + 1;
				}
			}
			int row = lower;
			upper = 7;
			lower = 0;
			for (int i = 7; i < 10; i++) {
				if (line.charAt(i) == 'L') {
					upper -= (upper - lower)/2 + 1;
				} else {
					lower += (upper - lower)/2 + 1;
				}
			}
			ids.add(8*row + upper);
			max = Math.max(max, 8*row + upper);
			min = Math.min(min, 8*row + upper);
		}
		
		int middle = max - (max - min)/2;
		for (int i = 0; i < (max - min)/2; i++) {
			if (!ids.contains(middle - i) && ids.contains(middle - i + 1) && ids.contains(middle - i - 1)) {
				System.out.println(middle - i);
				break;
			}
			if (!ids.contains(middle + i) && ids.contains(middle + i + 1) && ids.contains(middle + i - 1)) {
				System.out.println(middle + i);
				break;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Day5 day = new Day5();
		day.part1();
		day.part2();
	}
}