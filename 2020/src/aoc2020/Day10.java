package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {
	private List<Integer> input = new ArrayList<>();
	
	Day10() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data10.txt"));
		while (scan.hasNextLine()) {
			input.add(Integer.parseInt(scan.nextLine()));
		}
		input.sort((a,b) -> a - b);
		input.add(0, 0);
		input.add(input.get(input.size() - 1) + 3);
	}
	
	private long countCombs(int index, int end) {
		if (index == end) {
			return 1;
		} else if (index > end) {
			return 0;
		}
		long sum = 0;
		for (int i = 1; i <= 3; i++) {
			sum += countCombs(index + i, end);
		}
		return sum;
	}
	
	private void part1() {
		int ones = 0;
		int threes = 0;
		for (int i = 0; i < input.size()-1; i++) {
			if (input.get(i) + 1 == input.get(i+1)) {
				ones++;
			} else if (input.get(i) + 3 == input.get(i+1)) {
				threes++;
			}
		}
		System.out.println(ones * threes);
	}
	
	private void part2() {
		long res = 1;
		int ones = 0;
		for (int i = 0; i < input.size()-1; i++) {
			if (input.get(i) + 1 == input.get(i+1)) {	
				ones++;
			} else if (input.get(i) + 3 == input.get(i+1)) {
				res *= countCombs(0, ones);
				ones = 0;
			}
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) throws Exception {
		Day10 day = new Day10();
		day.part1();
		day.part2();
	}
	
}