package com.github.quaoz.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Dual-pivot quick sort is a modified quick sort algorithm which uses two pivot values instead of one, works by picking
 * two elements as a pivots and partitioning the array around them
 *
 * <p> Worst-case performance O(n^2), Best-case performance O(n log n), Average performance O(n log n)
 */
public class DualPivotQuickSort {

	/**
	 * Implements a generic dual-pivot quick sort algorithm without having to specify the left and right bounds
	 *
	 * @param array The array to be sorted
	 * @param <T>   The array type
	 *
	 * @return The sorted array
	 */
	public static <T extends Comparable<T>> T[] sort(T[] array) {
		return sort(array, 0, array.length - 1);
	}

	/**
	 * Implements a generic dual-pivot quick sort algorithm
	 *
	 * @param array The array to be sorted
	 * @param left  The left bound of the array
	 * @param right The right bound of the array
	 * @param <T>   The array type
	 *
	 * @return The sorted array
	 */
	static <T extends Comparable<T>> T[] sort(T[] array, int left, int right) {
		if (left < right) {
			final int[] partition = partition(array, left, right);

			sort(array, left, partition[0] - 1);
			sort(array, partition[0] + 1, partition[1] - 1);
			sort(array, partition[1] + 1, right);
		}

		return array;
	}

	/**
	 * Partitions an array around two pivot values
	 *
	 * @param array The array to partition
	 * @param left  The left bound of the array
	 * @param right The right bound of the array
	 * @param <T>   The array type
	 *
	 * @return The partition indexes
	 */
	static <T extends Comparable<T>> int @NotNull [] partition(T @NotNull [] array, int left, int right) {
		/*
		 * Randomizing one of the values which will be chosen as a pivot as we do with standard quick sort can
		 * significantly increase the performance on non-random data sets as it allows dual-pivot quick sort to
		 * function as though the data isn't sorted
		 */

		// QuickSort.randomPivot(array, left, right);

		if (Comparisons.bigger(array[left], array[right])) {
			Swap.swap(array, left, right);
		}

		int index = left + 1;
		int leftIndex = left + 1;
		int rightIndex = right - 1;

		final T pivotOne = array[left];
		final T pivotTwo = array[right];

		while (leftIndex <= rightIndex) {
			if (Comparisons.smaller(array[leftIndex], pivotOne)) {
				Swap.swap(array, leftIndex, index++);
			} else if (Comparisons.biggerOrEqual(array[leftIndex], pivotTwo)) {
				while (Comparisons.bigger(array[rightIndex], pivotTwo) && leftIndex < rightIndex) {
					--rightIndex;
				}

				Swap.swap(array, leftIndex, rightIndex--);

				if (Comparisons.smaller(array[leftIndex], pivotOne)) {
					Swap.swap(array, leftIndex, index++);
				}
			}
			leftIndex++;
		}

		Swap.swap(array, left, --index);
		Swap.swap(array, right, ++rightIndex);

		return new int[]{index, rightIndex};
	}

	/**
	 * Implements a generic dual-pivot quick sort algorithm without having to specify the left and right bounds
	 *
	 * @param list The list to be sorted
	 * @param <E>  The list type
	 *
	 * @return The sorted list
	 */
	public static <E extends Comparable<E>> List<E> sort(List<E> list) {
		return sort(list, 0, list.size() - 1);
	}

	/**
	 * Implements a generic dual-pivot quick sort algorithm
	 *
	 * @param list  The list to be sorted
	 * @param left  The left bound of the list
	 * @param right The right bound of the list
	 * @param <E>   The list type
	 *
	 * @return The sorted list
	 */
	static <E extends Comparable<E>> List<E> sort(@NotNull List<E> list, int left, int right) {
		if (left < right) {
			final int[] partition = partition(list, left, right);

			sort(list, left, partition[0] - 1);
			sort(list, partition[0] + 1, partition[1] - 1);
			sort(list, partition[1] + 1, right);
		}

		return list;
	}

	/**
	 * Partitions a list around two pivot values
	 *
	 * @param list  The list to partition
	 * @param left  The left bound of the list
	 * @param right The right bound of the list
	 * @param <E>   The list type
	 *
	 * @return The partition indexes
	 */
	static <E extends Comparable<E>> int @NotNull [] partition(@NotNull List<E> list, int left, int right) {
		if (Comparisons.bigger(list.get(left), list.get(right))) {
			Swap.swap(list, left, right);
		}

		int index = left + 1;
		int leftIndex = left + 1;
		int rightIndex = right - 1;

		final E pivotOne = list.get(left);
		final E pivotTwo = list.get(right);

		while (leftIndex <= rightIndex) {
			if (Comparisons.smaller(list.get(leftIndex), pivotOne)) {
				Swap.swap(list, leftIndex, index++);
			} else if (Comparisons.biggerOrEqual(list.get(leftIndex), pivotTwo)) {
				while (Comparisons.bigger(list.get(rightIndex), pivotTwo) && leftIndex < rightIndex) {
					--rightIndex;
				}

				Swap.swap(list, leftIndex, rightIndex--);

				if (Comparisons.smaller(list.get(leftIndex), pivotOne)) {
					Swap.swap(list, leftIndex, index++);
				}
			}
			leftIndex++;
		}

		Swap.swap(list, left, --index);
		Swap.swap(list, right, ++rightIndex);

		return new int[]{index, rightIndex};
	}
}

