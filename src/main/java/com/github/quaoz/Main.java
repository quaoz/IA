package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.gui.GUI;

import java.io.File;

public class Main {
	private static GUI gui;
	private static DataBase recordsDatabase;

	public static void main(String[] args) {
		gui = new GUI().init();
		// databaseInit();
	}

	private static void databaseInit() {

		File recordsDatabaseFile = new File("src/main/java/com/github/quaoz/tests/db/records.db");
		File recordsConfigFile = new File("src/main/java/com/github/quaoz/tests/db/records.json");
		DataBaseConfig recordsConfig = new DataBaseConfig();

		// id 16, species 64, location 32, date 16, size 16
		recordsConfig.fields = new Integer[]{16, 80, 112, 128, 144};
		recordsConfig.recordLength = 144;

		recordsDatabase = new DataBase(recordsDatabaseFile.toPath(), recordsConfigFile.toPath(), recordsConfig);
	}
}
