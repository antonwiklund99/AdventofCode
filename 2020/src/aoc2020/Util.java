package aoc2020;

import java.util.List;

public class Util {
	public static long maxInRange(List<Long> list, int start, int end) {
		long n = list.get(start);
		for (int i = start+1; i < end; i++) {
			n = Math.max(n, list.get(i));
		}
		return n;
	}
	
	public static long minInRange(List<Long> list, int start, int end) {
		long n = list.get(start);
		for (int i = start+1; i < end; i++) {
			n = Math.min(n, list.get(i));
		}
		return n;
	}
}
