package com.github.quaoz.managers;

import com.github.quaoz.Main;
import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.Moth;
import com.github.quaoz.structures.Pair;
import com.github.quaoz.structures.Record;
import com.github.quaoz.util.Geocoder;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class RecordManager implements Closeable {

	private static RecordManager recordManager;
	// id 32, species 64, location 32, date 16, size 16, username 64
	private final DataBaseConfig recordsConfig;
	private final DataBase recordsDatabase;

	private RecordManager() {
		Logger.info("Creating record manager...");

		final Path RECORDS_DB_FILE = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("db", "records.db"));
		final Path RECORDS_CONF_FILE = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("db", "records.json"));

		recordsConfig =
			new DataBaseConfig()
				.init(321, new Integer[] { 32, 96, 224, 240, 256, 321 });
		recordsDatabase =
			new DataBase(RECORDS_DB_FILE, RECORDS_CONF_FILE, recordsConfig);

		Logger.info("Finished creating record manager");
	}

	public void remove(Integer id) {
		recordsDatabase.remove(String.valueOf(id));
	}

	public static synchronized RecordManager getInstance() {
		return recordManager;
	}

	public static synchronized void init() {
		if (recordManager == null) {
			recordManager = new RecordManager();
		}
	}

	public void addRecord(
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

	public void close() {
		try {
			recordsDatabase.close();
		} catch (IOException e) {
			Logger.error(e, "Unable to close database");
			throw new RuntimeException(e);
		}
	}

	public @NotNull ArrayList<Record> getSpecies(String species) {
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

	public @NotNull ArrayList<Pair<Integer, Moth>> searchUser(String username) {
		username = username.strip();
		ArrayList<Pair<Integer, Moth>> moths = new ArrayList<>();

		recordsDatabase
			.collect(username, 5)
			.forEach(s ->
				moths.add(
					new Pair<>(
						Integer.parseInt(s.substring(0, recordsConfig.fields[0]).strip()),
						MothManager
							.getInstance()
							.basicSearch(
								s
									.substring(recordsConfig.fields[0], recordsConfig.fields[1])
									.strip()
							)
					)
				)
			);
		return moths;
	}

	public @NotNull ArrayList<Pair<Moth, Double>> searchLocation(
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
			Moth moth = MothManager
				.getInstance()
				.basicSearch(
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

	public String getRecord(Integer id) {
		return recordsDatabase.get(String.valueOf(id));
	}
}
