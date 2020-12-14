package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {
	Map<String, List<Pair<String, Integer>>> bags = new HashMap<>();
	
	Day7() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data7.txt"));
		Pattern parent = Pattern.compile("(\\w+ \\w+) bags contain (.+)");
		Pattern child = Pattern.compile("(\\d+) (\\w+ \\w+) bags?");
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			Matcher m1 = parent.matcher(line);
			m1.find();
			bags.put(m1.group(1), new ArrayList<>());
			for (String s: m1.group(2).split(",")) {
				Matcher m2 = child.matcher(s);
				if (m2.find()) {
					bags.get(m1.group(1)).add(new Pair<String, Integer>(m2.group(2), Integer.parseInt(m2.group(1))));
				}
			}
		}
	}
	
	private boolean containsShinyGold(String bag) {
		if (bag.equals("shiny gold")) {
			return true;
		}
		for (Pair<String, Integer> b: bags.get(bag)) {
			if (containsShinyGold(b.getKey())) {
				return true;
			}
		}
		return false;
	}
	
	private int countBags(String bag) {
		int count = 1;
		for (Pair<String, Integer> b: bags.get(bag)) {
			count += countBags(b.getKey())*b.getValue();
		}
		return count;
	}
	
	private void part1() {
		int res = 0;
		for (String bag: bags.keySet()) {
			if (!bag.equals("shiny gold") && containsShinyGold(bag)) {
				res++;
			}
		}
		System.out.println(res);
	}
	
	private void part2() {
		System.out.println(countBags("shiny gold") - 1);
	}
	
	public static void main(String[] args) throws Exception {
		Day7 day = new Day7();
		day.part1();
		day.part2();
	}
}