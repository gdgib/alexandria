package com.g2forge.alexandria.java.concurrent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConcurrentHelpers {
	private static <I, O> O internal(final Function<I, O> function, final I input, final Object[] locks, int offset) {
		if (offset == locks.length - 1) return sync(function, input, locks[offset]);
		else if (locks[offset] == null) return internal(function, input, locks, offset + 1);
		else synchronized (locks[offset]) {
			return internal(function, input, locks, offset + 1);
		}
	}

	public static <I> void sync(final Consumer<I> consumer, final I input, final Object lock) {
		if (lock == null) consumer.accept(input);
		else synchronized (lock) {
			consumer.accept(input);
		}
	}

	public static <I, O> O sync(final Function<I, O> function, final I input, final Object lock) {
		if (lock == null) return function.apply(input);
		else synchronized (lock) {
			return function.apply(input);
		}
	}

	public static <I, O> O sync(final Function<I, O> function, final I input, final Object... locks) {
		if ((locks == null) || (locks.length < 1)) return function.apply(input);
		else return internal(function, input, locks, 0);
	}

	public static void sync(final Runnable runnable, final Object lock) {
		if (lock == null) runnable.run();
		else synchronized (lock) {
			runnable.run();
		}
	}

	public static <O> O sync(final Supplier<O> supplier, final Object lock) {
		if (lock == null) return supplier.get();
		else synchronized (lock) {
			return supplier.get();
		}
	}
}
