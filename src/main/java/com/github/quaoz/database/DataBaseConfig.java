package com.github.quaoz.database;

public class DataBaseConfig {
    public long recordCount = 0;
    public Integer recordLength = 10;
    public Integer[] fields;

    public DataBaseConfig(Integer recordLength, Integer[] fields) {
        this.recordLength = recordLength;
        this.fields = fields;
    }
}
