package aoc2020;

import java.io.*;
import java.util.*;


public class Day24 {
	
	ArrayList<ArrayList<String>> instructions = new ArrayList<>();
	HashMap<Point2D, Boolean> tiles = new HashMap<>();
	
	Day24() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data24.txt"));
		while (scan.hasNextLine()) {
			ArrayList<String> instruction = new ArrayList<>();
			String line = scan.nextLine();
			
			while (!line.isEmpty()) {
				if (line.charAt(0) == 'e') {
					instruction.add("e");
					line = line.substring(1);
				} else if (line.charAt(0) == 'w') {
					instruction.add("w");
					line = line.substring(1);
				} else if (line.substring(0, 2).equals("se")) {
					instruction.add("se");
					line = line.substring(2);
				} else if (line.substring(0, 2).equals("sw")) {
					instruction.add("sw");
					line = line.substring(2);
				} else if (line.substring(0, 2).equals("ne")) {
					instruction.add("ne");
					line = line.substring(2);
				} else if (line.substring(0, 2).equals("nw")) {
					instruction.add("nw");
					line = line.substring(2);
				} else {
					throw new FileNotFoundException("Å NEJ!");
				}
			}
			
			instructions.add(instruction);
		}
	}
	
	private void part1() {
		for (ArrayList<String> instruction : instructions) {
			Point2D point = new Point2D(0,0);
			for (String dir : instruction) {
				switch (dir) {
				case "e":
					point.add(2, 0);
					break;
				case "w":
					point.add(-2, 0);
					break;
				case "se":
					point.add(1, -1);
					break;
				case "sw":
					point.add(-1, -1);
					break;
				case "ne":
					point.add(1, 1);
					break;
				case "nw":
					point.add(-1, 1);
					break;
				}
			}
			if (!tiles.containsKey(point)) {
				tiles.put(point, true);
			} else {
				tiles.put(point, !tiles.get(point));
			}
		}
		int blacks = 0;
		for (boolean black : tiles.values()) {
			if (black) blacks++;
		}
		System.out.println(blacks);
	}
	
	private void part2() {
		// Add tiles adjacent to a black tile as whites
		HashMap<Point2D, Boolean> newTiles = new HashMap<>();
		for (Point2D tile : tiles.keySet()) {
			if (tiles.get(tile)) {
				newTiles.put(tile, true);
				List<Point2D> neighbours = List.of(tile.added(-2, 0), tile.added(2, 0), tile.added(-1, 1), tile.added(-1, -1), tile.added(1, 1), tile.added(1, -1));
				for (Point2D neighbour : neighbours) {
					if (!newTiles.containsKey(neighbour)) {
						newTiles.put(neighbour, false);
					}
				}
			}
		}
		tiles = newTiles;
		for (int i = 1; i < 101; i++) {
			newTiles = new HashMap<>();
			for (Point2D tile : tiles.keySet()) {
				// Get black neighbours
				int blackNeighbours = 0;
				List<Point2D> neighbours = List.of(tile.added(-2, 0), tile.added(2, 0), tile.added(-1, 1), tile.added(-1, -1), tile.added(1, 1), tile.added(1, -1));
				for (Point2D neighbour : neighbours) {
					if (tiles.containsKey(neighbour) && tiles.get(neighbour)) {
						blackNeighbours++;
					}
				}
				
				if ((tiles.get(tile) && (blackNeighbours == 1 || blackNeighbours == 2)) || (!tiles.get(tile) && blackNeighbours == 2)) {
					newTiles.put(tile, true);
					// Add neighbours not in newTiles as whites
					for (Point2D neighbour : neighbours) {
						if (!newTiles.containsKey(neighbour)) {
							newTiles.put(neighbour, false);
						}
					}
				}
			}
			tiles = newTiles;
		}
		
		int blacks = 0;
		for (boolean black : tiles.values()) {
			if (black) blacks++;
		}
		System.out.println(blacks);
	}
	
	public static void main(String[] args) throws Exception {
		Day24 day = new Day24();
		day.part1();
		day.part2();
	}
}
