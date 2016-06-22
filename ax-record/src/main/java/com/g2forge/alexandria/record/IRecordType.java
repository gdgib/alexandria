package com.g2forge.alexandria.record;

import java.util.Collection;

import com.g2forge.alexandria.java.core.iface.IFactory;
import com.g2forge.alexandria.java.typed.IGeneric;

public interface IRecordType<R> extends IGeneric<R>, IFactory<R> {
	public Collection<? extends IFieldType<R, ?>> getFields();
}
