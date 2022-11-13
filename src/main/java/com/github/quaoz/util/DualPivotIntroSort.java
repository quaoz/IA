package com.github.quaoz.util;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Dual-pivot intro sort is a modified intro sort which uses dual-pivot quick sort instead of
 * standard quick sort as it is faster for almost all data sets
 *
 * <p>Worst-case performance O(n log n), Best-case performance O(n log n), Average performance O(n
 * log n)
 */
public class DualPivotIntroSort {

	/**
	 * Implements a modified intro sort algorithm without the need to specify the bounds
	 *
	 * @param list The list to be sorted
	 * @param <E>  The list type
	 *
	 * @return The sorted list
	 */
	public static <E extends Comparable<E>> List<E> sort(@NotNull List<E> list) {
		final int maxDepth = (int) (
			2 * Math.floor(Math.log(list.size()) / Math.log(2))
		);
		return sort(list, 0, list.size() - 1, maxDepth);
	}

	/**
	 * Implements a modified intro sort algorithm
	 *
	 * @param list     The list to be sorted
	 * @param maxDepth The number of times the function can recursively call itself before switching
	 *                 to heapsort
	 * @param left     The left bound of the list
	 * @param right    The right bound of the list
	 * @param <E>      The list type
	 *
	 * @return The sorted list
	 */
	static <E extends Comparable<E>> List<E> sort(
		List<E> list,
		int left,
		int right,
		int maxDepth
	) {
		final int size = right - left;

		if (size > 16) {
			if (maxDepth != 0) {
				final int[] partition = DualPivotQuickSort.partition(list, left, right);

				sort(list, left, partition[0] - 1, maxDepth - 1);
				sort(list, partition[0] + 1, partition[1] - 1, maxDepth - 1);
				sort(list, partition[1] + 1, right, maxDepth - 1);
			} else {
				HeapSort.sort(list, left, right);
			}
		} else {
			InsertionSort.sort(list, left, right);
		}

		return list;
	}
}
