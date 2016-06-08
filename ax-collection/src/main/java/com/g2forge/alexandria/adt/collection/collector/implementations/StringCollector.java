package com.g2forge.alexandria.adt.collection.collector.implementations;

import java.util.Objects;

import com.g2forge.alexandria.adt.collection.collector.ICollector;

public class StringCollector<T> implements ICollector<T> {
	protected final StringBuilder internal = new StringBuilder();
	
	@Override
	public ICollector<T> add(final Iterable<? extends T> values) {
		for (final T value : values) {
			add(value);
		}
		return this;
	}
	
	@Override
	public ICollector<T> add(final T value) {
		internal.append(Objects.toString(value));
		return this;
	}
	
	@Override
	public ICollector<T> add(@SuppressWarnings("unchecked") final T... values) {
		for (final T value : values) {
			add(value);
		}
		return this;
	}
	
	@Override
	public String toString() {
		return internal.toString();
	}
}
