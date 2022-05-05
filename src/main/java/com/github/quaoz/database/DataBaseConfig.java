package com.github.quaoz.database;

import java.util.Map;

public class DataBaseConfig {
    private long recordCount;
    private boolean ordered;
    private boolean cache;
    private Map<String, Integer> fields;

    public boolean isCache() {
        return cache;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public void setFields(Map<String, Integer> fields) {
        this.fields = fields;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public Map<String, Integer> getFields() {
        return fields;
    }
}
