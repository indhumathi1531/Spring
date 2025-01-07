package com.test;

import java.util.HashMap;
import java.util.Map;

public class WordCount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = " Java is a Java is a good good language";
		Map<String, Integer> map = new HashMap<String, Integer>();
		String[] words = str.split(" ");
		for (String word : words) {
			Integer i = map.get(word);
			if (i == null) {
				map.put(word, 1);
			} else {
				map.put(word, i + 1);
			}
		}
		System.out.println(map.keySet() + " : " + map.values());
		System.out.println(map);
	}

}
