package com.test;

public class SecondLargest {
	public static void main(String[] args) {
		// int[] arr = new int[5];
		int[] arr = { 2, 3, 5, 6, 7, 8, 35 };
		int sec = Integer.MIN_VALUE;
		int fst = Integer.MIN_VALUE;
		for (int a : arr) {
			if (a > fst) {
				sec = fst;
				fst = a;
			} else if (a > sec && a != fst) {
				sec = a;
			}

		}
		if (sec == Integer.MIN_VALUE) {
			System.out.println(-1);
		}
		System.out.println(sec);

	}

}
