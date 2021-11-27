package aoc2020;

import java.io.*;
import java.util.*;


public class Day25 {
	long cardPublicKey = 8458505L;
	long doorPublicKey = 16050997L;
	
	private static int getLoopSize(long value, long subjectNbr) {
		long num = 1;
		int loopSize = 0;
		while (num != value) {
			num *= subjectNbr;
			num = Long.remainderUnsigned(num, 20201227L);
			loopSize++;
		}
		return loopSize;
	}
	
	private void part1() {
		int cardLoopSize = getLoopSize(cardPublicKey, 7L);
		long encryptKey = 1;
		for (int i = 0; i < cardLoopSize; i++) {
			encryptKey *= doorPublicKey;
			encryptKey = Long.remainderUnsigned(encryptKey, 20201227L);
		}
		System.out.println(encryptKey);
	}
	
	private void part2() {
		
	}
	
	public static void main(String[] args) throws Exception {
		Day25 day = new Day25();
		day.part1();
		day.part2();
	}
}
