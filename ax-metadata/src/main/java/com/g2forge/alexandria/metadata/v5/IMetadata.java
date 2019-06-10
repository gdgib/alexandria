package com.g2forge.alexandria.metadata.v5;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.metadata.v5.load.MergedMetadata;
import com.g2forge.alexandria.metadata.v5.load.SimpleMetadata;

public interface IMetadata {
	public static IMetadata merge(Collection<? extends IMetadata> metadatas) {
		return new MergedMetadata(metadatas.stream().map(IAnnotations.class::cast).collect(Collectors.toList()));
	}

	public static IMetadata merge(IMetadata... metadatas) {
		return merge(HCollection.asList(metadatas));
	}

	public static IMetadata of(AnnotatedElement element) {
		return new SimpleMetadata(element);
	}

	public static IMetadata of(AnnotatedElement element, Object value) {
		return new SimpleMetadata(element, value);
	}

	public <T> T getMetadata(Class<T> type);

	public boolean isMetadataPresent(Class<?> type);
}
