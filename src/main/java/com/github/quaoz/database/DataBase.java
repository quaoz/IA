package com.github.quaoz.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quaoz.structures.BinarySearchTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

// TODO: fuzzy search?
public class DataBase implements Closeable {
    private final Cache cache;
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

        this.cache = new Cache();
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

        this.cache = new Cache();
    }

    /**
     * Adds a record to the database using RandomAccessFile to write to the file
     *
     * @param record The record to add
     */
    public void add(@NotNull String record) {
        // cache.add(record);

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
                get(mid, config.recordLength).substring(0, config.fields[0]));

        if (end - start == 1) {
            if (comparison < 0) {
                if (mid == 0) {
                    try (RandomAccessFile randomAccessFile = new RandomAccessFile(location, "rws")) {
                        long index = randomAccessFile.length() - config.recordLength;
                        byte[] line = new byte[config.recordLength];

                        // Seek to the last line in the file
                        randomAccessFile.seek(index);

                        long pos = 0;

                        while (index >= pos) {
                            // Copy the current line one place forwards
                            randomAccessFile.read(line, 0, config.recordLength);
                            randomAccessFile.seek(index + config.recordLength);
                            randomAccessFile.write(line);

                            // Seek back
                            index -= config.recordLength;
                            randomAccessFile.seek(index > 0 ? index : 0);
                        }

                        // Insert the line
                        randomAccessFile.seek(index + config.recordLength);
                        randomAccessFile.write(record.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        System.err.println("L get good");
                        throw new RuntimeException(e);
                    }
                } else {
                    RandomFileHandler.insertBytes(location, record.getBytes(StandardCharsets.UTF_8), mid * config.recordLength, config.recordLength);
                }
            } else {
                RandomFileHandler.insertBytes(location, record.getBytes(StandardCharsets.UTF_8), mid * config.recordLength, config.recordLength);
            }
        } else if (comparison < 0) {
            add(record, start, mid);
        } else if (comparison > 0) {
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

        return cached == null ? get(field, 0, config.recordCount, compField) : cached;
    }

    /**
     * Gets a record from the database
     *
     * @param compField The record to get
     * @param start     The start index of the search
     * @param end       The end index of the search
     * @return The record from the database
     */
    private @Nullable String get(@NotNull String field, long start, long end, int compField) {
        long mid = (start + end) / 2;
        String midRecord = get(mid, config.recordLength);

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
        String midRecord = get(mid, config.recordLength);
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
