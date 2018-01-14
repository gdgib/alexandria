package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFunction2<I0, I1, O> extends BiFunction<I0, I1, O>, IFunction<O> {
	public static <I0, I1, O> IFunction2<I0, I1, O> create(IFunction2<I0, I1, O> function) {
		return function;
	}

	public default IFunction1<I1, O> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return i1 -> apply(before.get(), i1);
	}

	public default IFunction1<I0, O> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return i0 -> apply(i0, before.get());
	}

	public default IFunction1<I1, O> compute0(IFunction1<? super I1, ? extends I0> i0) {
		return i1 -> this.apply(i0.apply(i1), i1);
	}

	public default IFunction1<I0, O> compute1(IFunction1<? super I0, ? extends I1> i1) {
		return i0 -> this.apply(i0, i1.apply(i0));
	}

	public default IFunction1<I1, O> curry0(I0 input0) {
		return input1 -> apply(input0, input1);
	}

	public default IFunction1<I0, O> curry1(I1 input1) {
		return input0 -> apply(input0, input1);
	}

	public default IConsumer2<I0, I1> noReturn() {
		return (i0, i1) -> apply(i0, i1);
	}
}
