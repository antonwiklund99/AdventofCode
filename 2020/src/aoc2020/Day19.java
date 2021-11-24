package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day19 {
	private Map<Integer,Rule> allRules = new HashMap<>();
	private List<String> messages = new ArrayList<>();
	
	Day19() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data19.txt"));
		List<String> ruleLines = new ArrayList<String>();
		boolean allRulesCounted = false;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (!allRulesCounted) {
				if (line.isBlank()) {
					allRulesCounted = true;
				} else {
					ruleLines.add(line);
				}
			} else {
				messages.add(line);
			}
		}
		
		Pattern rule = Pattern.compile("(\\d+)\\: (.+)");
		Pattern constant = Pattern.compile("(\\d+)\\: \"(\\w)\"");
		for (int i = 0; i < ruleLines.size(); i++) {
			String line = ruleLines.get(i);
			
			Matcher m = constant.matcher(line);
			if (m.find()) {
				allRules.put(Integer.parseInt(m.group(1)), new Constant(m.group(2).charAt(0)));
				continue;
			} 
			
			m = rule.matcher(line);
			if (m.find()) {
				String[] splitted = m.group(2).split(" \\| ");
				if (splitted.length > 1) {
					allRules.put(Integer.parseInt(m.group(1)), new PipeRule(splitted, allRules));
				} else {
					allRules.put(Integer.parseInt(m.group(1)), new SubRule(splitted[0], allRules));
				}
			}
		};
	}
	
	private void part1() {
		int matches = 0;
		for (String msg : messages) {
			List<String> res = allRules.get(0).match(List.of(msg));
			if (res != null) {
				for (String s : res) {
					if (s.isEmpty()) matches++;
				}
			}
		}
		System.out.println(matches);
	}
	
	private void part2() {
		allRules.put(8, new LoopedRule(allRules.get(42)));
		allRules.put(11, new Rule11(allRules.get(42), allRules.get(31)));
		int matches = 0;
		for (String msg : messages) {
			List<String> res = allRules.get(0).match(List.of(msg));
			if (res != null) {
				for (String s : res) {
					if (s.isEmpty()) {
						matches++;
						break;
					}
				}
			}
		}
		System.out.println(matches);
	}
	
	public static void main(String[] args) throws Exception {
		Day19 day = new Day19();
		day.part1();
		day.part2();
	}
}

interface Rule {
	List<String> match(List<String> s);
}

class Constant implements Rule {
	private char c;
	
	Constant(char c) {
		this.c = c;
	}
	
	public List<String> match(List<String> s) {
		List<String> res = new ArrayList<>();
		for (String str : s) {
			if (!str.isEmpty() && str.charAt(0) == c) {
				res.add(str.substring(1));
			}
		}
		if (res.isEmpty()) return null;
		return res;
	}
	
	public String toString() {
		return "\"" + c + "\"";
	}
}

class SubRule implements Rule {
	private Map<Integer,Rule> allRules;
	private int[] rules;
	
	SubRule(String nums, Map<Integer,Rule> allRules) {
		this.allRules = allRules;
		String[] splitted = nums.split(" ");
		rules = new int[splitted.length];
		for (int i = 0; i < splitted.length; i++) {
			rules[i] = Integer.parseInt(splitted[i]);
		}
	}
	
	public List<String> match(List<String> s) {
		List<String> newStrings = s;
		for (int i = 0; i < rules.length; i++) {
			Rule rule = allRules.get(rules[i]);
			List<String> afterMatch = rule.match(newStrings);
			newStrings = new ArrayList<>();
			if (afterMatch != null) {
				for (String str : afterMatch) {
					if (i == rules.length-1 || !str.isEmpty()) {
						newStrings.add(str);
					}
				}
			}
			if (newStrings.isEmpty()) return null;
		}
		return newStrings;
	}
	
	public String toString() {
		return Arrays.toString(rules);
	}
}

class PipeRule implements Rule {
	private SubRule[] rules;
	
	PipeRule(String[] pipes, Map<Integer,Rule> allRules) {
		rules = new SubRule[pipes.length];
		for (int i = 0; i < pipes.length; i++) {
			rules[i] = new SubRule(pipes[i], allRules);
		}
	}
	
	public List<String> match(List<String> s) {
		List<String> newStrings = new ArrayList<>();
		for (int i = 0; i < rules.length; i++) {
			List<String> res = rules[i].match(s);
			if (res != null) newStrings.addAll(res);
		}
		if (newStrings.isEmpty()) return null;
		return newStrings;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rules.length; i++) {
			sb.append(rules[i].toString() + " | ");
		}
		return sb.substring(0, sb.length()-3);
	}
}

class LoopedRule implements Rule {
	private Rule innerRule;
	
	LoopedRule(Rule innerRule) {
		this.innerRule = innerRule;
	}
	
	public List<String> match(List<String> s) {
		List<String> res = s;
		List<String> allStrs = new ArrayList<>();
		while (true) {
			res = innerRule.match(res);
			if (res == null) break;
			allStrs.addAll(res);
		}
		if (allStrs.isEmpty()) return null;
		return allStrs;
	}
}

class Rule11 implements Rule {
	private Rule firstRule;
	private Rule secondRule;
	
	Rule11(Rule firstRule, Rule secondRule) {
		this.firstRule = firstRule;
		this.secondRule = secondRule;
	}
	
	public List<String> match(List<String> s) {
		List<String> resAfterFirst = s;
		List<String> newStrings = new ArrayList<>();
		int n = 0;
		while (true) {
			resAfterFirst = firstRule.match(resAfterFirst);
			n++;
			if (resAfterFirst == null) break;
			List<String> res = resAfterFirst;
			for (int i = 0; i < n; i++) {
				res = secondRule.match(res);
				if (res == null) break;
			}
			if (res != null) {
				newStrings.addAll(res);
			}
		}
		if (newStrings.isEmpty()) return null;
		return newStrings;
	}
}