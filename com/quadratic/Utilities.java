package com.quadratic;

public class Utilities {

	public static String repeatString(String s, int times) {
		String result = "";
		for (int i = 0; i < times; i++) {
			result += s;
		}
		return result;
	}
	
	public static String stringFormat(double num) {
		if (num % 1 == 0 && inBounds(num)) {
			return String.valueOf((int) num);
		} else {
			return String.valueOf(num);
		}
	}
	
	public static boolean inBounds(double num) {
		return num >= Integer.MIN_VALUE && num <= Integer.MAX_VALUE;
	}
	
}