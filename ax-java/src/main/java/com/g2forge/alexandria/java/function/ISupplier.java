package com.g2forge.alexandria.java.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface ISupplier<T> extends Supplier<T> {
	public static <T> ISupplier<T> create(T value) {
		return new LiteralSupplier<>(value);
	}

	public default <I> IFunction1<I, T> toFunction() {
		return t -> get();
	}
}
