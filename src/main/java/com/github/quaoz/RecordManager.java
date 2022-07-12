package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;

import java.io.File;

public class RecordManager {
	private static final File recordsDatabaseFile = new File("src/main/java/com/github/quaoz/tests/db/records.db");
	private static final File recordsConfigFile = new File("src/main/java/com/github/quaoz/tests/db/records.json");
	// id 16, species 64, location 32, date 16, size 16
	private static final DataBaseConfig recordsConfig = new DataBaseConfig().init(144, new Integer[]{16, 80, 112, 128, 144});
	private static final DataBase recordsDatabase = new DataBase(recordsDatabaseFile.toPath(), recordsConfigFile.toPath());

	public static boolean addRecord(Integer id, String species, String location, String date, String size) {
		String record = String.format("%-16s %-64s %-32s %-16s %-16s", id, species, location, date, size);

		if (record.length() != recordsConfig.recordLength) {
			return false;
		}

		recordsDatabase.add(record);
		return true;
	}

	public static String getRecord(Integer id) {
		return recordsDatabase.get(String.valueOf(id), 0);
	}
}
