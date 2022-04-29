package com.github.quaoz.tests;

import org.jetbrains.annotations.NotNull;

public record RecordTest(String name, int age) implements Comparable<RecordTest> {
    private static final String format = "%-10s %04d";

    @Override
    public String toString() {
        return String.format(format, name, age).concat("\n");
    }

    @Override
    public int compareTo(@NotNull RecordTest o) {
        return name.compareTo(o.name);
    }
}
