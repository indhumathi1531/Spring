package com.test;

import java.util.Arrays;

class A {

}

public class TestClass {

	public static void main(String[] args) {
		String str = "Indhu";
		System.out.println("Test");
		A a = new A();
		A a1 = new A();
		System.out.println(a.hashCode());
		System.out.println(a1.hashCode());
		int[] marks = { 96, 98, 100, 99, 100 };
		System.out.println("t: " + marks);
		System.out.println("t1: " + Arrays.toString(marks));
		for (int mark : marks)
			System.out.println(mark);
		String t = new String("Indhu");
		t.concat("mathi");
		System.out.println(t);
		String s = "indhu";
		s.concat("dhayalan");
		System.out.println(s);

	}

}
