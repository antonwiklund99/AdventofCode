package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day11 {
	private char[][] input;
	
	Day11() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data11.txt"));
		List<char[]> lines = new ArrayList<>();
		while (scan.hasNextLine()) {
			lines.add(scan.nextLine().toCharArray());
		}
		input = new char[lines.size()][];
		for (int i = 0; i < input.length; i++) {
			input[i] = lines.get(i);
		}
	}
	
	private int nbrsInDirection(int dx, int dy, int startX, int startY) {
		int x = startX + dx;
		int y = startY + dy;
		while (x >= 0 && x < input[0].length && y >= 0 && y < input.length) {
			if (input[y][x] == '#') return 1;
			if (input[y][x] == 'L') return 0;
			x += dx;
			y += dy;
		}
		return 0;
	}
	
	private void part1() {
		boolean done = false;
		while (!done) {
			char[][] next = new char[input.length][input[0].length];
			done = true;
			for (int i = 0; i < input.length; i++) {
				for (int j = 0; j < input[0].length; j++) {
					int nbrs = 0;
					if (i > 0 && j > 0 && input[i-1][j-1] == '#') nbrs++;
					if (i > 0 && input[i-1][j] == '#') nbrs++;
					if (i > 0 && j < input[0].length-1 && input[i-1][j+1] == '#') nbrs++;
					if (j > 0 && input[i][j-1] == '#') nbrs++;
					if (j < input[0].length-1 && input[i][j+1] == '#') nbrs++;
					if (i < input.length-1 && j > 0 && input[i+1][j-1] == '#') nbrs++;
					if (i < input.length-1 && input[i+1][j] == '#') nbrs++;
					if (i < input.length-1 && j < input[0].length-1 && input[i+1][j+1] == '#') nbrs++;
					
					if (input[i][j] == '#' && nbrs >= 4) {
						done = false;
						next[i][j] = 'L';
					} else if (input[i][j] == 'L' && nbrs == 0) {
						done = false;
						next[i][j] = '#';
					} else {
						next[i][j] = input[i][j];
					}
				}
			}
			input = next;
		}
		int res = 0;
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length;j++) {
				if (input[i][j] == '#') res++;
			}
		}
		System.out.println(res);
	}
	
	private void part2() {
		boolean done = false;
		while (!done) {
			char[][] next = new char[input.length][input[0].length];
			done = true;
			for (int i = 0; i < input.length; i++) {
				for (int j = 0; j < input[0].length; j++) {
					int nbrs = 0;
					nbrs += nbrsInDirection(-1,-1,j,i);
					nbrs += nbrsInDirection(0,-1,j,i);
					nbrs += nbrsInDirection(1,-1,j,i);
					nbrs += nbrsInDirection(-1,0,j,i);
					nbrs += nbrsInDirection(1,0,j,i);
					nbrs += nbrsInDirection(-1,1,j,i);
					nbrs += nbrsInDirection(0,1,j,i);
					nbrs += nbrsInDirection(1,1,j,i);
					
					if (input[i][j] == '#' && nbrs >= 5) {
						done = false;
						next[i][j] = 'L';
					} else if (input[i][j] == 'L' && nbrs == 0) {
						done = false;
						next[i][j] = '#';
					} else {
						next[i][j] = input[i][j];
					}
				}
			}
			input = next;
		}
		int res = 0;
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length;j++) {
				if (input[i][j] == '#') res++;
			}
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) throws Exception {
		Day11 day = new Day11();
		//day.part1();
		day.part2();
	}
	
}