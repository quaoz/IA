package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;

import java.io.File;
import java.util.ArrayList;

public class MothManager {
	private static final File mothsDatabaseFile = new File("src/main/java/com/github/quaoz/tests/db/moths.db");
	private static final File mothsConfigFile = new File("src/main/java/com/github/quaoz/tests/db/moths.json");
	private static final DataBaseConfig mothsConfig = new DataBaseConfig();
	private static final DataBase mothsDatabase = new DataBase(mothsDatabaseFile.toPath(), mothsConfigFile.toPath(), mothsConfig);

	static {
		mothsConfig.recordLength = 368;
		// name 64, sci name 64, size 16, flight 32, habitat 64, food 128
		mothsConfig.fields = new Integer[]{64, 128, 144, 176, 240, 368};
	}

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
}