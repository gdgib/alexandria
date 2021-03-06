package com.g2forge.alexandria.java.adt.tuple;

public interface ITuple2GS<T0, T1> extends ITuple2G_<T0, T1>, ITuple2_S<T0, T1>, ITuple1GS<T0> {
	@Override
	public ITuple2GS<T0, T1> set0(T0 value);

	@Override
	public ITuple2GS<T0, T1> set1(T1 value);

	public default T1 swap1(final T1 value) {
		final T1 retVal = get1();
		set1(value);
		return retVal;
	}
}
