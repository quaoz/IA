package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MothManager {
	private static final File MOTHS_DB_FILE = new File("src/main/java/com/github/quaoz/tests/db/moths.db");
	private static final File MOTHS_CONF_FILE = new File("src/main/java/com/github/quaoz/tests/db/moths.json");
	// name 64, sci name 64, size 16, flight 32, habitat 64, food 128
	private static final DataBaseConfig mothsConfig = new DataBaseConfig().init(368, new Integer[]{64, 128, 144, 176, 240, 368});
	private static final DataBase mothsDatabase = new DataBase(MOTHS_DB_FILE.toPath(), MOTHS_CONF_FILE.toPath(), mothsConfig);

	public static Moth basicSearch(String name) {
		String record = mothsDatabase.get(name, 0);

		if (record == null) {
			return null;
		}

		String sciName = record.substring(mothsConfig.fields[0], mothsConfig.fields[1]).strip();
		String size = record.substring(mothsConfig.fields[1], mothsConfig.fields[2]).strip();
		String flight = record.substring(mothsConfig.fields[2], mothsConfig.fields[3]).strip();
		String habitat = record.substring(mothsConfig.fields[3], mothsConfig.fields[4]).strip();
		String food = record.substring(mothsConfig.fields[4], mothsConfig.fields[5]).strip();

		return new Moth(
				name,
				sciName,
				Integer.getInteger(size.split(":")[0]),
				Integer.getInteger(size.split(":")[1]),
				Integer.getInteger(flight.split(":")[0]),
				Integer.getInteger(flight.split(":")[1]),
				habitat,
				food
		);
	}

	public static ArrayList<Moth> advancedSearch(String name, String location, Integer sizeLower, Integer sizeUpper, Integer flightStart, Integer flightEnd, String habitat, String foodSources) {
		ArrayList<Moth> moths = new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();

		list = name != null
				? mothsDatabase.collect(name, 0)
				: sizeLower != null && sizeUpper != null
				? mothsDatabase.collect(sizeLower + ":" + sizeUpper, 2)
				: flightStart != null && flightEnd != null
				? mothsDatabase.collect(flightStart + ":" + flightEnd, 3)
				: habitat != null
				? mothsDatabase.collect(habitat, 5)
				: mothsDatabase.collect(foodSources, 6);

		for (String s : list) {
			String size = s.substring(mothsConfig.fields[1], mothsConfig.fields[2]).strip();
			String flight = s.substring(mothsConfig.fields[2], mothsConfig.fields[3]).strip();

			moths.add(new Moth(
					s.substring(0, mothsConfig.fields[0]).strip(),
					s.substring(mothsConfig.fields[0], mothsConfig.fields[1]).strip(),
					Integer.getInteger(size.split(":")[0]),
					Integer.getInteger(size.split(":")[1]),
					Integer.getInteger(flight.split(":")[0]),
					Integer.getInteger(flight.split(":")[1]),
					s.substring(mothsConfig.fields[3], mothsConfig.fields[4]).strip(),
					s.substring(mothsConfig.fields[4], mothsConfig.fields[5]).strip()
			));
		}

		return moths;
	}

	public static void close() {
		try {
			mothsDatabase.close();
		} catch (IOException e) {
			Logger.error(e, "Unable to close database");
			throw new RuntimeException(e);
		}
	}
}
