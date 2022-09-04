package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class RecordManager {
	private static final File RECORDS_DB_FILE = new File("src/main/java/com/github/quaoz/tests/db/records.db");
	private static final File RECORDS_CONF_FILE = new File("src/main/java/com/github/quaoz/tests/db/records.json");
	// id 32, species 64, location 32, date 16, size 16, username 64
	private static final DataBaseConfig recordsConfig = new DataBaseConfig().init(321, new Integer[]{32, 96, 224, 240, 256, 321});
	private static final DataBase recordsDatabase = new DataBase(RECORDS_DB_FILE.toPath(), RECORDS_CONF_FILE.toPath(), recordsConfig);

	public static void addRecord(String species, String location, String date, Double size, String user) {
		String record = String.format("%-32s%-64s%-128s%-16s%-16s%-64s\n", recordsDatabase.getRecordCount(), species, location, date, size, user);
		System.out.println(record);
		System.out.println(record.length());
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
