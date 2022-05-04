package com.github.quaoz.tests;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// This can be converted into a record but the school computers won't allow the needed java version
public final class RecordTest implements Comparable<RecordTest> {
    private static final String format = "%-10s %04d";
    private final String name;
    private final int age;

    public RecordTest(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format(format, name, age).concat("\n");
    }

    @Override
    public int compareTo(@NotNull RecordTest o) {
        return name.compareTo(o.name);
    }

    public String name() {
        return name;
    }

    public int age() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RecordTest) obj;
        return Objects.equals(this.name, that.name) &&
                this.age == that.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

}
