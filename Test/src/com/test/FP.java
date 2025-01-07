package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FP {

	public static void main(String[] args) {
		/*
		 * var num = List.of(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6); num.stream().filter(e
		 * -> e % 2 == 0).forEach(System.out::println); num.stream().filter(n -> n % 2
		 * == 1).forEach(System.out::println); var str = List.of("Spring", "SPringBoot",
		 * "API", "Microservices", "AWS", "PCF", "Azure", "Docker", "Kuberbnetes");
		 * str.stream().forEach(System.out::println); str.stream().filter(course ->
		 * course.contains("Spring")).forEach(System.out::println);
		 * str.stream().filter(course -> course.length() >=
		 * 4).forEach(System.out::println); num.stream().map(e -> e *
		 * e).forEach(System.out::println); num.stream().filter(e -> e % 2 == 1).map(p
		 * -> p * p * p).forEach(System.out::println); str.stream().map(course -> course
		 * + " " + course.length()).forEach(System.out::println);
		 */
		// tr.stream().map(course -> course.charAt()).forEach(System.out::println);
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		System.out.println(list);

		list.stream().filter(e -> e % 2 == 1).map(p -> p * p).forEach(System.out::println);
		try {
			List<Employees> emp = Arrays.asList(new Employees("Alice", 50000), new Employees("Boy", 50000),
					new Employees("Coy", 50000));
			Employees sec = emp.stream().sorted(Comparator.comparing(Employees::getSalary).reversed()).skip(1)
					.findFirst().orElseThrow(() -> new Exception("no"));
			// sec.ifPresent(empl -> System.out.println(empl));
			System.out.println(sec.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
