package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class New {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		// char ch = sc.next().charAt(0);
		char ch = 'o';
		switch (ch) {
		case 'a', 'e', 'i', 'o', 'u':
		case 'A', 'B', 'E', 'I', 'O', 'U':
			System.out.println("true");
			break;
		default:
			System.out.println("false");
		}

		String str = "Indhu";
		String rev = "";
		for (int i = str.length() - 1; i >= 0; i--) {
			rev = rev + str.charAt(i);
		}
		System.out.println(rev);
		System.out.println(str.length());
		String[] arr = { "Indhu", "vaibhav" };
		String[] st = new String[9];
		System.out.println(arr.length);
		System.out.println(arr);
		System.out.println(Arrays.toString(arr));
		System.out.println("===================");
		System.out.println(st.length);
		System.out.println(st);
		System.out.println(Arrays.toString(st));
		System.out.println("===================");
		ArrayList<String> AL = new ArrayList<String>(List.of("Indhu", "Vaibhav", "10"));
		System.out.println(AL.size());
		System.out.println(AL);

		int[] intarray = { 5, 7, 8, 9, 5, 8, 9 };
		int l = Integer.MIN_VALUE;
		int s = Integer.MIN_VALUE;
		int t = Integer.MIN_VALUE;
		for (int a : intarray) {
			if (a > l) {
				t = s;
				s = l;
				l = a;
			} else if (a > s && a != l) {
				t = s;
				s = a;

			} else if (a > t && a != l && a != s) {
				t = a;
			}
		}
		System.out.println(t);
		System.out.println(s);
		System.out.println(l);

	}

}
