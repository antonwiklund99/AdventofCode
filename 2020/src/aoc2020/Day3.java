package aoc2020;

import java.io.*;
import java.util.*;

public class Day3 {
	private List<String> input = new ArrayList<>();
	private char[][] matrix;
	
	Day3() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data3.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
		matrix = new char[input.size()][input.get(0).length()];
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < input.get(0).length(); j++) {
				matrix[i][j] = input.get(i).charAt(j);
			}
		}
	}
	
	private void part1() {
		int i = 0, j = 0, trees = 0;
		while (i < matrix.length) {
			if (matrix[i][j] == '#') {
				trees++;
			}
			i++;
			j = (j + 3) % matrix[0].length;
		}
		System.out.println(trees);
	}
	
	private void part2() {
		int[] right = {1,3,5,7,1};
		int[] down = {1,1,1,1,2};
		long total = 1;
		for (int x = 0; x < down.length; x++) {
			int i = 0, j = 0, trees = 0;
			while (i < matrix.length) {
				if (matrix[i][j] == '#') {
					trees++;
				}
				i += down[x];
				j = (j + right[x]) % matrix[0].length;
			}
			total *= trees;
		}
		System.out.println(total);
	}
	
	public static void main(String[] args) throws Exception {
		Day3 day = new Day3();
		day.part1();
		day.part2();
	}
	
}