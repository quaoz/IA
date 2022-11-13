package com.github.quaoz.util;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Class to swap elements
 */
public class Swap {

	/**
	 * Helper method for swapping places in a list
	 *
	 * @param list The list with elements we want to swap
	 * @param idx  The index of the first element
	 * @param idy  The index of the second element
	 * @param <E>  The value type
	 */
	public static <E extends Comparable<E>> void swap(
		@NotNull List<E> list,
		int idx,
		int idy
	) {
		list.set(idy, list.set(idx, list.get(idy)));
	}
}
