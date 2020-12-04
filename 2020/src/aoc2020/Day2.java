package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
	private List<String> input = new ArrayList<>();
	
	Day2() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data2.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {
		Pattern pattern = Pattern.compile("(\\d+)-(\\d+) (\\w): (\\w+)");
		int res = 0;
		for (String line : input) {
			Matcher m = pattern.matcher(line);
			m.find();
			int lower = Integer.parseInt(m.group(1)), upper = Integer.parseInt(m.group(2));
			char letter = m.group(3).charAt(0);
			int occ = 0;
			for (char c: m.group(4).toCharArray()) {
				if (c == letter) {
					occ++;
				}
			}
			if (lower <= occ && occ <= upper) res++;
		}
		System.out.println(res);
	}
	
	private void part2() {
		Pattern pattern = Pattern.compile("(\\d+)-(\\d+) (\\w): (\\w+)");
		int res = 0;
		for (String line : input) {
			Matcher m = pattern.matcher(line);
			m.find();
			int lower = Integer.parseInt(m.group(1)), upper = Integer.parseInt(m.group(2));
			char letter = m.group(3).charAt(0);
			if (m.group(4).charAt(lower - 1) == letter ^ m.group(4).charAt(upper - 1) == letter) res++;
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) throws Exception {
		Day2 day = new Day2();
		//day.part1();
		day.part2();
	}
	
}
