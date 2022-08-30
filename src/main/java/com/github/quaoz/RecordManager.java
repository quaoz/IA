package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class RecordManager {
	private static final File RECORDS_DB_FILE = new File("src/main/java/com/github/quaoz/tests/db/records.db");
	private static final File RECORDS_CONF_FILE = new File("src/main/java/com/github/quaoz/tests/db/records.json");
	// id 16, species 64, location 32, date 16, size 16
	private static final DataBaseConfig recordsConfig = new DataBaseConfig().init(144, new Integer[]{16, 80, 112, 128, 144});
	private static final DataBase recordsDatabase = new DataBase(RECORDS_DB_FILE.toPath(), RECORDS_CONF_FILE.toPath(), recordsConfig);

	public static void addRecord(Integer id, String species, String location, String date, String size) {
		String record = String.format("%-16s %-64s %-32s %-16s %-16s\n", id, species, location, date, size);
		recordsDatabase.add(record);
	}

	public static void close() {
		try {
			recordsDatabase.close();
		} catch (IOException e) {
			Logger.error(e, "Unable to close database");
			throw new RuntimeException(e);
		}
	}

	public static String getRecord(Integer id) {
		return recordsDatabase.get(String.valueOf(id));
	}
}
