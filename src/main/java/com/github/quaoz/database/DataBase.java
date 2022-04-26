package com.github.quaoz.database;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class DataBase<T extends Comparable<T>> {
    private final RecordHandler<T> recordHandler;
    private final Cache<T> cache;
    private final File location;
    private long recordCount;

    /**
     * Creates a new database or loads an existing one
     *
     * @param location      The location of the database
     * @param recordHandler The record handler to use
     *
     * @throws IllegalArgumentException If the location is {@code null}
     * @throws IllegalArgumentException If the location is a directory
     * @throws IllegalArgumentException If the location is not readable
     * @throws IllegalArgumentException If the location is not writable
     * @throws IllegalArgumentException If the database cannot be created
     */
    public DataBase(File location, RecordHandler<T> recordHandler) throws IllegalArgumentException {
        if (recordHandler == null) {
            throw new IllegalArgumentException("Record length cannot be 0");
        } else {
            this.recordHandler = recordHandler;
        }

        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        } else if (location.isDirectory()) {
            throw new IllegalArgumentException("location is a directory");
        } else if (!location.canRead()) {
            throw new IllegalArgumentException("location cannot be read from");
        } else if (!location.canWrite()) {
            throw new IllegalArgumentException("location cannot be written to");
        } else if (!location.exists()) {
            // try to create a new file at location and throw an exception if it fails
            try {
                location.createNewFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("location cannot be created");
            }

            this.location = location;
            updateRecordCount();
        } else {
            this.location = location;
            recordCount = 0;
        }

        this.cache = new Cache<>();
    }

    /**
     * Adds a record to the database using RandomAccessFile to write to the file
     */
    public void add(@NotNull T record) {
        cache.add(record);

        if (recordCount == 0) {
            RandomFileHandler.writeBytes(location, 0, record.toString().getBytes());
            recordCount++;
        } else {
            add(record, 0, recordCount);
        }
    }

    /**
     * Gets the number of records in the database
     *
     * @return The number of records in the database
     */
    public long getRecordCount() {
        return recordCount;
    }

    /**
     * Updates the record count of the database.
     */
    private void updateRecordCount() {
        try (Stream<String> stream = Files.lines(location.toPath())) {
            recordCount = stream.count();
        } catch (IOException e) {
            System.err.printf("Unable to access the file at %s", location);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a record to the database using RandomAccessFile to write to the file
     */
    @SuppressWarnings("unchecked")
    private void add(@NotNull T record, long start, long end) {
        long mid = (start + end) / 2;
        int comparison = record.compareTo((T) (get(mid, recordHandler.recordLength())));

        if (comparison > 0) {
            add(record, start, mid);
        } else if (comparison < 0) {
            add(record, mid, end);
        } else {
            RandomFileHandler.insertBytes(location, record.toString().getBytes(), mid * recordHandler.recordLength(), recordHandler.recordLength());
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
        if (index < 0 || index >= recordCount) {
            return null;
        }

        return new String(RandomFileHandler.readBytes(location, index * length, length));
    }

    /**
     * Gets a record from the database
     *
     * @param record The record to get
     *
     * @return The record from the database
     */
    public T get(T record) {
        if (record == null) {
            return null;
        }

        T cached = cache.get(record);

        return Objects.requireNonNullElseGet(cached, () -> get(record, 0, recordCount));
    }

    /**
     * Gets a record from the database
     *
     * @param record The record to get
     * @param start  The start index of the search
     * @param end    The end index of the search
     *
     * @return The record from the database
     */
    private @NotNull T get(@NotNull T record, long start, long end) {
        long mid = (start + end) / 2;
        T midRecord = recordHandler.getRecord(get(mid, recordHandler.recordLength()));
        int comparison = midRecord.compareTo(record);

        if (comparison > 0) {
            return get(record, start, mid);
        } else if (comparison < 0) {
            return get(record, mid, end);
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
        remove(record, 0, recordCount);
        recordCount--;
    }

    /**
     * Removes a record from the database
     *
     * @param record The record to remove
     * @param start  The start index of the search
     * @param end    The end index of the search
     */
    private void remove(@NotNull T record, long start, long end) {
        long mid = (start + end) / 2;
        T midRecord = recordHandler.getRecord(get(mid, recordHandler.recordLength()));
        int comparison = midRecord.compareTo(record);

        if (comparison > 0) {
            remove(record, start, mid);
        } else if (comparison < 0) {
            remove(record, mid, end);
        } else {
            RandomFileHandler.deleteLine(location, mid * recordHandler.recordLength(), recordHandler.recordLength());
        }
    }

    /**
     * The database cache
     *
     * @param <T> The type of the records
     */
    private static class Cache<T extends Comparable<T>> {
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
            tree = new BinarySearchTree<>();
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
        public T get(T record) {
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
