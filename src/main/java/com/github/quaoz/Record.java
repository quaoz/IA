package com.github.quaoz;

public class Record {
	// id 32, species 64, location 32, date 16, size 16, username 64
	public final int id;
	public final String species;
	public final String location;
	public final String date;
	public final Double size;
	public final String user;

	public Record(int id, String species, String location, String date, Double size, String user) {
		this.id = id;
		this.species = species;
		this.location = location;
		this.date = date;
		this.size = size;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public String getSpecies() {
		return species;
	}

	public String getLocation() {
		return location;
	}

	public String getDate() {
		return date;
	}

	public Double getSize() {
		return size;
	}

	public String getUser() {
		return user;
	}
}
