package com.github.quaoz;

public class Moth {
	private String name;
	private String sciName;
	private int sizeUpper;
	private int sizeLower;
	private int flightStart;
	private int flightEnd;
	private String habitat;
	private String food;

	public Moth(String name, String sciName, int sizeUpper, int sizeLower, int flightStart, int flightEnd, String habitat, String food) {
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

	public void setName(String name) {
		this.name = name;
	}

	public String getSciName() {
		return sciName;
	}

	public void setSciName(String sciName) {
		this.sciName = sciName;
	}

	public int getSizeUpper() {
		return sizeUpper;
	}

	public void setSizeUpper(int sizeUpper) {
		this.sizeUpper = sizeUpper;
	}

	public int getSizeLower() {
		return sizeLower;
	}

	public void setSizeLower(int sizeLower) {
		this.sizeLower = sizeLower;
	}

	public int getFlightStart() {
		return flightStart;
	}

	public void setFlightStart(int flightStart) {
		this.flightStart = flightStart;
	}

	public int getFlightEnd() {
		return flightEnd;
	}

	public void setFlightEnd(int flightEnd) {
		this.flightEnd = flightEnd;
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}
}
