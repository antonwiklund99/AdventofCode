package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
	private List<String> input = new ArrayList<>();
	
	Day14() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data14.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private final long debruijn64 = 0x03f79d71b4cb0a89L;
	private final int[] index64 = { 0,  1,  48, 2,  57, 49, 28, 3,  61, 58, 50, 42, 38,
	                                29, 17, 4,  62, 55, 59, 36, 53, 51, 43, 22, 45, 39,
	                                33, 30, 24, 18, 12, 5,  63, 47, 56, 27, 60, 41, 37,
	                                16, 54, 35, 52, 21, 44, 32, 23, 11, 46, 26, 40, 15,
	                                34, 20, 31, 10, 25, 14, 19, 9,  13, 8,  7,  6 };
	private int bitScanForward(long b) {
		int idx = (int)(((b & -b) * debruijn64) >>> 58);
		return index64[idx];
	}
	
	private void allCombs(long floatingBits, long mask, List<Long> res) {
		if (floatingBits == 0) {
			res.add(mask);
		} else {
			int i = bitScanForward(floatingBits);
			floatingBits &= floatingBits - 1;
			allCombs(floatingBits, mask | (1L << i), res);
			allCombs(floatingBits, mask & ~(1L << i), res);
		}
	}
	
	private void part1() {	
		long bitsToMask = 0;
		long mask = 0;
		Map<Long, Long> mem = new HashMap<>();
		Pattern maskPattern = Pattern.compile("mask = ([10X]{36})");
		Pattern memPattern = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
		for (String line: input) {
			Matcher mp = maskPattern.matcher(line);
			Matcher mm = memPattern.matcher(line);
			if (mp.find()) {
				char[] bits = mp.group(1).toCharArray();
				bitsToMask = 0;
				mask = 0;
				for (int i = bits.length - 1; i >= 0; i--) {
					if (bits[i] != 'X') {
						long shift = bits.length - 1 - i;
						bitsToMask |= 1L << shift;
						mask += (bits[i] == '0' ? 0L : 1L) << shift;
					}
				}
			} else if (mm.find()) {
				long index = Long.parseLong(mm.group(1));
				long value = Long.parseLong(mm.group(2));
				mem.put(index, (value & ~bitsToMask) + mask);
			} else {
				System.out.println("UNKNOWN INPUT: " + line);
			}
		}
		System.out.println(mem.values().stream().reduce(0L, (x,y) -> x + y));
	}
	
	private void part2() {
		long floatingBits = 0;
		long mask = 0;
		Map<Long, Long> mem = new HashMap<>();
		Pattern maskPattern = Pattern.compile("mask = ([10X]{36})");
		Pattern memPattern = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
		for (String line: input) {
			Matcher mp = maskPattern.matcher(line);
			Matcher mm = memPattern.matcher(line);
			if (mp.find()) {
				char[] bits = mp.group(1).toCharArray();
				floatingBits = 0;
				mask = 0;
				for (int i = bits.length - 1; i >= 0; i--) {
					long shift = bits.length - 1 - i;
					if (bits[i] != 'X') {
						mask += (bits[i] == '0' ? 0L : 1L) << shift;
					} else {
						floatingBits |= 1L << shift;
					}
				}
			} else if (mm.find()) {
				int index = Integer.parseInt(mm.group(1));
				long value = Long.parseLong(mm.group(2));
				List<Long> adresses = new ArrayList<>();
				allCombs(floatingBits, (index | mask) & ~floatingBits, adresses);
				for (long a: adresses) {
					mem.put(a, value);
				}
			} else {
				System.out.println("UNKNOWN INPUT: " + line);
			}
		}
		System.out.println(mem.values().stream().reduce(0L, (x,y) -> x + y));
	}

	public static void main(String[] args) throws Exception {
		Day14 day = new Day14();
		day.part1();
		day.part2();
	}
}