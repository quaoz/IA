package com.github.quaoz.database;

/**
 * Used to calculate the similarity between two strings instead of using {@link me.xdrop.fuzzywuzzy.FuzzySearch#weightedRatio(String, String)}
 */
public interface CustomRatio {
	/**
	 * @param s1 Input string
	 * @param s2 Input string
	 *
	 * @return A double containing the ratio of similarity between the strings, may be negative
	 */
	double ratio(String s1, String s2);
}
