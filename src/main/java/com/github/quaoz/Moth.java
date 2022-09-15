package com.github.quaoz;

import org.jetbrains.annotations.NotNull;

public class Moth implements Comparable<Moth> {
	private final String name;
	private final String sciName;
	private final double sizeUpper;
	private final double sizeLower;
	private final int flightStart;
	private final int flightEnd;
	private final String habitat;
	private final String food;

	public Moth(
			String name,
			String sciName,
			double sizeUpper,
			double sizeLower,
			int flightStart,
			int flightEnd,
			String habitat,
			String food) {
		this.name = name;
		this.sciName = sciName;
		this.sizeUpper = sizeUpper;
		this.sizeLower = sizeLower;
		this.flightStart = flightStart;
		this.flightEnd = flightEnd;
		this.habitat = habitat;
		this.food = food;
	}

	public String getName() {
		return name;
	}

	public String getSciName() {
		return sciName;
	}

	public double getSizeUpper() {
		return sizeUpper;
	}

	public double getSizeLower() {
		return sizeLower;
	}

	public int getFlightStart() {
		return flightStart;
	}

	public int getFlightEnd() {
		return flightEnd;
	}

	public String getHabitat() {
		return habitat;
	}

	public String getFood() {
		return food;
	}

	@Override
	public int compareTo(@NotNull Moth o) {
		return this.getName().compareTo(o.getName());
	}
}
