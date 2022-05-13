package com.github.quaoz.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quaoz.structures.BinarySearchTree;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class DataBase<T extends Comparable<Object>> implements Closeable {
    private final Cache<T> cache;
    private final File location;
    private final File configLocation;
    private final DataBaseConfig config;

    /**
     * Creates a new database or loads an existing one
     *
     * @param location The location of the database
     * @param configLocation   The location of the config file
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

        this.cache = new Cache<>();
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

        this.cache = new Cache<>();
    }

    /**
     * Adds a record to the database using RandomAccessFile to write to the file
     *
     * @param record The record to add
     */
    public void add(@NotNull T record) {
        cache.add(record);

        if (config.recordCount == 0) {
            RandomFileHandler.writeBytes(location, 0, record.toString().getBytes());
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
    private void add(@NotNull T record, long start, long end) {
        long mid = (start + end) / 2;
        int comparison = record.compareTo(get(mid, config.recordLength).substring(0, config.fields[0]));

        if (comparison > 0) {
            add(record, start, mid);
        } else if (comparison < 0) {
            add(record, mid, end);
        } else {
            RandomFileHandler.insertBytes(location, record.toString().getBytes(), mid * config.recordLength, config.recordLength);
        }
    }

    /**
     * Gets a record from the database
     *
     * @param index  The index of the record to get
     * @param length The length of the record to get
     *
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
     * @param compField The record to get
     *
     * @return The record from the database
     */
    public T get(String compField) {
        if (compField == null) {
            return null;
        }

        T cached = cache.get(compField);

        return Objects.requireNonNullElseGet(cached, () -> get(compField, 0, config.recordCount));
    }

    /**
     * Gets a record from the database
     *
     * @param compField The record to get
     * @param start  The start index of the search
     * @param end    The end index of the search
     *
     * @return The record from the database
     */
    @SuppressWarnings("unchecked")
    private @NotNull T get(@NotNull String compField, long start, long end) {
        long mid = (start + end) / 2;
        T midRecord = (T) get(mid, config.recordLength);
        int comparison = midRecord.compareTo(compField);

        if (comparison > 0) {
            return get(compField, start, mid);
        } else if (comparison < 0) {
            return get(compField, mid, end);
        } else {
            return midRecord;
        }
    }

    /**
     * Removes a record from the database
     *
     * @param record The record to remove
     */
    public void remove(T record) {
        cache.remove(record);
        remove(record, 0, config.recordCount);
        config.recordCount--;
    }

    /**
     * Removes a record from the database
     *
     * @param record The record to remove
     * @param start  The start index of the search
     * @param end    The end index of the search
     */
    @SuppressWarnings("unchecked")
    private void remove(@NotNull T record, long start, long end) {
        long mid = (start + end) / 2;
        T midRecord = (T) get(mid, config.recordLength).substring(0, config.fields[0]);
        int comparison = midRecord.compareTo(record);

        if (comparison > 0) {
            remove(record, start, mid);
        } else if (comparison < 0) {
            remove(record, mid, end);
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
     *
     * @param <T> The type of the records
     */
    private static class Cache<T extends Comparable<Object>> {
        /**
         * Stores the records in order
         */
        BinarySearchTree<T> tree;

        /**
         * Stores the records in order of when they were added
         */
        ArrayList<T> list;

        /**
         * Creates a new cache
         */
        public Cache() {
            tree = new BinarySearchTree<T>();
            list = new ArrayList<>();
        }

        /**
         * Adds a record to the cache
         *
         * @param record The record to add
         */
        public void add(@NotNull T record) {
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
        public T get(String record) {
            // TODO: Rework the list so that it stores the records in order of when they were last accessed
            return tree.get(record);
        }

        /**
         * Removes a record from the cache
         *
         * @param record The record to remove
         */
        public void remove(T record) {
            tree.remove(record);
        }
    }
}
