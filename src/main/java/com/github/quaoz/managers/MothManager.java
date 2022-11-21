package com.github.quaoz.managers;

import com.github.quaoz.Main;
import com.github.quaoz.database.CustomRatio;
import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.Moth;
import com.github.quaoz.structures.Pair;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class MothManager implements Closeable {

	private static MothManager mothManager;
	// name 64, sci name 64, size 16, flight 32, habitat 64, food 128
	private final DataBaseConfig mothsConfig;
	private final DataBase mothsDatabase;

	private MothManager() {
		Logger.info("Creating user manager...");

		Path MOTHS_DB_FILE = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("db", "moths.db"));
		Path MOTHS_CONF_FILE = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("db", "moths.json"));

		mothsConfig =
			new DataBaseConfig()
				.init(369, new Integer[] { 64, 128, 144, 176, 240, 368 });
		mothsDatabase = new DataBase(MOTHS_DB_FILE, MOTHS_CONF_FILE, mothsConfig);

		Logger.info("Finished creating user manager");
	}

	/**
	 * Gets the {@link MothManager} instance
	 *
	 * @return The {@link MothManager} instance
	 */
	public static synchronized MothManager getInstance() {
		return mothManager;
	}

	/**
	 * Initialises the {@link MothManager} instance if it hasn't already been initialised
	 */
	public static synchronized void init() {
		if (mothManager == null) {
			mothManager = new MothManager();
		}
	}

	/**
	 * Removes the given moth from the database
	 *
	 * @param name The name of the moth to remove
	 */
	public void remove(String name) {
		mothsDatabase.remove(name);
	}

	/**
	 * Adds a new moth to the database
	 *
	 * @param name 			The moths name
	 * @param sciName		The moths scientific name
	 * @param sizeLower		The moths lower bound for size
	 * @param sizeUpper		The moths upper bound for size
	 * @param flightStart	The month the moth starts flying
	 * @param flightEnd		The month the moth stops flying
	 * @param habitat		The moths habitat
	 * @param food			The moths food source
	 */
	public void addMoth(
		String name,
		String sciName,
		double sizeLower,
		double sizeUpper,
		int flightStart,
		int flightEnd,
		String habitat,
		String food
	) {
		String record = String.format(
			"%-64s%-64s%-16s%-32s%-64s%-128s\n",
			name,
			sciName,
			String.format("%s:%s", sizeLower, sizeUpper),
			String.format("%s:%s", flightStart, flightEnd),
			habitat,
			food
		);
		mothsDatabase.add(record);
	}

	/**
	 * Searches for a moth by name
	 *
	 * @param name The moths name
	 *
	 * @return The moth
	 */
	public @NotNull Moth basicSearch(@NotNull String name) {
		name = name.strip();
		String record = mothsDatabase.get(name);

		String sciName = record
			.substring(mothsConfig.fields[0], mothsConfig.fields[1])
			.strip();
		String size = record
			.substring(mothsConfig.fields[1], mothsConfig.fields[2])
			.strip();
		String flight = record
			.substring(mothsConfig.fields[2], mothsConfig.fields[3])
			.strip();
		String habitat = record
			.substring(mothsConfig.fields[3], mothsConfig.fields[4])
			.strip();
		String food = record
			.substring(mothsConfig.fields[4], mothsConfig.fields[5])
			.strip();

		return new Moth(
			name,
			sciName,
			Double.parseDouble(size.split(":")[0]),
			Double.parseDouble(size.split(":")[1]),
			Integer.parseInt(flight.split(":")[0]),
			Integer.parseInt(flight.split(":")[1]),
			habitat,
			food
		);
	}

	public @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
		@NotNull String field,
		int compField
	) {
		return collectMoths(field, compField, null);
	}

	public @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
		@NotNull String field,
		int compField,
		CustomRatio customRatio
	) {
		return collectMoths(field, compField, 10, customRatio);
	}

	public @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
		@NotNull String field,
		int compField,
		int count
	) {
		return collectMoths(field, compField, count, null);
	}

	public @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
		@NotNull String field,
		int compField,
		int count,
		CustomRatio customRatio
	) {
		field = field.strip();

		ArrayList<Pair<String, Double>> rawMoths = new ArrayList<>(
			mothsDatabase.collect(field, compField, count, customRatio)
		);

		/*mothsDatabase
			.collect(field, compField)
			.forEach(s -> rawMoths.add(new Pair<>(s, 100.0)));

		if (rawMoths.size() <= 1) {
			rawMoths.addAll(
				mothsDatabase.collect(field, compField, count, customRatio)
			);
		}*/

		ArrayList<Pair<Moth, Double>> moths = new ArrayList<>();

		for (Pair<String, Double> pair : rawMoths) {
			String s = pair.getKey();
			if (s != null) {
				String size = s
					.substring(mothsConfig.fields[1], mothsConfig.fields[2])
					.strip();
				String flight = s
					.substring(mothsConfig.fields[2], mothsConfig.fields[3])
					.strip();

				moths.add(
					new Pair<>(
						new Moth(
							s.substring(0, mothsConfig.fields[0]).strip(),
							s.substring(mothsConfig.fields[0], mothsConfig.fields[1]).strip(),
							Double.parseDouble(size.split(":")[0]),
							Double.parseDouble(size.split(":")[1]),
							Integer.parseInt(flight.split(":")[0]),
							Integer.parseInt(flight.split(":")[1]),
							s.substring(mothsConfig.fields[3], mothsConfig.fields[4]).strip(),
							s.substring(mothsConfig.fields[4], mothsConfig.fields[5]).strip()
						),
						pair.getValue()
					)
				);
			}
		}

		return moths;
	}

	public void close() {
		try {
			mothsDatabase.close();
		} catch (IOException e) {
			Logger.error(e, "Unable to close database");
			throw new RuntimeException(e);
		}
	}
}
