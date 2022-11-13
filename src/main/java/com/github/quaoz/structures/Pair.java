package com.github.quaoz.structures;

import org.jetbrains.annotations.NotNull;

public class Pair<K, V extends Comparable<V>>
	implements Comparable<Pair<K, V>> {

	private K key;
	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Pair{ " + "key = " + key + ", value = " + value + " }";
	}

	@Override
	public int compareTo(@NotNull Pair<K, V> o) {
		return this.getValue().compareTo(o.getValue());
	}
}
