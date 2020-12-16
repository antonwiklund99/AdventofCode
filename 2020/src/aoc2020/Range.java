package aoc2020;

public class Range {
	private int start;
	private int end;
	
	Range(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean inRange(int x) {
		return start <= x && x <= end;
	}
	
	public String toString() {
		return start + " -> " + end;
	}
}
