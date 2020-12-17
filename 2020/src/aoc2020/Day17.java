package aoc2020;

import java.io.*;
import java.util.*;

public class Day17 {
	private Set<Point3D> input1 = new HashSet<>();
	private Set<Point4D> input2 = new HashSet<>();
	
	Day17() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data17.txt"));
		int i = 0;
		while (scan.hasNextLine()) {
			char[] chars = scan.nextLine().toCharArray();
			for (int j = 0; j < chars.length; j++) {
				if (chars[j] == '#') {
					input1.add(new Point3D(j, i, 0));
					input2.add(new Point4D(j, i, 0, 0));
				}
			}
			i++;
		}
	}
	
	private void part1() {
		Set<Point3D> active = new HashSet<>(input1);
		for (int cycle = 0; cycle < 6; cycle++) {
			Map<Point3D, Integer> nbrs = new HashMap<>();
			for (Point3D point: active) {
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						for (int dz = -1; dz <= 1; dz++) {
							if (dz != 0 || dy != 0 || dx != 0) {
								Point3D p = point.added(dx, dy, dz);
								nbrs.put(p, nbrs.getOrDefault(p, 0) + 1);
							}
						}
					}
				}
			}
			Set<Point3D> nextActive = new HashSet<>();
			for (Point3D point: nbrs.keySet()) {
				if (nbrs.get(point) == 3 || (active.contains(point) && nbrs.get(point) == 2)) {
					nextActive.add(point);
				}
			}
			active = nextActive;
		}
		System.out.println(active.size());
	}
	
	private void part2() {
		Set<Point4D> active = new HashSet<>(input2);
		for (int cycle = 0; cycle < 6; cycle++) {
			Map<Point4D, Integer> nbrs = new HashMap<>();
			for (Point4D point: active) {
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						for (int dz = -1; dz <= 1; dz++) {
							for (int dw = -1; dw <= 1; dw++) {
								if (dz != 0 || dy != 0 || dx != 0 || dw != 0) {
									Point4D p = point.added(dx, dy, dz, dw);
									nbrs.put(p, nbrs.getOrDefault(p, 0) + 1);
								}
							}
						}
					}
				}
			}
			Set<Point4D> nextActive = new HashSet<>();
			for (Point4D point: nbrs.keySet()) {
				if (nbrs.get(point) == 3 || (active.contains(point) && nbrs.get(point) == 2)) {
					nextActive.add(point);
				}
			}
			active = nextActive;
		}
		System.out.println(active.size());
	}
	
	public static void main(String[] args) throws Exception {
		Day17 day = new Day17();
		day.part1();
		day.part2();
	}
}