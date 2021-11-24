package aoc2020;

import java.io.*;
import java.util.*;

public class Day22 {
	LinkedList<Integer> player1Cards = new LinkedList<>();
	LinkedList<Integer> player2Cards = new LinkedList<>();
	
	Day22() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data22.txt"));
		int player = 1;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			LinkedList<Integer> cards = player == 1 ? player1Cards : player2Cards;
			line = scan.nextLine();
			while (!line.isBlank()) {
				cards.add(Integer.parseInt(line));
				if (!scan.hasNextLine()) break;
				line = scan.nextLine();
			}
			player++;
		}
	}
	
	private static int len(int i) {
		// Largest nbr is 45
		if (i >= 10) return 2;
		return 1;
	}
	
	private int playRec(List<Integer> p1, List<Integer> p2, boolean clone) {
		LinkedList<Integer> player1;
		LinkedList<Integer> player2;
		if (clone) {
			player1 = new LinkedList<Integer>(p1);
			player2 = new LinkedList<Integer>(p2);
		} else {
			player1 = (LinkedList<Integer>) p1;
			player2 = (LinkedList<Integer>) p2;
		}
		
		Set<String> seenStates = new HashSet<>();
		StringBuilder sbPlayer1 = new StringBuilder();
		StringBuilder sbPlayer2 = new StringBuilder();
		for (int i : player1) sbPlayer1.append(i);
		for (int i : player2) sbPlayer2.append(i);
		String state = sbPlayer1.toString() + " " + sbPlayer2.toString();
		seenStates.add(state);
		
		while (!player1.isEmpty() && !player2.isEmpty()) {
			int c1 = player1.removeFirst(), c2 = player2.removeFirst();
			sbPlayer1.delete(0, len(c1));
			sbPlayer2.delete(0, len(c2));
			if (player1.size() >= c1 && player2.size() >= c2) {
				int won = playRec(player1.subList(0, c1), player2.subList(0, c2), true);
				if (won == 1) {
					player1.add(c1);
					player1.add(c2);
					sbPlayer1.append(c1);
					sbPlayer1.append(c2);
				} else {
					player2.add(c2);
					player2.add(c1);
					sbPlayer2.append(c1);
					sbPlayer2.append(c2);
				}
			} else if (c1 == c2) {
				player1.add(c1);
				player2.add(c2);
				sbPlayer1.append(c1);
				sbPlayer2.append(c2);
			} else if (c1 < c2) {
				player2.add(c2);
				player2.add(c1);
				sbPlayer2.append(c1);
				sbPlayer2.append(c2);
			} else {
				player1.add(c1);
				player1.add(c2);
				sbPlayer1.append(c1);
				sbPlayer1.append(c2);
			}
			
			state = sbPlayer1.toString() + " " + sbPlayer2.toString();
			if (seenStates.contains(state)) {
				player1.addAll(player2);
				player2 = new LinkedList<>();
			} else {
				seenStates.add(state);
			}
		}
		return player2.isEmpty() ? 1 : 2;
	}
	
	private void part1() {
		LinkedList<Integer> player1 = (LinkedList<Integer>) player1Cards.clone();
		LinkedList<Integer> player2 = (LinkedList<Integer>) player2Cards.clone();
		while (!player1.isEmpty() && !player2.isEmpty()) {
			int c1 = player1.removeFirst(), c2 = player2.removeFirst();
			if (c1 == c2) {
				player1.add(c1);
				player2.add(c2);
			} else if (c1 < c2) {
				player2.add(c2);
				player2.add(c1);
			} else {
				player1.add(c1);
				player1.add(c2);
			}
		}
		long res = 0;
		LinkedList<Integer> winning = player1.isEmpty() ? player2 : player1;
		int len = winning.size();
		for (int i = 1; i <= len; i++) {
			res += i*winning.removeLast();
		}
		System.out.println(res);
	}
	
	private void part2() {
		int winner = playRec(player1Cards, player2Cards, false);
		long res = 0;
		LinkedList<Integer> winning = winner == 1 ? player1Cards : player2Cards;
		int len = winning.size();
		for (int i = 1; i <= len; i++) {
			res += i*winning.removeLast();
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) throws Exception {
		Day22 day = new Day22();
		day.part1();
		day.part2();
	}
}
