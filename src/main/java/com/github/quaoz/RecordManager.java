package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.Pair;
import com.github.quaoz.util.Geocoder;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class RecordManager {

	private static final Path RECORDS_DB_FILE = Main
		.getInstallDir()
		.resolve(Paths.get("db", "records.db"));
	private static final Path RECORDS_CONF_FILE = Main
		.getInstallDir()
		.resolve(Paths.get("db", "records.json"));
	// id 32, species 64, location 32, date 16, size 16, username 64
	private static final DataBaseConfig recordsConfig = new DataBaseConfig()
		.init(321, new Integer[] { 32, 96, 224, 240, 256, 321 });
	private static final DataBase recordsDatabase = new DataBase(
		RECORDS_DB_FILE,
		RECORDS_CONF_FILE,
		recordsConfig
	);

	public static void addRecord(
		String species,
		String location,
		String date,
		Double size,
		String user
	) {
		String record = String.format(
			"%-32s%-64s%-128s%-16s%-16s%-64s\n",
			recordsDatabase.getRecordCount(),
			species,
			location,
			date,
			size,
			user
		);
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

	public static @NotNull ArrayList<Record> getSpecies(String species) {
		species = species.strip();
		ArrayList<Record> records = new ArrayList<>();

		recordsDatabase
			.collect(species, 1)
			.forEach(s ->
				records.add(
					new Record(
						Integer.parseInt(s.substring(0, recordsConfig.fields[0]).strip()),
						s
							.substring(recordsConfig.fields[0], recordsConfig.fields[1])
							.strip(),
						s
							.substring(recordsConfig.fields[1], recordsConfig.fields[2])
							.strip(),
						s
							.substring(recordsConfig.fields[2], recordsConfig.fields[3])
							.strip(),
						Double.parseDouble(
							s
								.substring(recordsConfig.fields[3], recordsConfig.fields[4])
								.strip()
						),
						s
							.substring(recordsConfig.fields[4], recordsConfig.fields[5])
							.strip()
					)
				)
			);
		return records;
	}

	public static @NotNull ArrayList<Moth> searchUser(String username) {
		username = username.strip();
		ArrayList<Moth> moths = new ArrayList<>();

		recordsDatabase
			.collect(username, 5)
			.forEach(s -> moths.add(MothManager.basicSearch(s)));
		return moths;
	}

	public static @NotNull ArrayList<Pair<Moth, Double>> searchLocation(
		String location
	) {
		location = Geocoder.standardise(location);
		ArrayList<Pair<String, Double>> records = recordsDatabase.collect(
			location,
			2,
			100,
			(s1, s2) -> {
				List<String> split1 = new ArrayList<>(List.of(s1.split(", ")));
				List<String> split2 = new ArrayList<>(List.of(s2.split(", ")));

				double ratio = 0.0;

				for (int i = 0; i < split1.size(); i++) {
					if (
						split1.get(i).strip().equals("null") ||
						split2.get(i).strip().equals("null")
					) {
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
			}
		);

		ArrayList<Pair<Moth, Double>> moths = new ArrayList<>();

		for (Pair<String, Double> record : records) {
			Moth moth = MothManager.basicSearch(
				record
					.getKey()
					.substring(recordsConfig.fields[0], recordsConfig.fields[1])
			);

			if (
				moths.stream().noneMatch(m -> m.getKey().name().equals(moth.name()))
			) {
				moths.add(new Pair<>(moth, record.getValue()));
			}
		}

		return moths;
	}

	public static String getRecord(Integer id) {
		return recordsDatabase.get(String.valueOf(id));
	}
}
