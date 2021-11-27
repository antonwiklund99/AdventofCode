package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day20 {
	private List<Tile> tiles = new ArrayList<>();
	private Tile[][] tileArrangement;
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
		tileArrangement = new Tile[(int) Math.sqrt(tiles.size())][(int) Math.sqrt(tiles.size())];
		
		// Generate all possible tiles
		ArrayList<Tile> possibleTiles = new ArrayList<>();
		for (Tile tile : tiles) {
			for (int n1 = 0; n1 < 2; n1++) {
				for (int n2 = 0; n2 < 4; n2++) {
					possibleTiles.add(tile);
					tile = tile.rotated();
				}
				tile = tile.flipped();
			}
		}
		tiles = possibleTiles;
		
	}
	
	private boolean matchBorders(int i, int j, Set<Integer> alreadyChoosen) {
		Tile up = i > 0 ? tileArrangement[i-1][j] : null;
		Tile left = j > 0 ? tileArrangement[i][j-1] : null;
		int nextI = ((i*tileArrangement.length)+j+1) / tileArrangement.length;
		int nextJ = ((i*tileArrangement.length)+j+1) % tileArrangement.length;
		for (Tile tile : tiles) {
			if (!alreadyChoosen.contains(tile.id) && (up == null || up.canBeBelow(tile)) && (left == null || left.canBeRight(tile))) {
				alreadyChoosen.add(tile.id);
				tileArrangement[i][j] = tile;
				if (i == tileArrangement.length-1 && j == tileArrangement.length-1) {
					return true;
				}
				if (matchBorders(nextI, nextJ, alreadyChoosen)) {
					return true;
				}
				alreadyChoosen.remove(tile.id);
			}
		}
		return false;
	}
	
	private void part1() {
		matchBorders(0,0, new HashSet<>());
		System.out.println(((long) tileArrangement[0][0].id) *
				           ((long) tileArrangement[0][tileArrangement.length-1].id) *
				           ((long) tileArrangement[tileArrangement.length-1][0].id) *
				           ((long) tileArrangement[tileArrangement.length-1][tileArrangement.length-1].id));
	}
	
	private void part2() {
		// Create a big tile of all aligned tiles
		char[][] bigGrid = new char[tileArrangement.length*(EDGE_LENGTH-2)][tileArrangement.length*(EDGE_LENGTH-2)];
		for (int i = 0; i < tileArrangement.length; i++) {
			for (int j = 0; j < tileArrangement.length; j++) {
				for (int row = 1; row < EDGE_LENGTH-1; row++) {
					for (int col = 1; col < EDGE_LENGTH-1; col++) {
						bigGrid[i*(EDGE_LENGTH-2) + row - 1][j*(EDGE_LENGTH-2) + col - 1] = tileArrangement[i][j].grid[row][col];
					}
				}
			}
		}
		Tile bigTile = new Tile(0, bigGrid);
		Tile monsterTile = new Tile(0, bigGrid.clone());
		for (int n1 = 0; n1 < 2; n1++) {
			for (int n2 = 0; n2 < 4; n2++) {
				// Check for pattern
				for (int i = 0; i < bigTile.grid.length-2; i++) {
					for (int j = 18; j < bigTile.grid.length-1; j++) {
						if (bigTile.grid[i][j] == '#' && bigTile.grid[i+1][j-18] == '#' && bigTile.grid[i+1][j-13] == '#' &&
							bigTile.grid[i+1][j-12] == '#' && bigTile.grid[i+1][j-6] == '#' && bigTile.grid[i+1][j-7] == '#' &&
							bigTile.grid[i+1][j-1] == '#' && bigTile.grid[i+1][j] == '#' && bigTile.grid[i+1][j+1] == '#' &&
							bigTile.grid[i+2][j-17] == '#' && bigTile.grid[i+2][j-14] == '#' && bigTile.grid[i+2][j-11] == '#' &&
							bigTile.grid[i+2][j-8] == '#' && bigTile.grid[i+2][j-5] == '#' && bigTile.grid[i+2][j-2] == '#') {
							monsterTile.grid[i][j] = 'O';
							monsterTile.grid[i+1][j-18] = 'O';
							monsterTile.grid[i+1][j-13] = 'O';
							monsterTile.grid[i+1][j-12] = 'O';
							monsterTile.grid[i+1][j-6] = 'O';
							monsterTile.grid[i+1][j-7] = 'O';
							monsterTile.grid[i+1][j-1] = 'O';
							monsterTile.grid[i+1][j] = 'O';
							monsterTile.grid[i+1][j+1] = 'O';
							monsterTile.grid[i+2][j-17] = 'O';
							monsterTile.grid[i+2][j-14] = 'O';
							monsterTile.grid[i+2][j-11] = 'O';
							monsterTile.grid[i+2][j-8] = 'O';
							monsterTile.grid[i+2][j-5] = 'O';
							monsterTile.grid[i+2][j-2] = 'O';
						}
					}
				}
				bigTile = bigTile.rotated();
				monsterTile = monsterTile.rotated();
			}
			monsterTile = monsterTile.flipped();
		}
		int count = 0;
		for (int i = 0; i < bigGrid.length; i++) {
			for (int j = 0; j < bigGrid.length; j++) {
				if (monsterTile.grid[i][j] == '#') count++;
			}
		}
		System.out.println(count);
	}
	
	public static void main(String[] args) throws Exception {
		Day20 day = new Day20();
		day.part1();
		day.part2();
	}
}

class Tile {
	int id;
	char[][] grid;
	
	Tile(int id, char[][] grid) {
		this.id = id;
		this.grid = grid;
	}
	
	public Tile rotated() {
		char[][] newGrid = new char[grid.length][grid.length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				newGrid[i][j] = grid[j][grid.length-1-i];
			}
		}
		return new Tile(id, newGrid);
	}
	
	public Tile flipped() {
		char[][] newGrid = new char[grid.length][grid.length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				newGrid[i][j] = grid[i][grid.length-1-j];
				newGrid[i][grid.length-1-j] = grid[i][j];
			}
		}
		return new Tile(id, newGrid);
	}
	
	public boolean canBeBelow(Tile other) {
		for (int i = 0; i < grid.length; i++) {
			if (grid[grid.length-1][i] != other.grid[0][i]) return false;
		}
		return true;
	}
	
	public boolean canBeRight(Tile other) {
		for (int i = 0; i < grid.length; i++) {
			if (grid[i][grid.length-1] != other.grid[i][0]) return false;
		}
		return true;
	}
	
	public String toString() {
		return Integer.toString(id);
	}
}