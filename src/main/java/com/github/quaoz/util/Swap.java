package com.github.quaoz.util;


import java.util.List;
import org.jetbrains.annotations.NotNull;

/** Class to swap elements of arrays */
public class Swap {

	/**
	 * Helper method for swapping places in an array
	 *
	 * @param array The array with elements we want to swap
	 * @param idx The index of the first element
	 * @param idy The index of the second element
	 * @param <T> The value type
	 */
	public static <T extends Comparable<T>> void swap(T @NotNull [] array, int idx, int idy) {
		T swap = array[idx];
		array[idx] = array[idy];
		array[idy] = swap;
	}

	/**
	 * Helper method to swap a block of an array
	 *
	 * @param array The array to perform the swap on
	 * @param blockStart The index of the start of the block to swap
	 * @param blockEnd The index of the end of the block to swap
	 * @param dest The index to start copying to
	 * @param <T> The array type
	 */
	public static <T extends Comparable<T>> void swapBlock(
			T[] array, int blockStart, int blockEnd, int dest) {
		final int blockSize = blockEnd - blockStart;
		int i = 0;

		while (i < blockSize) {
			swap(array, blockStart + i, dest + i);
			i++;
		}
	}

	/**
	 * Helper method for swapping places in a list
	 *
	 * @param list The list with elements we want to swap
	 * @param idx The index of the first element
	 * @param idy The index of the second element
	 * @param <E> The value type
	 */
	public static <E extends Comparable<E>> void swap(@NotNull List<E> list, int idx, int idy) {
		list.set(idy, list.set(idx, list.get(idy)));
	}
}
