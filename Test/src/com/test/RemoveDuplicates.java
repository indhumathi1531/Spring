package com.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicates {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = { 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5 };

		Set<Integer> set = new HashSet<>();
		for (int a : arr) {
			set.add(a);
		}
		System.out.println(set);
		int newArr[] = new int[set.size()];
		int i = 0;
		for (int n : set) {
			newArr[i++] = n;
		}
		System.out.println(Arrays.toString(newArr));
		System.out.println(0.1 + 0.2 == 0.3);
		System.out.println(0.1 + 0.2);

	}

}
