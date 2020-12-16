package aoc2020;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 {
	private List<String> input = new ArrayList<>();
	
	Day13() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data13.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private static long modInverse(long a, long m) { 
		a = a % m; 
		for (int i = 1; i < m; i++) {
		    if ((a * i) % m == 1) return i;
		}
		return 1; 
	}
	
	private void part1() {	
		long earliest = Long.parseLong(input.get(0));
		List<Long> ids = Arrays.stream(input.get(1).split(",")).filter((s) -> !s.equals("x")).map((s) -> Long.parseLong(s)).collect(Collectors.toList());
		long i = earliest;
		boolean done = false;
		long resId = -1;
		while (!done) {
			for (Long id: ids) {
				if (i % id == 0) {
					resId = id;
					done = true;
					break;
				}
			}
			i += done ? 0 : 1;
		}
		System.out.println((i - earliest)*resId);
	}
	
	private void part2() {
		List<Long> ids = Arrays.stream(input.get(1).split(",")).map((s) -> s.equals("x") ? 1 : Long.parseLong(s)).collect(Collectors.toList());
		long product = ids.stream().reduce(1L, (x, y) -> x*y);
		long sum = 0;
		for (int i = 0; i < ids.size(); i++) {
			if (ids.get(i) != 1) {
				long part = product/ids.get(i);
				sum += part * modInverse(part, ids.get(i)) * (ids.get(i) - i);
			}
		}
		System.out.println(sum % product);
	}

	public static void main(String[] args) throws Exception {
		Day13 day = new Day13();
		day.part1();
		day.part2();
	}
}