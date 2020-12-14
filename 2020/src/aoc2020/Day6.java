package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {
	private List<String> input = new ArrayList<>();
	
	Day6() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data6.txt"));
		String group = "";
		while (scan.hasNextLine()) {
			String s = scan.nextLine();
			if (s.isEmpty()) {
				input.add(group);
				group = "";
			} else {
				group += s + "\n";
			}
		}
		if (!group.isEmpty()) input.add(group);
	}
	
	private void part1() {	
		int res = 0;
		for (String group: input) {
			Set<Character> answered = new HashSet<>();
			for (Character c: group.toCharArray()) {
				if (!answered.contains(c) && c != '\n') {
					res++;
					answered.add(c);
				}
			}
		}
		System.out.println(res);
	}
	
	private void part2() {
		int res = 0;
		for (String group: input) {
			Set<Character> answered = new HashSet<>();
			String[] splitted = group.split("\n");
			for (Character c: splitted[0].toCharArray()) {
				answered.add(c);
			}
			for (int i = 1; i < splitted.length; i++) {
				List<Character> removing = new ArrayList<>();
				for (Character c: answered) {
					if (splitted[i].indexOf(c) == -1) {
						removing.add(c);
					}
				}
				answered.removeAll(removing);
			}
			res += answered.size();
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) throws Exception {
		Day6 day = new Day6();
		day.part1();
		day.part2();
	}
}
