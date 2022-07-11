package com.github.quaoz.tests;

public class Scripts {
	public static void main(String[] args) {
		m();
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
