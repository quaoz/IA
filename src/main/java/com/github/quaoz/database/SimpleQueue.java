package com.github.quaoz.database;

/**
 * A simple generic queue
 */
public class SimpleQueue<T> {
    /**
     * The first element in the queue
     */
    private Node<T> head;

    /**
     * The last element in the queue
     */
    private Node<T> tail;

    /**
     * The size of the queue
     */
    private int size;

    /**
     * Constructs an empty queue
     */
    public SimpleQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds an element to the end of the queue
     *
     * @param value The value to add
     */
    public void enqueue(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    /**
     * Removes the first element of the queue
     *
     * @return The removed element or null if the queue is empty
     */
    public T dequeue() {
        if (head == null) {
            return null;
        }

        T value = head.value;
        head = head.next;
        size--;

        return value;
    }

    /**
     * Returns the first element of the queue without removing it
     *
     * @return The first element or null if the queue is empty
     */
    public T peek() {
        if (head == null) {
            return null;
        }

        return head.value;
    }

    /**
     * Returns the number of elements in the queue
     *
     * @return The number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if the queue is empty
     *
     * @return True if the queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Simple node
     */
    private static class Node<T> {
        public T value;
        public Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }
}
