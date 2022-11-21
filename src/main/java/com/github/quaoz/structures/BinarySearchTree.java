package com.github.quaoz.structures;

import com.github.quaoz.util.Comparisons;
import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Binary Tree implementation
 *
 * @param <T> The trees type
 */
public class BinarySearchTree<T extends Comparable<T>> {

	private BinaryNode<T> root;

	/**
	 * Constructs a new empty binary search tree
	 */
	public BinarySearchTree() {
		root = null;
	}

	/**
	 * Constructs a new binary search tree with the given value as its root
	 *
	 * @param value The value to use as the root of this tree
	 */
	public BinarySearchTree(T value) {
		root = new BinaryNode<>(value);
	}

	/**
	 * Removes the root node and replaces it with its left or right child, if it has one
	 */
	private @Nullable BinaryNode<T> removeRoot() {
		BinaryNode<T> prevRoot = root;

		// Return null if the tree is empty
		if (root == null) {
			return null;
		}

		BinaryNode<T> left = root.left;
		BinaryNode<T> right = root.right;

		// If the root has no left children, set the root to its right child
		if (left == null) {
			root = right;
		} else {
			// If the root has a left child, find the rightmost node in the left subtree
			BinaryNode<T> rightMost = left;
			while (rightMost.right != null) {
				rightMost = rightMost.right;
			}

			// Set the rightmost node's right child to the root's right child (append the right
			// subtree to the left subtree)
			rightMost.right = right;
			root = left;
		}

		// Return the previous root
		return prevRoot;
	}

	/**
	 * Inserts the given value into the tree
	 *
	 * @param value The value to insert
	 */
	public void add(T value) {
		// If the tree is empty, create a new root node
		if (root == null) {
			root = new BinaryNode<>(value);
		} else {
			add(root, value);
		}
	}

	/**
	 * Inserts the given value into the tree
	 *
	 * @param node  The node to start at
	 * @param value The value to insert
	 */
	private void add(@NotNull BinaryNode<T> node, @NotNull T value) {
		// If the value is greater than the current node's value, go right

		if (Comparisons.bigger(value, node.value)) {
			if (node.right == null) {
				node.right = new BinaryNode<>(value);
			} else {
				add(node.right, value);
			}
		} else {
			// Otherwise, go left
			if (node.left == null) {
				node.left = new BinaryNode<>(value);
			} else {
				add(node.left, value);
			}
		}
	}

	/**
	 * Removes the given value from the tree
	 *
	 * @param value The value to remove
	 *
	 * @return The value removed
	 */
	public BinaryNode<T> remove(T value) {
		return remove(root, value);
	}

	/**
	 * Removes the given value from the tree
	 *
	 * @param node  The node to start at
	 * @param value The value to remove
	 *
	 * @return The value removed
	 */
	private BinaryNode<T> remove(BinaryNode<T> node, T value) {
		// Return null if the tree is empty
		if (node == null) {
			return null;
		} else {
			int comparison = value.compareTo(node.value);
			if (value.compareTo(root.value) == 0) {
				// If the value is the root, remove the root
				return removeRoot();
			} else if (comparison > 0) {
				// If the value is greater than the current node's value, go right
				node.right = remove(node.right, value);
			} else if (comparison < 0) {
				// Otherwise, go left
				node.left = remove(node.left, value);
			} else {
				// If the value is the current node's value, remove it
				if (node.left == null) {
					return node.right;
				} else if (node.right == null) {
					return node.left;
				} else {
					// If the node has two children, replace it with its left child's rightmost child
					BinaryNode<T> temp = node;
					node = min(node.right);
					node.right = remove(node.right, temp.value);
				}
			}
		}

		return node;
	}

	/**
	 * Gets the minimum value in the tree
	 *
	 * @param node The node to start at
	 *
	 * @return The minimum value in the tree
	 */
	@Contract(pure = true)
	private @NotNull BinaryNode<T> min(@NotNull BinaryNode<T> node) {
		if (node.left == null) {
			return node;
		}

		// Go left until we hit a leaf
		return min(node.left);
	}

	/**
	 * Visually prints the tree
	 */
	public void print() {
		print(root, 0);
	}

	/**
	 * Visually prints the tree
	 *
	 * @param node  The node to start at
	 * @param depth The current depth
	 */
	private void print(BinaryNode<T> node, int depth) {
		// Return if the node is null
		if (node == null) {
			return;
		}

		// Recursively print the right and left nodes with one level of indentation more for each
		// recursion
		print(node.right, depth + 1);

		for (int i = 0; i < depth; i++) {
			System.out.print("\t");
		}
		System.out.println(node.value);

		print(node.left, depth + 1);
	}

	/**
	 * Balances the tree
	 */
	public void balance() {
		root = balance(root);
	}

	/**
	 * Balances the tree
	 *
	 * @param node The node to start at
	 *
	 * @return The balanced tree
	 */
	private BinaryNode<T> balance(BinaryNode<T> node) {
		// Return if the subtree is empty
		if (node == null) {
			return null;
		}

		// Recursively balance the left and right subtrees
		node.left = balance(node.left);
		node.right = balance(node.right);

		int leftHeight = height(node.left);
		int rightHeight = height(node.right);

		// If the left subtree is taller than the right, rotate right
		if (leftHeight - rightHeight > 1) {
			if (height(node.left.left) < height(node.left.right)) {
				node.left = rotateLeft(node.left);
			}
			node = rotateRight(node);
		} else if (rightHeight - leftHeight > 1) {
			// Otherwise, rotate left
			if (height(node.right.right) < height(node.right.left)) {
				node.right = rotateRight(node.right);
			}
			node = rotateLeft(node);
		}

		return node;
	}

	/**
	 * Rotates the tree left
	 *
	 * @param node The node to rotate around
	 *
	 * @return The new root of the subtree
	 */
	private @NotNull BinaryNode<T> rotateLeft(@NotNull BinaryNode<T> node) {
		BinaryNode<T> temp = node.right;
		node.right = temp.left;
		temp.left = node;

		return temp;
	}

	/**
	 * Rotates the tree right
	 *
	 * @param node The node to rotate around
	 *
	 * @return The new root of the subtree
	 */
	private @NotNull BinaryNode<T> rotateRight(@NotNull BinaryNode<T> node) {
		BinaryNode<T> temp = node.left;
		node.left = temp.right;
		temp.right = node;

		return temp;
	}

	/**
	 * Gets the height of the tree
	 *
	 * @return The height of the tree
	 */
	public int height() {
		return height(root);
	}

	/**
	 * Gets the height of the tree
	 *
	 * @param node The node to start at
	 *
	 * @return The height of the tree
	 */
	private int height(BinaryNode<T> node) {
		// Return 0 if the tree is empty
		if (node == null) {
			return 0;
		}

		// Recursively get the height of the left and right subtrees
		int leftHeight = height(node.left);
		int rightHeight = height(node.right);

		// Return the larger of the two
		return Math.max(leftHeight, rightHeight) + 1;
	}

	/**
	 * Checks if the tree contains the given value
	 *
	 * @param value The value to check for
	 *
	 * @return {@code true} if the tree contains the value, {@code false} otherwise
	 */
	public boolean contains(T value) {
		return contains(root, value);
	}

	/**
	 * Checks if the tree contains the given value
	 *
	 * @param node  The node to start at
	 * @param value The value to check for
	 *
	 * @return {@code true} if the tree contains the value, {@code false} otherwise
	 */
	private boolean contains(BinaryNode<T> node, T value) {
		if (node == null) {
			return false;
		} else {
			int comparison = node.value.compareTo(value);
			if (comparison > 0) {
				// Go left if the value is less than the current node
				return contains(node.left, value);
			} else if (comparison < 0) {
				// Go right if the value is greater than the current node
				return contains(node.right, value);
			} else {
				// Return true if the value is equal to the current node
				return true;
			}
		}
	}

	/**
	 * Gets the node with the given value
	 *
	 * @param value The value to get the node for
	 *
	 * @return The node with the given value
	 */
	public T get(T value) {
		return get(root, value);
	}

	/**
	 * Gets the node with the given value
	 *
	 * @param node  The node to start at
	 * @param value The value to get the node for
	 *
	 * @return The node with the given value
	 */
	private T get(BinaryNode<T> node, T value) {
		if (node == null) {
			return null;
		} else {
			int comparison = node.value.compareTo(value);
			if (comparison > 0) {
				// Go left if the value is less than the current node
				return get(node.left, value);
			} else if (comparison < 0) {
				// Go right if the value is greater than the current node
				return get(node.right, value);
			} else {
				// Return the value if it is equal to the current node
				return node.value;
			}
		}
	}

	/**
	 * Traverses the tree pre-order and returns an arraylist of the values
	 */
	public ArrayList<T> preOrder() {
		return preOrder(root);
	}

	/**
	 * Traverses the tree pre-order and returns an arraylist of the values
	 *
	 * @param node The node to start at
	 *
	 * @return An arraylist of the values
	 */
	private @NotNull ArrayList<T> preOrder(BinaryNode<T> node) {
		ArrayList<T> list = new ArrayList<>();

		// Return an empty list if the tree is empty
		if (node != null) {
			// Recursively traverse the left and right subtrees adding the values to the list
			list.add(node.value);
			list.addAll(preOrder(node.left));
			list.addAll(preOrder(node.right));
		}

		return list;
	}

	/**
	 * Traverses the tree in-order and returns an arraylist of the values
	 */
	public ArrayList<T> inOrder() {
		return inOrder(root);
	}

	/**
	 * Traverses the tree in-order and returns an arraylist of the values
	 *
	 * @param node The node to start at
	 *
	 * @return An arraylist of the values
	 */
	private @NotNull ArrayList<T> inOrder(BinaryNode<T> node) {
		ArrayList<T> list = new ArrayList<>();

		// Return an empty list if the tree is empty
		if (node != null) {
			// Recursively traverse the left and right subtrees adding the values to the list
			list.addAll(inOrder(node.left));
			list.add(node.value);
			list.addAll(inOrder(node.right));
		}

		return list;
	}

	/**
	 * Traverses the tree post-order and returns an arraylist of the values
	 */
	public ArrayList<T> postOrder() {
		return postOrder(root);
	}

	/**
	 * Traverses the tree post-order and returns an arraylist of the values
	 *
	 * @param node The node to start at
	 *
	 * @return An arraylist of the values
	 */
	private @NotNull ArrayList<T> postOrder(BinaryNode<T> node) {
		ArrayList<T> list = new ArrayList<>();

		// Return an empty list if the tree is empty
		if (node != null) {
			// Recursively traverse the left and right subtrees adding the values to the list
			list.addAll(postOrder(node.left));
			list.addAll(postOrder(node.right));
			list.add(node.value);
		}

		return list;
	}

	/**
	 * Traverses the tree level-order and returns an arraylist of the values
	 */
	public ArrayList<T> levelOrder() {
		return levelOrder(root);
	}

	/**
	 * Traverses the tree level-order and returns an arraylist of the values
	 *
	 * @param node The node to start at
	 *
	 * @return An arraylist of the values
	 */
	private @NotNull ArrayList<T> levelOrder(BinaryNode<T> node) {
		// Create a queue to store the nodes
		SimpleQueue<BinaryNode<T>> queue = new SimpleQueue<>();
		ArrayList<T> list = new ArrayList<>();

		// Add the root to the queue
		if (node != null) {
			queue.enqueue(node);
		}

		while (!queue.isEmpty()) {
			// Dequeue the next node and add its value to the list
			BinaryNode<T> current = queue.dequeue();
			list.add(current.value);

			// Add the left and right children to the queue if they exist
			if (current.left != null) {
				queue.enqueue(current.left);
			}

			if (current.right != null) {
				queue.enqueue(current.right);
			}
		}

		return list;
	}

	/**
	 * A simple node class for the binary search tree containing a value and a left and right child
	 */
	private static class BinaryNode<T extends Comparable<T>> {

		/**
		 * The value of the node
		 */
		final T value;

		/**
		 * The left child of the node
		 */
		BinaryNode<T> left;

		/**
		 * The right child of the node
		 */
		BinaryNode<T> right;

		/**
		 * Constructs a new node with the given value
		 *
		 * @param value The nodes value
		 */
		public BinaryNode(T value) {
			this.value = value;
		}
	}
}
