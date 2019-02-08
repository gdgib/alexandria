package com.g2forge.alexandria.filesystem.attributes;

import java.nio.file.attribute.FileAttribute;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleFileAttribute<T> implements FileAttribute<T> {
	protected final String name;

	protected final T value;

	@Override
	public String name() {
		return getName();
	}

	@Override
	public T value() {
		return getValue();
	}
}
