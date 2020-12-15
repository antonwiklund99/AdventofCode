package aoc2020;

import java.io.*;
import java.util.*;

public class Day12 {
	private List<String> input = new ArrayList<>();
	
	Day12() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data12.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private void part1() {	
		int dir = 0;
		int x = 0;
		int y = 0;
		for (String line: input) {
			char action = line.charAt(0);
			int value = Integer.parseInt(line.substring(1));
			switch (action) {
			case 'N': y += value;
					  break;
			case 'S': y -= value;
					  break;
			case 'E': x += value;
					  break;
			case 'W': x -= value;
					  break;
			case 'L': dir += value;
					  dir += Math.ceil(-dir/360)*360;
					  break;
			case 'R': dir -= value;
					  break;
			case 'F': x += value*Math.cos(Math.toRadians(dir));
					  y += value*Math.sin(Math.toRadians(dir));
					  break;
			default: System.out.println("UNKNOWN ACTION " + action);
			}
		}
		System.out.println(Math.abs(x) + Math.abs(y));
	}
	
	private void part2() {
		Point2D waypoint = new Point2D(10,1);
		Point2D ship = new Point2D(0,0);
		for (String line: input) {
			char action = line.charAt(0);
			int value = Integer.parseInt(line.substring(1));
			switch (action) {
			case 'N': waypoint.add(0, value);
					  break;
			case 'S': waypoint.add(0, -value);
					  break;
			case 'E': waypoint.add(value, 0);
					  break;
			case 'W': waypoint.add(-value, 0);
					  break;
			case 'L': waypoint.rotate(value);
					  break;
			case 'R': waypoint.rotate(-value);
					  break;
			case 'F': ship.add(value*waypoint.x, value*waypoint.y);
					  break;
			default: System.out.println("UNKNOWN ACTION " + action);
			}
		}
		System.out.println(Math.abs(ship.x) + Math.abs(ship.y));
	}
	
	public static void main(String[] args) throws Exception {
		Day12 day = new Day12();
		day.part1();
		day.part2();
	}
}