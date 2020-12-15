package aoc2020;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 {
	private List<Integer> input;
	
	Day15() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data15.txt"));
		while (scan.hasNextLine()) {
			input = Arrays.stream(scan.nextLine().split(",")).map((x) -> Integer.parseInt(x)).collect(Collectors.toList());
		}
	}
	
	private void part1() {
		Map<Integer, Integer> wordsSpoken = new HashMap<>();
		for (int i = 0; i < input.size(); i++) {
			wordsSpoken.put(input.get(i), i);
		}
		int next = 0;
		for (int turn = input.size(); turn < 2019; turn++) {
			int n = next;
			next = wordsSpoken.containsKey(next) ? turn - wordsSpoken.get(next) : 0;
			wordsSpoken.put(n, turn);
		}
		System.out.println(next);
	}
	
	private void part2() {
		Map<Integer, Integer> wordsSpoken = new HashMap<>();
		for (int i = 0; i < input.size(); i++) {
			wordsSpoken.put(input.get(i), i);
		}
		int next = 0;
		for (int turn = input.size(); turn < 29999999; turn++) {
			int n = next;
			next = wordsSpoken.containsKey(next) ? turn - wordsSpoken.get(next) : 0;
			wordsSpoken.put(n, turn);
		}
		System.out.println(next);
	}

	public static void main(String[] args) throws Exception {
		Day15 day = new Day15();
		day.part1();
		day.part2();
	}
}