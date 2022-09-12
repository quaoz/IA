package com.github.quaoz.util;

import org.jetbrains.annotations.NotNull;

/** Class to simplify comparisons between two comparable objects */
public class Comparisons {

  /**
   * Method to see if the base is smaller than value
   *
   * @param base Base value to be compared to
   * @param value Value to compare
   * @param <T> The value type
   * @return boolean Whether base is smalled than the value
   */
  public static <T extends Comparable<T>> boolean smaller(@NotNull T base, T value) {
    return base.compareTo(value) < 0;
  }

  /**
   * Method to see if the base is smaller or equal to the value
   *
   * @param base Base value to be compared to
   * @param value Value to compare
   * @param <T> The value type
   * @return boolean Whether base is smalled or equal to the value
   */
  public static <T extends Comparable<T>> boolean smallerOrEqual(@NotNull T base, T value) {
    return base.compareTo(value) <= 0;
  }

  /**
   * Method to see if the base is bigger than the value
   *
   * @param base Base value to be compared to
   * @param value Value to compare
   * @param <T> The value type
   * @return boolean Whether base is smalled than the value
   */
  public static <T extends Comparable<T>> boolean bigger(@NotNull T base, T value) {
    return base.compareTo(value) > 0;
  }

  /**
   * Method to see if the base is bigger or equal to the value
   *
   * @param base Base value to be compared to
   * @param value Value to compare
   * @param <T> The value type
   * @return boolean Whether base is bigger or equal to the value
   */
  public static <T extends Comparable<T>> boolean biggerOrEqual(@NotNull T base, T value) {
    return base.compareTo(value) >= 0;
  }

  /**
   * Method to see if the base is equal to the value
   *
   * @param base Base value to be compared to
   * @param value Value to compare
   * @param <T> The value type
   * @return boolean Whether the base is smalled than the value
   */
  public static <T extends Comparable<T>> boolean equal(@NotNull T base, T value) {
    return base.compareTo(value) == 0;
  }

  /**
   * Method to see if the base is not equal to the value
   *
   * @param base Base value to be compared to
   * @param value Value to compare
   * @param <T> The value type
   * @return boolean Whether the base is smalled than the value
   */
  public static <T extends Comparable<T>> boolean notEqual(@NotNull T base, T value) {
    return base.compareTo(value) != 0;
  }

  /**
   * Returns the bigger value
   *
   * @param a The first argument to compare
   * @param b The second argument to compare
   * @param <T> The value type
   * @return The bigger of the two values
   */
  public static <T extends Comparable<T>> T max(T a, T b) {
    return bigger(a, b) ? a : b;
  }

  /**
   * Finds the largest value in an array
   *
   * @param array The array to find the max value in
   * @param <T> The array type
   * @return The largest value in the array
   */
  public static <T extends Comparable<T>> T max(T @NotNull [] array) {
    int index = 0;

    for (int i = 1; i < array.length - 1; i++) {
      if (bigger(array[i], array[index])) {
        index = i;
      }
    }

    return array[index];
  }

  /**
   * Returns the biggest value
   *
   * @param a The first argument to compare
   * @param b The second argument to compare
   * @param c The third argument to compare
   * @param <T> The value type
   * @return The biggest of the three values
   */
  public static <T extends Comparable<T>> T max(T a, T b, T c) {
    return max(max(a, b), c);
  }

  /**
   * Returns the smaller value
   *
   * @param a The first argument to compare
   * @param b The second argument to compare
   * @param <T> The value type
   * @return The smaller of the two values
   */
  public static <T extends Comparable<T>> T min(T a, T b) {
    return (smaller(a, b)) ? a : b;
  }

  /**
   * Finds the smallest value in an array
   *
   * @param array The array to find the min value in
   * @param <T> The array type
   * @return The smallest value in the array
   */
  public static <T extends Comparable<T>> T min(T @NotNull [] array) {
    int index = 0;

    for (int i = 1; i < array.length - 1; i++) {
      if (smaller(array[i], array[index])) {
        index = i;
      }
    }

    return array[index];
  }

  /**
   * Returns the smallest value
   *
   * @param a The first argument to compare
   * @param b The second argument to compare
   * @param c The third argument to compare
   * @param <T> The value type
   * @return The smallest of the three values
   */
  public static <T extends Comparable<T>> T min(T a, T b, T c) {
    return min(min(a, b), c);
  }
}
