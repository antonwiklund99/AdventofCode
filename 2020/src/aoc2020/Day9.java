package aoc2020;

import java.io.*;
import java.util.*;

public class Day9 {
	private List<Long> input = new ArrayList<>();
	
	Day9() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data9.txt"));
		while (scan.hasNextLine()) {
			input.add(Long.parseLong(scan.nextLine()));
		}
	}
	
	private boolean isValid(long n, int start, int end) {
		for (int i = start; i <= end; i++) {
			for (int j = i + 1; j <= end; j++) {
				if (n == input.get(i) + input.get(j)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void part1() {
		int start = 0;
		int end = 24;
		while (isValid(input.get(end + 1), start, end)) {
			start++;
			end++;
		}
		System.out.println(input.get(end + 1));
	}
	
	private void part2() {
		long n = 15690279;
		int i = 0;
		boolean found = false;
		while (!found) {
			int j = 0;
			long sum = 0;
			while (sum < n) {
				sum += input.get(i+j);
				if (sum == n) {
					found = true;
					System.out.println(Util.maxInRange(input, i, i+j) + Util.minInRange(input, i, i+j));
				}
				j++;
			}
			i++;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Day9 day = new Day9();
		day.part1();
		day.part2();
	}
	
}