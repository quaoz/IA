package com.github.quaoz.util;

import java.util.List;

/**
 * Heap sort is a fast sorting algorithm, works by dividing the array into a sorted and unsorted
 * region and shrinking the unsorted region by moving the largest element into the sorted region
 *
 * <p>Worst-case performance O(n log n), Best-case performance O(n log n), Average performance O(n
 * log n)
 */
public class HeapSort {

	/**
	 * Implements a generic heap sort algorithm
	 *
	 * @param list  The list to be sorted
	 * @param start The start index
	 * @param end   The end index
	 * @param <E>   The list type
	 *
	 * @return The sorted list
	 */
	static <E extends Comparable<E>> List<E> sort(
		List<E> list,
		int start,
		int end
	) {
		int size = end - start;

		// Build heap
		for (int i = size / 2 - 1; i >= 0; i--) {
			heapify(list, size, i);
		}

		// Extract an element from the heap
		while (size > 0) {
			// Move the current root to the end
			Swap.swap(list, start, --size + start);

			// Heapify the reduced heap
			heapify(list, size, start);
		}

		return list;
	}

	/**
	 * Heapifies the subtree
	 *
	 * @param list The list to be heapified
	 * @param size The size of the subtree
	 * @param root The root of the subtree
	 * @param <E>  The list type
	 */
	static <E extends Comparable<E>> void heapify(
		List<E> list,
		int size,
		int root
	) {
		int max = root;
		final int left = 2 * root + 1;
		final int right = 2 * root + 2;

		// If left is bigger than max replace max with it
		if (left < size && Comparisons.bigger(list.get(left), list.get(max))) {
			max = left;
		}

		// If right is bigger than max replace max with it
		if (right < size && Comparisons.bigger(list.get(right), list.get(max))) {
			max = right;
		}

		// Swaps the max and the root if they aren't equal
		if (max != root) {
			Swap.swap(list, max, root);

			// Recursively heapify the affected subtree
			heapify(list, size, max);
		}
	}
}
