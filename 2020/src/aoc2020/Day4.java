package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
	private ArrayList<String> input = new ArrayList<>();
	
	Day4() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data4.txt"));
		String k = "";
		while (scan.hasNextLine()) {
			String s = scan.nextLine();
			if (s.isEmpty()) {
				input.add(k);
				k = "";
			} else {
				k += s + " ";
			}
		}
		if (!k.isEmpty()) {
			input.add(k);
		}
	}
	
	private void part1() {
		String[] required = {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
		int res = 0;
		for (String passport : input) {
			boolean good = true;
			for (String key: required) {
				if (!passport.contains(key + ":")) {
					good = false;
					break;
				}
			}
			if (good) res++;
		}
		System.out.println(res);
	}
	
	private void part2() {
		int res = 0;
		Pattern[] patterns = {
			Pattern.compile("byr:(19[2-9]\\d|200[0-2])"),
			Pattern.compile("iyr:(201\\d|2020)"),
			Pattern.compile("eyr:(202\\d|2030)"),
			Pattern.compile("hgt:(((59|6\\d|7[0-6])in)|(1([5-8]\\d|9[0-3])cm))"),
			Pattern.compile("hcl:(#[0-9a-f]{6})"),
			Pattern.compile("ecl:(amb|blu|brn|gry|grn|hzl|oth)"),
			Pattern.compile("pid:\\d{9}[^\\d]"),
		};
		for (String passport : input) {
			boolean good = true;
			for (Pattern pattern: patterns) {
				Matcher m = pattern.matcher(passport);
				if (!m.find()) {
					good = false;
					break;
				}
			}
			if (good) res++;
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) throws Exception {
		Day4 day = new Day4();
		//day.part1();
		day.part2();
	}
	
}
