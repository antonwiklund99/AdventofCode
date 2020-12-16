package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {
	private Map<String, Pair<Range,Range>> fields = new HashMap<>();
	private List<Integer> myTicket = new ArrayList<>();
	private List<List<Integer>> nearbyTickets = new ArrayList<>();
	
	Day16() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data16.txt"));
		boolean parsingMy = false, parsingNearby = false;
		Pattern fieldPattern = Pattern.compile("([\\w ]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)");
		while (scan.hasNextLine()) {
			String s = scan.nextLine();
			if (s.isBlank()) continue;
			if (parsingNearby) {
				nearbyTickets.add(Arrays.stream(s.split(",")).map((x) -> Integer.parseInt(x)).collect(Collectors.toList()));
			} else if (parsingMy) {
				if (s.contains("nearby tickets:")) {
					parsingNearby = true;
 				} else {
 					myTicket = Arrays.stream(s.split(",")).map((x) -> Integer.parseInt(x)).collect(Collectors.toList());
 				}
			} else {
				Matcher m = fieldPattern.matcher(s);
				if (m.find()) {
					Range r1 = new Range(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
					Range r2 = new Range(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)));
					fields.put(m.group(1), new Pair<>(r1,r2));
				} else {
					parsingMy = true;
				}
			}
		}
	}
	
	private List<String> findValidComb(List<String> used, List<String> remaining, List<Set<String>> valid) {
		if (remaining.isEmpty()) {
			return used;
		} else {
			for (int i = 0; i < remaining.size(); i++) {
				if (valid.get(used.size()).contains(remaining.get(i))) {
					used.add(remaining.get(i));
					String e = remaining.remove(i);
					List<String> res = findValidComb(used, remaining, valid);
					if (res != null) return res;
					remaining.add(i, e);
					used.remove(used.size()-1);
				}
			}
			return null;
		}
	}
	
	private void part1() {
		List<Range> possibleRanges = new ArrayList<>();
		for (String key: fields.keySet()) {
			Pair<Range,Range> pr = fields.get(key);
			possibleRanges.add(pr.getKey());
			possibleRanges.add(pr.getValue());
		}
		int res = 0;
		for (List<Integer> ticket: nearbyTickets) {
			for (Integer value: ticket) {
				boolean good = false;
				for (Range r: possibleRanges) {
					if (r.inRange(value)) {
						good = true;
						break;
					}
				}
				if (!good) res += value;
			}
		}
		System.out.println(res);
	}
	
	private void part2() {
		List<Range> possibleRanges = new ArrayList<>();
		for (String key: fields.keySet()) {
			Pair<Range,Range> pr = fields.get(key);
			possibleRanges.add(pr.getKey());
			possibleRanges.add(pr.getValue());
		}
		List<List<Integer>> valid = new ArrayList<>();
		for (List<Integer> ticket: nearbyTickets) {
			boolean good = false;
			for (Integer value: ticket) {
				good = false;
				for (Range r: possibleRanges) {
					if (r.inRange(value)) {
						good = true;
						break;
					}
				}
				if (!good) break;
			}
			if (good) valid.add(ticket);
		}
		
		List<Set<String>> validKeys = new ArrayList<>();
		for (int i = 0; i < valid.get(0).size(); i++) {
			Set<String> set = new HashSet<>();
			for (String key: fields.keySet()) {
				boolean invalid = false;
				Pair<Range,Range> p = fields.get(key);
				for (List<Integer> ticket: valid) {
					int value = ticket.get(i);
					if (!p.getKey().inRange(value) && !p.getValue().inRange(value)) {
						invalid = true;
						break;
					}
				}
				if (!invalid) set.add(key);
			}
			validKeys.add(set);
		}
		
		// Remove all ones
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 0; i < validKeys.size(); i++) {
				if (validKeys.get(i).size() == 1) {
					for (int j = 0; j < validKeys.size(); j++) {
						if (j != i) {
							int sizeBefore = validKeys.get(j).size();
							validKeys.get(j).removeAll(validKeys.get(i));
							if (sizeBefore != validKeys.get(j).size()) {
								changed = true;
							}
						}
					}
				}
			}
		}

		//List<String> comb = findValidComb(new ArrayList<String>(), new ArrayList<String>(fields.keySet()), validKeys);
		List<String> comb = validKeys.stream().map((s) -> new ArrayList<String>(s).get(0)).collect(Collectors.toList());
		long res = 1;
		for (int i = 0; i < comb.size(); i++) {
			if (comb.get(i).contains("departure")) res *= myTicket.get(i);
		}
		System.out.println(res);
	}

	public static void main(String[] args) throws Exception {
		Day16 day = new Day16();
		day.part1();
		day.part2();
	}
}