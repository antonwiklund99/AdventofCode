package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 {
	private List<String> input = new ArrayList<>();
	StreamTokenizer tokenizer;
	int token = 0;
	
	Day18() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data18.txt"));
		while (scan.hasNextLine()) {
			input.add(scan.nextLine());
		}
	}
	
	private long evalExpr1() throws IOException {
		int token = tokenizer.nextToken();
		long value = 1;
		Op op = (x,y) -> y;
		while (token != StreamTokenizer.TT_EOL && token != StreamTokenizer.TT_EOF) {
			switch (token) {
				case StreamTokenizer.TT_NUMBER:
					value = op.apply(value, (long) tokenizer.nval);
					break;
				case '+':
					op = (x,y) -> x + y;
					break;
				case '*':
					op = (x,y) -> x * y;
					break;
				case '(':
					value = op.apply(value, evalExpr1());
					break;
				case ')':
					return value;
			}
			token = tokenizer.nextToken();
		}
		return value;
	}
	
	private long evalExpr2() throws IOException {
		token = tokenizer.nextToken();
		long value = 0;
		long n = 0;
		while (token != StreamTokenizer.TT_EOL && token != StreamTokenizer.TT_EOF) {
			switch (token) {
				case StreamTokenizer.TT_NUMBER:
					n = (long) tokenizer.nval;
					token = tokenizer.nextToken();
					break;
				case '+':
					long v = evalAdd(n);
					value = v;
					break;
				case '*':
					if (value == 0) {
						value = n;
					}
					n = evalAdd(0);
					value *= n;
					break;
				case '(':
					n = evalExpr2();
					value = value == 0 ? n : value*n;
					break;
				case ')':
					token = tokenizer.nextToken();
					return value;
				default:
					throw new IOException("WHAAAAAAAAAAAAT");
			}
		}
		return value;
	}
	
	private long evalAdd(long value) throws IOException {
		token = tokenizer.nextToken();
		while (token == StreamTokenizer.TT_NUMBER || token == '+' || token == '(') {
			if (token == StreamTokenizer.TT_NUMBER) {
				value += (long) tokenizer.nval;
				token = tokenizer.nextToken();
			} else if (token == '(') {
				value += evalExpr2();
			} else {
				token = tokenizer.nextToken();
			}
		}
		return value;
	}
	
	private void part1() throws IOException {
		long sum = 0;
		for (String expr: input) {
			tokenizer = new StreamTokenizer(new StringReader(expr));
			sum += evalExpr1();
		}
		System.out.println(sum);
	}
	
	private void part2() throws IOException {
		long sum = 0;
		for (String expr: input) {
			tokenizer = new StreamTokenizer(new StringReader(expr));
			sum += evalExpr2();
		}
		System.out.println(sum);
	}
	
	public static void main(String[] args) throws Exception {
		Day18 day = new Day18();
		day.part1();
		day.part2();
	}
}
	
interface Op {
	long apply(long x, long y);
}