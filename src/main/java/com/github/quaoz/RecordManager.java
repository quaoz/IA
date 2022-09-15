package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.Pair;
import com.github.quaoz.util.Geocoder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class RecordManager {
private static final File RECORDS_DB_FILE =
	new File("src/main/java/com/github/quaoz/tests/db/records.db");
private static final File RECORDS_CONF_FILE =
	new File("src/main/java/com/github/quaoz/tests/db/records.json");
// id 32, species 64, location 32, date 16, size 16, username 64
private static final DataBaseConfig recordsConfig =
	new DataBaseConfig().init(321, new Integer[] {32, 96, 224, 240, 256, 321});
private static final DataBase recordsDatabase =
	new DataBase(RECORDS_DB_FILE.toPath(), RECORDS_CONF_FILE.toPath(), recordsConfig);

public static void addRecord(
	String species, String location, String date, Double size, String user) {
	String record =
		String.format(
			"%-32s%-64s%-128s%-16s%-16s%-64s\n",
			recordsDatabase.getRecordCount(), species, location, date, size, user);
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

public static @NotNull ArrayList<Moth> searchUser(String username) {
	username = username.strip();
	ArrayList<Moth> moths = new ArrayList<>();

	recordsDatabase.collect(username, 5).forEach(s -> moths.add(MothManager.basicSearch(s)));
	return moths;
}

public static @NotNull ArrayList<Pair<Moth, Double>> searchLocation(String location) {
	location = Geocoder.standardise(location);
	ArrayList<Pair<String, Double>> records =
		recordsDatabase.collect(
			location,
			2,
			100,
			(s1, s2) -> {
			List<String> split1 = new ArrayList<>(List.of(s1.split(", ")));
			List<String> split2 = new ArrayList<>(List.of(s2.split(", ")));

			double ratio = 0.0;

			for (int i = 0; i < split1.size(); i++) {
				if (split1.get(i).strip().equals("null") || split2.get(i).strip().equals("null")) {
				split1.remove(i);
				split2.remove(i);
				i--;
				}
			}

			for (int i = 0; i < split1.size(); i++) {
				if (split1.get(i).equals(split2.get(i))) {
				ratio += i + 1;
				} else {
				ratio--;
				}
			}

			return ratio;
			});

	ArrayList<Pair<Moth, Double>> moths = new ArrayList<>();

	for (Pair<String, Double> record : records) {
	Moth moth =
		MothManager.basicSearch(
			record.getKey().substring(recordsConfig.fields[0], recordsConfig.fields[1]));

	if (moths.stream().noneMatch(m -> m.getKey().getName().equals(moth.getName()))) {
		moths.add(new Pair<>(moth, record.getValue()));
	}
	}

	return moths;
}

public static String getRecord(Integer id) {
	return recordsDatabase.get(String.valueOf(id));
}
}
