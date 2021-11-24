package aoc2020;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day21 {
	Map<String,Integer> ingredientToInt = new HashMap<>();
	Map<Integer,String> intToIngredient = new HashMap<>();
	Map<String,Integer> allergenToInt = new HashMap<>();
	int nextIngredient = 0;
	int nextAllergen = 0;
	ArrayList<Set<Integer>> ingredientList = new ArrayList<>();
	ArrayList<Set<Integer>> allergenList = new ArrayList<>();
	Set<Integer>[] possibleAllergens;
	
	Day21() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("data/data21.txt"));
		Pattern foodListPattern = Pattern.compile("((?:[a-z]+ ?)+) \\(contains ((?:[a-z]+,? ?)+)");
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			Matcher m = foodListPattern.matcher(line);
			m.find();
			String[] ingredientStrs = m.group(1).split(" ");
			String[] allergenStrs = m.group(2).split(", ");
			Set<Integer> ingredients = new HashSet<>();
			Set<Integer> allergens = new HashSet<>();
			for (String ingredient : ingredientStrs) {
				if (!ingredientToInt.containsKey(ingredient)) {
					ingredientToInt.put(ingredient, nextIngredient);
					intToIngredient.put(nextIngredient, ingredient);
					nextIngredient++;
				}
				ingredients.add(ingredientToInt.get(ingredient));
			}
			for (String allergen : allergenStrs) {
				if (!allergenToInt.containsKey(allergen)) {
					allergenToInt.put(allergen, nextAllergen);
					nextAllergen++;
				}
				allergens.add(allergenToInt.get(allergen));
			}
			
			this.allergenList.add(allergens);
			this.ingredientList.add(ingredients);
		}
		

		possibleAllergens = new HashSet[nextIngredient];
		for (int i = 0; i < possibleAllergens.length; i++) {
			possibleAllergens[i] = new HashSet<Integer>();
		}
		for (int i = 0; i < ingredientList.size(); i++) {
			for (int ingr : ingredientList.get(i)) {
				possibleAllergens[ingr].addAll(allergenList.get(i));
			}
		}
	}
	private void part1() {
		boolean[] canBeAllergen = new boolean[nextIngredient];
		for (int ingredient : ingredientToInt.values()) {
			if (!canBeAllergen[ingredient]) {
				for (int possibleAllergen : possibleAllergens[ingredient]) {
					Set<Integer>[] possibleIngredientAllergen = new Set[nextAllergen];
					for (int i = 0; i < nextAllergen; i++) {
						possibleIngredientAllergen[i] = new HashSet<>();
					}
					boolean failed = false;
					possibleIngredientAllergen[possibleAllergen].add(ingredient);
					for (int i = 0; i < ingredientList.size(); i++) {
						Set<Integer> ingredients = ingredientList.get(i);
						Set<Integer> allergens = allergenList.get(i);
						
						for (int allergen : allergens) {
							if (possibleIngredientAllergen[allergen].isEmpty()) {
								possibleIngredientAllergen[allergen].addAll(ingredients);
							} else {
								possibleIngredientAllergen[allergen].removeIf((ingr) -> !ingredients.contains(ingr));
								if (possibleIngredientAllergen[allergen].isEmpty()) {
									failed = true;
									break;
								}
							}
						}
						
						if (failed) {
							break;
						}
					}
					if (!failed) {
						for (Set<Integer> ingrs : possibleIngredientAllergen) {
							for (int ingr : ingrs) {
								canBeAllergen[ingr] = true;
							}
						}
					}
				}
			}
		}
		
		int sum = 0;
		for (int i = 0; i < canBeAllergen.length; i++) {
			if (!canBeAllergen[i]) {
				for (Set<Integer> ingrs : ingredientList) {
					if (ingrs.contains(i)) sum++;
				}
			}
		}
		System.out.println(sum);
	}
	
	private void part2() {
		for (int ingredient : ingredientToInt.values()) {
			for (int possibleAllergen : possibleAllergens[ingredient]) {
				Set<Integer>[] possibleIngredientAllergen = new Set[nextAllergen];
				for (int i = 0; i < nextAllergen; i++) {
					possibleIngredientAllergen[i] = new HashSet<>();
				}
				boolean failed = false;
				possibleIngredientAllergen[possibleAllergen].add(ingredient);
				for (int i = 0; i < ingredientList.size(); i++) {
					Set<Integer> ingredients = ingredientList.get(i);
					Set<Integer> allergens = allergenList.get(i);
					
					for (int allergen : allergens) {
						if (possibleIngredientAllergen[allergen].isEmpty()) {
							possibleIngredientAllergen[allergen].addAll(ingredients);
						} else {
							possibleIngredientAllergen[allergen].removeIf((ingr) -> !ingredients.contains(ingr));
							if (possibleIngredientAllergen[allergen].isEmpty()) {
								failed = true;
								break;
							}
						}
					}
					
					if (failed) {
						break;
					}
				}
				if (!failed) {
					boolean done = false;
					Set<Integer> alreadyPicked = new HashSet<>();
					for (Set<Integer> ingrs : possibleIngredientAllergen) {
						if (ingrs.size() == 1) alreadyPicked.addAll(ingrs);
					}
					while (!done) {
						done = true;
						for (Set<Integer> ingrs : possibleIngredientAllergen) {
							if (ingrs.size() > 1) {
								ingrs.removeIf((ingr) -> alreadyPicked.contains(ingr));
								done = false;
								if (ingrs.size() == 1) alreadyPicked.addAll(ingrs);
							}
						}
					}
					List<String> sortedAllergens = new ArrayList<>(allergenToInt.keySet());
					sortedAllergens.sort((s1,s2) -> s1.compareTo(s2));
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < sortedAllergens.size(); i++) {
						sb.append(intToIngredient.get(new ArrayList<>(possibleIngredientAllergen[allergenToInt.get(sortedAllergens.get(i))]).get(0)));
						if (i != sortedAllergens.size()-1) sb.append(',');
					}
					System.out.println(sb.toString());
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Day21 day = new Day21();
		day.part1();
		day.part2();
	}
}
