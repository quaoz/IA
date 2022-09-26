package com.github.quaoz;

/**
 * @param id id 32, species 64, location 32, date 16, size 16, username 64
 */
public record Record(
	int id,
	String species,
	String location,
	String date,
	Double size,
	String user
) {}
