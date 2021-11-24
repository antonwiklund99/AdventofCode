package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day20 {
	private List<Tile> tiles = new ArrayList<>();
	public final int EDGE_LENGTH = 10;
	
	Day20() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data20.txt"));
		Pattern tileIdPattern = Pattern.compile("Tile (\\d+):");
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			while (line.isBlank()) line = scan.nextLine();
			Matcher m = tileIdPattern.matcher(line);
			m.find();
			char[][] grid = new char[EDGE_LENGTH][EDGE_LENGTH];
			for (int i = 0; i < EDGE_LENGTH; i++) {
				grid[i] = scan.nextLine().toCharArray();
			}
			tiles.add(new Tile(Integer.parseInt(m.group(1)), grid));
		}
		
		for (int k = 0; k < 5; k++) {
			Tile t = tiles.get(3);
			System.out.println(t + " x " + t.l + " " + t.r + " " + t.u + " " + t.d);
			t.rotateRight();
		}
		for (int i = 0; i < tiles.size(); i++) {
			System.out.println(tiles.get(1).r + " " + tiles.get(1).l + " " + tiles.get(1).u + " " + tiles.get(1).d + " " +tiles.get(1).adjusted);
			for (int j = 0; j < tiles.size(); j++) {
				if (i == j) continue;
				Tile t1 = tiles.get(i);
				Tile t2 = tiles.get(j);
				if (t1.left == t2 || t1.right == t2 || t1.up == t2 || t1.down == t2) {
					System.out.println(t1 + " has alreadt been matched with " + t2);
					continue;
				}
				System.out.println("WE MATCHING " + t1 + " " + t2);
				System.out.println(t1.u + " " + t1.d + " " + t1.r + " " + t1.l);
				System.out.println(t2.u + " " + t2.d + " " + t2.r + " " + t2.l);
				Tile adjustable;
				if (!t1.adjusted) {
					adjustable = t1;
				} else if (!t2.adjusted) {
					adjustable = t2;
				} else {
					System.out.println("both " + t1 + " and " + t2 + " has been adjusted. Only compare");
					System.out.println(t1.u + " " + t1.d + " " + t1.r + " " + t1.l);
					System.out.println(t2.u + " " + t2.d + " " + t2.r + " " + t2.l);
					if (t1.u == t2.d) {
						t1.up = t2;
						t2.down = t1;
					} else if (t1.d == t2.u) {
						t1.down = t2;
						t2.up = t1;
					} else if (t1.r == t2.l) {
						t1.right = t2;
						t2.left = t1;
					} else if (t1.l == t2.r) {
						t1.left = t2;
						t2.right = t1;
					}
					continue;
				}
				// Exact + Rotations
				boolean match = false;
				for (int k = 0; k < 4; k++) {
					if (t1.u == t2.d) {
						t1.up = t2;
						t2.down = t1;
						match = true;
						break;
					} else if (t1.d == t2.u) {
						t1.down = t2;
						t2.up = t1;
						match = true;
						break;
					} else if (t1.r == t2.l) {
						t1.right = t2;
						t2.left = t1;
						match = true;
						break;
					} else if (t1.l == t2.r) {
						t1.left = t2;
						t2.right = t1;
						match = true;
						break;
					}
					adjustable.rotateRight();
				}
				if (match) {
					t1.adjusted = true;
					t2.adjusted = true;
					System.out.println("rotated " + adjustable + " to match with " + (adjustable == t1 ? t2 : t1));
					continue;
				}
				
				// Flip vertical (top with bottom)
				for (int k = 0; k < 2; k++) {
					if (t1.u == t2.d) {
						t1.up = t2;
						t2.down = t1;
						match = true;
						break;
					} else if (t1.d == t2.u) {
						t1.down = t2;
						t2.up = t1;
						match = true;
						break;
					} else if (t1.r == t2.l) {
						t1.right = t2;
						t2.left = t1;
						match = true;
						break;
					} else if (t1.l == t2.r) {
						t1.left = t2;
						t2.right = t1;
						match = true;
						break;
					}
					adjustable.flipVertical();
				}
				if (match) {
					t1.adjusted = true;
					t2.adjusted = true;
					System.out.println("flipped vert " + adjustable + " to match with " + (adjustable == t1 ? t2 : t1));
					continue;
				}
				
				// Flip horizontal (left with right)
				for (int k = 0; k < 2; k++) {
					if (t1.u == t2.d) {
						t1.up = t2;
						t2.down = t1;
						match = true;
						break;
					} else if (t1.d == t2.u) {
						t1.down = t2;
						t2.up = t1;
						match = true;
						break;
					} else if (t1.r == t2.l) {
						t1.right = t2;
						t2.left = t1;
						match = true;
						break;
					} else if (t1.l == t2.r) {
						t1.left = t2;
						t2.right = t1;
						match = true;
						break;
					}
					adjustable.flipHorizontal();
				}
				if (match) {
					t1.adjusted = true;
					t2.adjusted = true;
					System.out.println("flipped hor " + adjustable + " to match with " + (adjustable == t1 ? t2 : t1));
					continue;
				}
			}
		}
	}
	
	static String toPaddedBinaryString(int i, int length) {
		StringBuilder sb = new StringBuilder();
		String binStr = Integer.toBinaryString(i);
		for (int n = binStr.length(); n < length; n++) sb.append('0');
		sb.append(binStr);
		return sb.toString();
	}
	
	private void part1() {
		long product = 1;
		for (Tile t : tiles) {
			System.out.println(t + " -> " + t.left + " " + t.right + " " + t.up + " " + t.down);
			/*
			if ((t.left == null && t.up == null) || (t.up == null && t.right == null) ||
			    (t.right == null && t.down == null) || (t.down == null && t.left == null)) {
				product *= t.id;
				System.out.println(t.id);
			}*/
		}
		System.out.println(product);
	}
	
	private void part2() {
		
	}
	
	public static void main(String[] args) throws Exception {
		Day20 day = new Day20();
		day.part1();
		day.part2();
	}
}

class Tile {
	int id;
	int l, u, r, d, invL, invU, invR, invD;
	Tile right, left, up, down;
	char[][] map;
	boolean adjusted = false;
	
	Tile(int id, char[][] grid) {
		this.id = id;
		
		int lastToggled = 1 << (grid.length-1);
		for (int i = 0; i < grid.length; i++) {
			if (grid[i][0] == '#') {
				l += (1 << i);
				invL += lastToggled >> i;
			}
			if (grid[i][grid.length-1] == '#') {
				r += (1 << i);
				invR += lastToggled >> i;
			}
			if (grid[0][i] == '#') {
				u += (1 << i);
				invU += lastToggled >> i;
			}
			if (grid[grid.length-1][i] == '#') {
				d += (1 << i);
				invD += lastToggled >> i;
			}
		}
		
		map = new char[grid.length-2][grid.length-2];
		for (int i = 1; i < grid.length-1; i++) {
			for (int j = 1; j < grid.length-1; j++) {
				map[i-1][j-1] = grid[i][j];
			}
		}
	}
	
	public void rotateRight() {
		int tmpL = l, tmpInvL = invL;
		l = d;
		invL = invD;
		d = invR;
		invD = r;
		r = u;
		invR = invU;
		u = tmpInvL;
		invU = tmpL;
	}
	
	public void flipVertical() {
		int tmpU = u, tmpInvU = u;
		u = d;
		invU = invD;
		d = tmpU;
		invD = tmpInvU;
	}
	
	public void flipHorizontal() {
		int tmpL = l, tmpInvL = invL;
		l = r;
		invL = invR;
		r = tmpL;
		invR = tmpInvL;
	}
	
	public String toString() {
		return Integer.toString(id);
	}
}