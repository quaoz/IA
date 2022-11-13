package com.github.quaoz.tests;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class Test {

	public static void main(String[] args) {
		String s1 = "bog bog";
		String s2 = "bog bog";

		System.out.println(FuzzySearch.weightedRatio(s1, s2));
	}
}
