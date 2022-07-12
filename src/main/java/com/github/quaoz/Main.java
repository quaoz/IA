package com.github.quaoz;

import com.github.quaoz.gui.GUI;

public class Main {
	private static GUI gui;

	public static void main(String[] args) {
		gui = new GUI().init();
	}
}
