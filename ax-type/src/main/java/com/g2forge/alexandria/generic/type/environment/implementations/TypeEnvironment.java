package com.g2forge.alexandria.generic.type.environment.implementations;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.IVariableType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;

public class TypeEnvironment implements ITypeEnvironment {
	protected static final Predicate<Object> PARENT_PREDICATE = ((Predicate<Object>)CollectionHelpers.asSet(null, EmptyTypeEnvironment.create())::contains).negate();

	public static ITypeEnvironment create(final Collection<ITypeEnvironment> parents) {
		if (parents.isEmpty()) return null;

		final Collection<ITypeEnvironment> nonNull = CollectionHelpers.filter(parents, PARENT_PREDICATE);
		if (nonNull.isEmpty()) return EmptyTypeEnvironment.create();
		if (nonNull.size() == 1) return CollectionHelpers.getAny(nonNull);
		return new TypeEnvironment(null, nonNull);
	}

	public static ITypeEnvironment create(final ITypeEnvironment... parents) {
		return create(CollectionHelpers.asList(parents));
	}

	public static ITypeEnvironment create(final ITypeEnvironment parent) {
		return parent;
	}

	protected final Map<IVariableType, IType> map;

	protected final Collection<ITypeEnvironment> parents;

	public TypeEnvironment(final Map<IVariableType, IType> map, final Collection<ITypeEnvironment> parents) {
		this.map = map;
		this.parents = CollectionHelpers.filter(parents, PARENT_PREDICATE);
	}

	public TypeEnvironment(final Map<IVariableType, IType> map, final ITypeEnvironment... parents) {
		this(map, CollectionHelpers.asList(parents));
	}

	@Override
	public IType apply(final IVariableType input) {
		if ((map != null) && map.containsKey(input)) return map.get(input);
		if (parents != null) for (final ITypeEnvironment parent : parents)
			return parent.apply(input);
		return null;
	}
}
