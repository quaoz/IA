package com.github.quaoz.tests;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;

public class Scripts {
	private static final int ITERATIONS = 3;
	private static final int MEMORY = 262144;
	private static final int PARALLELISM = 1;
	private static final int SALT_LENGTH = 16;
	private static final int HASH_LENGTH = 32;
	private static final Argon2Factory.Argon2Types TYPE = Argon2Factory.Argon2Types.ARGON2id;

	public static void main(String[] args) {
		d();
	}

	public static void d() {
		Argon2 argon2 = Argon2Factory.create(TYPE, SALT_LENGTH, HASH_LENGTH);
		// 1000 = The hash call must take at most 1000 ms
		// 65536 = Memory cost
		// 1 = parallelism
		int iterations = Argon2Helper.findIterations(argon2, 1000, 262144, 1);

		System.out.println("Optimal number of iterations: " + iterations);
	}

	private static void m() {
		Item[] topList = new Item[]{
				new Item(10, "a"),
				new Item(9, "b"),
				new Item(8, "z"),
				new Item(7, "c"),
				new Item(6, "d"),
				new Item(6, "d2"),
				new Item(5, "e"),
				new Item(5, "e2"),
				new Item(5, "e3"),
				new Item(4, "f")
		};

		for (Item item : topList) {
			item.display();
		}

		int i = 2;
		topList[i].hit();
		topList[i].display();

		/* ----- Start ---- */

		Item tmp = topList[i];
		int endIndex = i;
		int length = 0;

		while (endIndex > 0) {
			if (topList[endIndex - 1].getHits() <= tmp.getHits()) {
				endIndex--;
				length++;
			} else {
				break;
			}
		}

		if (length == 0) {
			topList[i] = topList[endIndex];
		} else {
			System.arraycopy(topList, endIndex, topList, endIndex + 1, length);
		}
		topList[endIndex] = tmp;

		/* ----- End ---- */

		System.out.println("\nFinal:");
		for (Item item : topList) {
			item.display();
		}
	}

	private static class Item {
		public String value;
		private int hits;

		public Item(int hits, String value) {
			this.hits = hits;
			this.value = value;
		}

		public void hit() {
			hits++;
		}

		public int getHits() {
			return hits;
		}

		public void display() {
			System.out.println(hits + " : " + value);
		}
	}
}
