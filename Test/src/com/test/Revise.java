package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Revise {

	public static void main(String[] args) {
		String str = "Beautiful";
		String newStr = "";
		System.out.println(str.toUpperCase());
		System.out.println(str.toLowerCase());
		System.out.println(str.length());
		for (int i = str.length() - 1; i > -1; i--) {
			newStr = newStr + str.charAt(i);
		}
		System.out.println(newStr);
		String rev=new StringBuilder(str).reverse().toString();
		System.out.println(rev);
		System.out.println("-------------------------------------------");
		String result=rev.chars().mapToObj(c->(char)c).sorted().map(String::valueOf).collect(Collectors.joining());
		System.out.println(result);
		System.out.println("-------------------------------------------");
		char[] ch = newStr.toCharArray();
		char temp;
		for (int i = 0; i < newStr.length(); i++) {
			for (int j = i + 1; j < newStr.length(); j++) {
				if (ch[i] < ch[j]) {
					temp = ch[i];
					ch[i] = ch[j];
					ch[j] = temp;

				}
			}
		}

		System.out.println(Arrays.toString(ch));
		String strng = new String(ch);
		System.out.println(strng);

		List val = List.of('A', "Indhu", 1, 1.0);
		System.out.println(val.get(2));

		Set<Integer> set = new HashSet<>();
		Set<String> strSet = new LinkedHashSet<>();
		Set<Boolean> bset = new TreeSet<>();

		Map<String, Integer> map = new LinkedHashMap<>();
		String sen = "Indhu got deloitte offer letter with good package , Good Girl";
		String[] words = sen.split(" ");
		for (String word : words) {
			Integer i = map.get(word);
			if (i == null) {
				map.put(word, 1);
			} else {
				map.put(word, i + 1);
			}
		}
		System.out.println(map);

		Map<Character, Integer> charac = new LinkedHashMap<>();
		char[] chi = str.toCharArray();
		for (char c : chi) {
			Integer i = charac.get(c);
			if (i == null) {
				charac.put(c, 1);
			} else {
				charac.put(c, i + 1);
			}
		}
		System.out.println(charac);

		List.of("Indhu", "vaibhav", "Arun", "Dhayalan", "Prem", "Jabeer").stream().map(word -> word.toLowerCase())
				.forEach(System.out::println);

		List.of("Indhu", "vaibhav", "Arun", "Dhayalan", "Prem", "Jabeer").stream()
				.map(name -> name + " " + name.length()).forEach(p -> System.out.println(p));
				

		System.out.println(List.of(23, 12, 24, 53).stream().max((n1, n2) -> Integer.compare(n1, n2)));

		System.out.println(List.of(23, 24, 25, 26).stream().filter(e -> e % 2 == 1).collect(Collectors.toList()));

		System.out.println(IntStream.range(1, 11).map(e -> e * e).boxed().collect(Collectors.toList()));

		System.out.println(List.of(23, 24, 25, 26).stream().filter(e -> e % 2 == 1));

		Predicate<String> isHello = Predicate.isEqual("Hello");
		System.out.println(isHello.test("Hello")); // Output: true
		System.out.println(isHello.test("World"));

		Consumer<String> printConsumer = (s) -> System.out.println(s);
		printConsumer.accept("Hello, Consumer!");

		BiConsumer<String, Integer> printPair = (name, age) -> System.out.println(name + " is " + age + " years old.");

		// Use the BiConsumer to print a name and age pair
		printPair.accept("Alice", 25); // Output: Alice is 25 years old.
		printPair.accept("Bob", 30);

		String s1 = "Indhu";
		String s2 = "Indhu";
		System.out.println(s1 == s2);
		System.out.println(s1.equals(s2));
		String s3 = new String("Indhu");
		String s4 = new String("Indhu");
		System.out.println( s3 == s4);
		System.out.println( s3.equals(s4));
		
		

	}

}
