package com.github.quaoz.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quaoz.structures.BinarySearchTree;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class DataBase implements Closeable {
    private final Cache cache;
    private final File location;
    private final File configLocation;
    private final DataBaseConfig config;

    /**
     * Creates a new database or loads an existing one
     *
     * @param location       The location of the database
     * @param configLocation The location of the config file
     */
    public DataBase(@NotNull Path location, @NotNull Path configLocation) throws IllegalArgumentException {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.cache = new Cache();
    }

    public DataBase(@NotNull Path location, @NotNull Path configLocation, DataBaseConfig config) throws IllegalArgumentException {
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

        this.cache = new Cache();
    }

    /**
     * Adds a record to the database using RandomAccessFile to write to the file
     *
     * @param record The record to add
     */
    public void add(@NotNull String record) {
        // cache.add(record);

        if (config.recordCount == 0) {
            RandomFileHandler.writeBytes(location, 0, record.getBytes(StandardCharsets.UTF_8));
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
                get(mid, config.recordLength).substring(0, config.fields[0]));

        if (mid == 0) {
            if (comparison > 0) {
                RandomFileHandler.writeBytes(location, config.recordCount * config.recordLength, record.getBytes(StandardCharsets.UTF_8));
            } else {
                RandomFileHandler.insertBytes(location, record.getBytes(StandardCharsets.UTF_8), 0, config.recordLength);
            }
        } else if (comparison > 0) {
            add(record, start, mid);
        } else if (comparison < 0) {
            add(record, mid, end);
        } else {
            RandomFileHandler.insertBytes(location, record.getBytes(StandardCharsets.UTF_8), mid * config.recordLength, config.recordLength);
        }
    }

    /**
     * Gets a record from the database
     *
     * @param index  The index of the record to get
     * @param length The length of the record to get
     * @return The string representation of the record at the given index
     */
    public String get(long index, int length) {
        if (index < 0 || index >= config.recordCount) {
            return null;
        }

        return new String(RandomFileHandler.readBytes(location, index * length, length));
    }

    /**
     * Gets a record from the database
     *
     * @param field The record to get
     * @return The record from the database
     */
    public String get(String field, int compField) {
        if (field == null) {
            return null;
        }

        String cached = null; // cache.get(field);

        return Objects.requireNonNullElseGet(cached, () -> get(field, 0, config.recordCount, compField));
    }

    /**
     * Gets a record from the database
     *
     * @param compField The record to get
     * @param start     The start index of the search
     * @param end       The end index of the search
     * @return The record from the database
     */
    private @NotNull String get(@NotNull String field, long start, long end, int compField) {
        long mid = (start + end) / 2;
        String midRecord = get(mid, config.recordLength);
        int comparison = midRecord.substring(compField, config.fields[compField]).strip().compareTo(field);

        if (comparison > 0) {
            return get(field, start, mid, compField);
        } else if (comparison < 0) {
            return get(field, mid, end, compField);
        } else {
            return midRecord;
        }
    }

    /**
     * Removes a record from the database
     *
     * @param field The record to remove
     */
    public void remove(String field) {
        // cache.remove(field);
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
        String midRecord = get(mid, config.recordLength).substring(0, config.fields[0]);
        int comparison = midRecord.compareTo(field);

        if (comparison > 0) {
            remove(field, start, mid);
        } else if (comparison < 0) {
            remove(field, mid, end);
        } else {
            RandomFileHandler.deleteLine(location, mid * config.recordLength, config.recordLength);
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
}
