package com.github.quaoz.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quaoz.structures.BinarySearchTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

// TODO: fuzzy search?
public class DataBase implements Closeable {
	private final CacheV2 cache;
	private final File location;
	private final File configLocation;
	private final DataBaseConfig config;

	/**
	 * Creates a new blank database or binds to an existing one
	 *
	 * @param location       The location of the database
	 * @param configLocation The location of the config file
	 */
	public DataBase(@NotNull Path location, @NotNull Path configLocation) {
		try {
			Files.createDirectories(location.getParent());
			Files.createDirectories(configLocation.getParent());

			if (!location.toFile().exists()) {
				Files.createFile(location);
			}

			if (!configLocation.toFile().exists()) {
				Files.createFile(configLocation);
				Files.copy(Objects.requireNonNull(getClass().getResourceAsStream("/config.json")), configLocation);
			}

			this.location = location.toFile();
			this.configLocation = configLocation.toFile();
			this.config = new ObjectMapper().readValue(this.configLocation, DataBaseConfig.class);
			updateRecordCount();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.cache = new CacheV2();
	}

	/**
	 * Creates a new blank database and config file
	 *
	 * @param location       The location of the database
	 * @param configLocation The location of the config file
	 * @param config         The config object
	 */
	public DataBase(@NotNull Path location, @NotNull Path configLocation, DataBaseConfig config) {
		try {
			Files.createDirectories(location.getParent());
			Files.createDirectories(configLocation.getParent());

			if (!location.toFile().exists()) {
				Files.createFile(location);
			}

			if (!configLocation.toFile().exists()) {
				Files.createFile(configLocation);
				new ObjectMapper().writeValue(configLocation.toFile(), config);
			}

			this.location = location.toFile();
			this.configLocation = configLocation.toFile();
			this.config = new ObjectMapper().readValue(this.configLocation, DataBaseConfig.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.cache = new CacheV2();
	}

	/**
	 * Adds a record to the database using RandomAccessFile to write to the file
	 *
	 * @param record The record to add
	 */
	public void add(@NotNull String record) {
		cache.add(record);

		// If the database is empty directly write the record
		if (config.recordCount == 0) {
			RandomFileHandler.writeBytes(location, 0, record.getBytes(StandardCharsets.UTF_8));
		} else if (config.recordCount == 1) {
			String base = new String(RandomFileHandler.readBytes(location, 0, config.recordLength));
			int comparison = record.substring(0, config.fields[0]).compareTo(base.substring(0, config.fields[0]));

			if (comparison > 0) {
				RandomFileHandler.writeBytes(location, config.recordLength, record.getBytes(StandardCharsets.UTF_8));
			} else {
				RandomFileHandler.writeBytes(location, 0, record.getBytes(StandardCharsets.UTF_8));
				RandomFileHandler.writeBytes(location, config.recordLength, base.getBytes(StandardCharsets.UTF_8));
			}
		} else {
			add(record, 0, config.recordCount);
		}

		config.recordCount++;
	}

	/**
	 * Gets the number of records in the database
	 *
	 * @return The number of records in the database
	 */
	public long getRecordCount() {
		return config.recordCount;
	}

	/**
	 * Updates the record count of the database.
	 */
	private void updateRecordCount() {
		try (Stream<String> stream = Files.lines(location.toPath())) {
			config.recordCount = stream.count();
		} catch (IOException e) {
			System.err.printf("Unable to access the file at %s", location);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a record to the database using RandomAccessFile to write to the file
	 *
	 * @param record The record to add
	 * @param start  The start index
	 * @param end    The end index
	 */
	private void add(@NotNull String record, long start, long end) {
		long mid = (start + end) / 2;
		int comparison = record.substring(0, config.fields[0]).compareTo(
				get(mid).substring(0, config.fields[0]));

		if (end - start == 1) {
			if (comparison < 0) {
				RandomFileHandler.insertBytes(location, record.getBytes(StandardCharsets.UTF_8), (mid - 1) * config.recordLength, config.recordLength);
			} else {
				RandomFileHandler.insertBytes(location, record.getBytes(StandardCharsets.UTF_8), mid * config.recordLength, config.recordLength);
			}
		} else if (comparison < 0) {
			add(record, start, mid);
		} else {
			add(record, mid, end);
		}
	}

	/**
	 * Gets a record from the database
	 *
	 * @param index The index of the record to get
	 *
	 * @return The string representation of the record at the given index
	 */
	public String get(long index) {
		if (index < 0 || index >= config.recordCount) {
			return null;
		}

		return new String(RandomFileHandler.readBytes(location, index * config.recordLength, config.recordLength));
	}

	/**
	 * Gets a record from the database
	 *
	 * @param field The record to get
	 *
	 * @return The record from the database
	 */
	public String get(String field, int compField) {
		if (field == null) {
			return null;
		}

		String cached = cache.get(field, compField);
		return cached == null ? get(field, 0, config.recordCount, compField) : cached;
	}

	/**
	 * Gets a record from the database
	 *
	 * @param compField The record to get
	 * @param start     The start index of the search
	 * @param end       The end index of the search
	 *
	 * @return The record from the database
	 */
	private @Nullable String get(@NotNull String field, long start, long end, int compField) {
		long mid = (start + end) / 2;
		String midRecord = get(mid);

		int comparison = compField == 0
				? midRecord.substring(0, config.fields[0]).strip().compareTo(field)
				: midRecord.substring(config.fields[compField - 1], config.fields[compField]).strip().compareTo(field);

		if (comparison == 0) {
			return midRecord;
		} else if (end - start <= 1) {
			return null;
		} else if (comparison > 0) {
			return get(field, start, mid, compField);
		} else {
			return get(field, mid, end, compField);
		}
	}

	/**
	 * @param field     The field to search for
	 * @param compField The index of the field
	 *
	 * @return A list of all records matching the field
	 */
	public ArrayList<String> collect(String field, int compField) {
		ArrayList<String> list = new ArrayList<>();
		field = field.strip();

		if (compField == 0) {
			for (long i = 0; i < config.recordCount; i++) {
				String line = get(i);
				if (line.substring(0, config.fields[0]).strip().equals(field)) {
					list.add(line);
				}
			}
		} else {
			for (long i = 0; i < config.recordCount; i++) {
				String line = get(i);
				if (line.substring(config.fields[compField - 1], config.fields[compField]).strip().equals(field)) {
					list.add(line);
				}
			}
		}

		return list;
	}

	/**
	 * Removes a record from the database
	 *
	 * @param field The record to remove
	 */
	public void remove(String field) {
		cache.remove(field);
		remove(field, 0, config.recordCount);
		config.recordCount--;
	}

	/**
	 * Removes a field from the database
	 *
	 * @param field The field to remove
	 * @param start The start index of the search
	 * @param end   The end index of the search
	 */
	private void remove(@NotNull String field, long start, long end) {
		long mid = (start + end) / 2;
		String midRecord = get(mid);
		int comparison = midRecord.substring(0, config.fields[0]).strip().compareTo(field);

		if (comparison == 0) {
			RandomFileHandler.deleteLine(location, mid * config.recordLength, config.recordLength);
		} else if (end - start > 1) {
			if (comparison > 0) {
				remove(field, start, mid);
			} else {
				remove(field, mid, end);
			}
		}
	}

	@Override
	public void close() throws IOException {
		new ObjectMapper().writeValue(configLocation, config);
	}

	/**
	 * The database cache
	 */

	// TODO: Extend bst and override get to work with compField parameter
	//       solution: Pass it two bounds and get the substring?
	private static class Cache {
		/**
		 * Stores the records in order
		 */
		BinarySearchTree<String> tree;

		/**
		 * Stores the records in order of when they were added
		 */
		ArrayList<String> list;

		/**
		 * Creates a new cache
		 */
		public Cache() {
			tree = new BinarySearchTree<>();
			list = new ArrayList<>();
		}

		/**
		 * Adds a record to the cache
		 *
		 * @param record The record to add
		 */
		public void add(@NotNull String record) {
			list.add(record);

			// If there are more than 100 records, remove the oldest one
			if (list.size() > 100) {
				tree.remove(list.get(0));
				list.remove(0);
			}

			tree.add(record);
		}

		/**
		 * Gets a record from the cache
		 *
		 * @param record The record to get
		 *
		 * @return The record
		 */
		public String get(String record) {
			// TODO: Rework the list so that it stores the records in order of when they were last accessed
			return tree.get(record);
		}

		/**
		 * Removes a record from the cache
		 *
		 * @param record The record to remove
		 */
		public void remove(String record) {
			tree.remove(record);
		}
	}

	private class CacheV2 {
		private static final int TOP_CACHE_SIZE = 100;
		private static final int RECENT_CACHE_SIZE = 100;
		private final Item[] recentList;
		private final Item[] topList;
		private int recentListSize;
		private int topListSize;


		public CacheV2() {
			recentList = new Item[RECENT_CACHE_SIZE];
			topList = new Item[TOP_CACHE_SIZE];
			recentListSize = 0;
			topListSize = 0;
		}

		public @Nullable String get(String field, int compField) {
			field = field.strip();

			if (compField == 0) {
				for (Item item : topList) {
					if (field.equals(item.value.substring(0, config.fields[0]).strip())) {
						add(item.value);
						return item.value;
					}
				}
			} else {
				for (Item item : recentList) {
					if (field.equals(item.value.substring(config.fields[compField], config.fields[compField + 1]))) {
						add(item.value);
						return item.value;
					}
				}
			}

			return null;
		}

		public void remove(String field) {
			field = field.strip();

			for (int i = 0; i < topListSize; i++) {
				if (field.equals(topList[i].value.substring(0, config.fields[0]))) {
					System.arraycopy(topList, i + 1, topList, i, topListSize - i);
					topListSize--;
					return;
				}
			}

			for (int i = 0; i < recentListSize; i++) {
				if (field.equals(recentList[i].value.substring(0, config.fields[0]))) {
					System.arraycopy(recentList, i + 1, recentList, i, recentListSize - i);
					recentListSize--;
					return;
				}
			}
		}

		public void add(@NotNull String record) {
			String recordComp = record.substring(0, config.recordLength).strip();

			if (topListSize < TOP_CACHE_SIZE) {
				topList[topListSize] = new Item(1, record);
				topListSize++;
				return;
			} else if (recentListSize < RECENT_CACHE_SIZE) {
				System.arraycopy(recentList, 0, recentList, 1, recentListSize);
				recentList[0] = new Item(1, record);
				recentListSize++;
				return;
			}

			// Search the top cache for a matching record
			for (int i = 0; i < TOP_CACHE_SIZE; i++) {
				if (recordComp.equals(topList[i].value.substring(0, config.fields[0]).strip())) {
					// If a record is found increase its hits
					topList[i].hit();

					// Adjust the records position in the cache based on its hits
					if (i == 0) {
						// If it's the first record do nothing
						return;
					} else if (i == 1 && topList[1].getHits() > topList[0].hits) {
						// If it's the second record swap it with the first one
						Item tmp = topList[0];
						topList[0] = topList[1];
						topList[1] = tmp;
						return;
					} else if (topList[i].getHits() > topList[i - 1].getHits()) {
						Item tmp = topList[i];
						int endIndex = i;
						int length = 0;

						// Iterate back through the array until an element with more hits is found
						while (endIndex > 0) {
							if (topList[endIndex - 1].getHits() <= tmp.getHits()) {
								endIndex--;
								length++;
							} else {
								break;
							}
						}

						if (length == 0) {
							topList[i] = topList[endIndex];
						} else {
							// Shift the elements down one
							System.arraycopy(topList, endIndex, topList, endIndex + 1, length);
						}
						// Add the record in the right place
						topList[endIndex] = tmp;
						return;
					}
				}
			}

			for (int i = 0; i < RECENT_CACHE_SIZE; i++) {
				if (recordComp.equals(recentList[i].value.substring(0, config.fields[0]).strip())) {
					recentList[i].hit();

					// Move the record into the top cache if it has enough hits
					Item tmp = recentList[i];
					if (recentList[i].getHits() >= topList[TOP_CACHE_SIZE].getHits()) {
						int endIndex = TOP_CACHE_SIZE;
						int length = 0;

						// Iterate back through the array until an element with more hits is found
						while (endIndex > 0) {
							if (topList[endIndex - 1].getHits() <= tmp.getHits()) {
								endIndex--;
								length++;
							} else {
								break;
							}
						}

						if (length == 0) {
							topList[TOP_CACHE_SIZE] = tmp;
							System.arraycopy(recentList, i + 1, recentList, i, recentList.length - i);
						} else {
							System.arraycopy(topList, endIndex, topList, endIndex + 1, length);
							topList[endIndex] = tmp;
						}

					} else {
						System.arraycopy(recentList, 0, recentList, 1, i - 1);
						recentList[0] = tmp;
					}
					return;
				}
			}

			System.arraycopy(recentList, 0, recentList, 1, recentList.length - 1);
			recentList[0] = new Item(1, record);
		}

		private class Item {
			public String value;
			private int hits;

			public Item(int hits, String value) {
				this.hits = hits;
				this.value = value;
			}

			public void hit() {
				hits++;
			}

			public int getHits() {
				return hits;
			}
		}
	}
}
