package com.github.quaoz.tests;

import org.jetbrains.annotations.NotNull;

public class TestRecord implements Comparable<Object> {
    private final String name;
    private final String value;

    public TestRecord(@NotNull String name, @NotNull String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof TestRecord) {
            return name.compareTo(((TestRecord) o).getName());
        } else if (o instanceof String) {
            return name.compareTo((String) o);
        } else {
            return 0;
        }
    }
}
