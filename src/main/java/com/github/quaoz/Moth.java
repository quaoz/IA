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

	public void setName(String name) {
		this.name = name;
	}

	public void setSciName(String sciName) {
		this.sciName = sciName;
	}

	public void setSizeUpper(int sizeUpper) {
		this.sizeUpper = sizeUpper;
	}

	public void setSizeLower(int sizeLower) {
		this.sizeLower = sizeLower;
	}

	public void setFlightStart(int flightStart) {
		this.flightStart = flightStart;
	}

	public void setFlightEnd(int flightEnd) {
		this.flightEnd = flightEnd;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public String getName() {
		return name;
	}

	public String getSciName() {
		return sciName;
	}

	public int getSizeUpper() {
		return sizeUpper;
	}

	public int getSizeLower() {
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
}
