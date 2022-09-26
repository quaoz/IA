package com.github.quaoz;

import org.jetbrains.annotations.NotNull;

public record Moth(
	String name,
	String sciName,
	double sizeUpper,
	double sizeLower,
	int flightStart,
	int flightEnd,
	String habitat,
	String food
)
	implements Comparable<Moth> {
	@Override
	public int compareTo(@NotNull Moth o) {
		return this.name().compareTo(o.name());
	}
}
