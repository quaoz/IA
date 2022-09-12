package com.github.quaoz.structures;

/** A simple generic stack */
public class SimpleStack<T> {
  /** The top element in the stack */
  private Node<T> top;

  /** The size of the stack */
  private int size;

  /** Constructs an empty stack */
  public SimpleStack() {
    top = null;
    size = 0;
  }

  /**
   * Adds an element to the top of the stack
   *
   * @param value The value to add
   */
  public void push(T value) {
    Node<T> node = new Node<>(value);
    node.previous = top;
    top = node;
    size++;
  }

  /**
   * Removes and returns the element at the top of the stack
   *
   * @return The element at the top of the stack
   */
  public T pop() {
    Node<T> popped = top;
    top = popped.previous;
    size--;

    return popped.value;
  }

  /**
   * Returns the element at the top of the stack
   *
   * @return The element at the top of the stack
   */
  public T peek() {
    return top.value;
  }

  /**
   * Returns the size of the stack
   *
   * @return The size of the stack
   */
  public int size() {
    return size;
  }

  /** Simple node */
  private static class Node<T> {
    public T value;
    public Node<T> previous;

    public Node(T value) {
      this.value = value;
    }
  }
}
