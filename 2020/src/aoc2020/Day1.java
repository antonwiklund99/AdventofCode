package aoc2020;

import java.io.*;
import java.util.*;

public class Day1 {
	private List<Integer> input = new ArrayList<>();
	
	Day1() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data1.txt"));
		while (scan.hasNextLine()) {
			input.add(Integer.parseInt(scan.nextLine()));
		}
	}
	
	private void part1() {
		for (int i = 0; i < input.size(); i++) {
			for (int j = i + 1; j < input.size(); j++) {
				if (input.get(i) + input.get(j) == 2020) {
					System.out.println(input.get(i) * input.get(j));
					return;
				}
			}
		}
	}
	
	private void part2() {
		for (int i = 0; i < input.size(); i++) {
			for (int j = i + 1; j < input.size(); j++) {
				for (int k = j + 1; k < input.size(); k++) {
					if (input.get(i) + input.get(j) + input.get(k) == 2020) {
						System.out.println(input.get(i) * input.get(j) * input.get(k));
						return;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Day1 day = new Day1();
		day.part1();
		day.part2();
	}

}