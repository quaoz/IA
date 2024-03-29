package com.github.quaoz.util;

import java.util.List;

/**
 * Insertion sort is a simple sorting algorithm, works by virtually splitting the array and placing
 * values from the unsorted part into their places in the sorted part
 *
 * <p>Worst-case performance O(n^2), Best-case performance O(n), Average performance O(n^2)
 */
public class InsertionSort {

	/**
	 * Implements a generic insertion sort algorithm, assumes the whole list should be sorted
	 *
	 * @param list The list to be sorted
	 * @param <E>  The array type
	 *
	 * @return The sorted list
	 */
	public static <E extends Comparable<E>> List<E> sort(List<E> list) {
		return sort(list, 0, list.size() - 1);
	}

	/**
	 * Implements a generic insertion sort algorithm
	 *
	 * @param list  The list to be sorted
	 * @param start The start index
	 * @param end   The end index
	 * @param <E>   The list type
	 *
	 * @return The sorted array
	 */
	static <E extends Comparable<E>> List<E> sort(
		List<E> list,
		int start,
		int end
	) {
		// Iterates through the list
		for (int i = start; i <= end; i++) {
			E insertValue = list.get(i);
			int j = i;

			// Moves elements
			while (j > start && Comparisons.smaller(insertValue, list.get(j - 1))) {
				list.set(j, list.get(--j));
			}

			// Re-adds the insert value
			list.set(j, insertValue);
		}

		return list;
	}
}
