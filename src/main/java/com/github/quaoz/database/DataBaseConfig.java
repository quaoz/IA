package com.github.quaoz.database;

public class DataBaseConfig {

	public long recordCount = 0;
	public Integer recordLength = 10;
	public Integer[] fields;

	public DataBaseConfig init(Integer recordLength, Integer[] fields) {
		this.recordLength = recordLength;
		this.fields = fields;
		return this;
	}
}
