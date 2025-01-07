package com.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CharArray {

	public static void main(String[] args) {
		String name = "Indhu";
		String newName = "";
		for (int i = name.length() - 1; i > -1; i--) {
			newName = newName + name.charAt(i);
		}
		System.out.println(newName);

		char[] chArray = newName.toCharArray();
		char temp;
		for (int i = 0; i < newName.length() - 1; i++) {
			for (int j = i + 1; j < newName.length(); j++) {
				if (chArray[i] > chArray[j]) {
					temp = chArray[i];
					chArray[i] = chArray[j];
					chArray[j] = temp;
				}
			}
		}

		System.out.println(Arrays.toString(chArray));

		Map<String, Integer> countMap = new TreeMap<>(Map.of("A", 1, "B", 2, "C", 3, "D", 4));
		System.out.println(countMap);

		String sen = "I got accenture offer, congrtas I! Indhumathi Dhayalan";
		Map<String, Integer> charCount = new TreeMap<>(Map.of("Indhu", 1, "Vaibhav", 2, "Dhayalan", 3));
		String[] words = sen.split(" ");
		for (String word : words) {
			Integer i = charCount.get(word);
			if (i == null) {
				charCount.put(word, 1);
			} else {
				charCount.put(word, i + 1);
			}

		}
		System.out.println(charCount);

		char[] character = sen.toCharArray();
		Map<Character, Integer> ch = new TreeMap<>();
		for (Character c : character) {
			Integer j = ch.get(c);
			if (j == null) {
				ch.put(c, 1);
			} else {
				ch.put(c, j + 1);
			}
		}
		System.out.println(ch);

		System.out.println(List.of(2, 3, 4, 5, 6, 7, 3, 456, 61, 2, 7, 9, 4, 5, 6).stream().distinct().sorted()
				.filter(i -> i % 2 != 0).map(j -> j + 1).reduce(0, (n1, n2) -> n1 + n2));// 3,5,7,61,9---4+6+8+62+10

		List.of(2, 3, 4, 5, 6, 7, 3, 456, 61, 2, 7, 9, 4, 5, 6).stream().distinct().sorted().filter(i -> i % 2 != 0)
				.forEach(e -> System.out.println(e));

	}
}
