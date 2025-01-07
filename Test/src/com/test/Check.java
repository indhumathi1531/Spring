package com.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Check {
	public static void main(String[] args) {
		String s1 = "Indhu";
		String s2 = "Indhu";
		System.out.println("-----" + s1 == s2);

		Set<Integer> set = new HashSet<>();
		Stream<Integer> stream=Stream.of(1, 2, 3, 4, 3, 4, 5, 62, 2, 3, 2);
		System.out.println(
				stream
				.filter(n -> set.add(n))
				.collect(Collectors.toSet()));
		
		List<Integer> lst=List.of(2,4,5,1,456,6,8,8,76,45,35,23);
		System.out.println(lst
				.stream().distinct().sorted().collect(Collectors.toList())
				
				);
		
		List<String> str=List.of("Indhu","Dhayalan","Vaibhav","Arun");
		System.out.println(str.stream().sorted().collect(Collectors.toList()));
		
		List<String> rev=str.stream().collect(Collectors.toList());
		Collections.reverse(rev);
		rev.forEach(System.out::println);
		
		System.out.println(str.stream().flatMapToInt(String::chars).count()
				);
		
		//Map<Character, Long> characterCount = list.stream()                    // Create a stream from the list of strings .flatMapToInt(String::chars)  // Flatten each string into an IntStream of character codes  .mapToObj(c -> (char) c)      // Convert the int values to Character objects .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
		
		//Map<String,Long> count=str.stream().flatMapToInt(String::chars);
		
		

	}

}
