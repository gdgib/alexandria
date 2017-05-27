package com.g2forge.alexandria.java.typed;

import java.lang.reflect.Type;

/**
 * Captures the relationship between the static type <code>T</code> and the runtime type {@link #getType()}. Due to the lack of proper generic support in the Java reflection
 * library, this interface is currently not statically type safe. As such implementors are responsible for type safety.
 *
 * @param <T>
 *            The (static) type of this type reference.
 */
@FunctionalInterface
public interface ITypeRef<T> {
	/**
	 * Construct an instance of {@link ITypeRef} for a Java {@link Class}. This is most useful for non-parameterized classes.
	 * 
	 * @param type
	 *            The class to create a reference to.
	 * @return An instance of {@link ITypeRef} whose {@link #getType()} returns <code>type</code>.
	 */
	public static <T> ITypeRef<T> of(Class<T> type) {
		return () -> type;
	}

	/**
	 * Get the dynamic (runtime) type of <code>T</code>.
	 * 
	 * @return The dynamic (runtime) type of <code>T</code>.
	 */
	public Type getType();
}
