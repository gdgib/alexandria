package com.g2forge.alexandria.adt.collection.collector.implementations;

import java.util.Collection;

import com.g2forge.alexandria.adt.collection.collector.ICollectionBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FixedCollectionBuilder<C, T> implements ICollectionBuilder<C, T> {
	protected final Collection<T> collectionActual;
	
	protected final C collectionReturn;

	@Override
	public ICollectionBuilder<C, T> add(Iterable<? extends T> values) {
		for (T value : values) {
			collectionActual.add(value);
		}
		return this;
	}

	@Override
	public ICollectionBuilder<C, T> add(T value) {
		collectionActual.add(value);
		return this;
	}

	@Override
	public ICollectionBuilder<C, T> add(@SuppressWarnings("unchecked") T... values) {
		for (T value : values) {
			collectionActual.add(value);
		}
		return this;
	}

	@Override
	public C create() {
		return collectionReturn;
	}
}
