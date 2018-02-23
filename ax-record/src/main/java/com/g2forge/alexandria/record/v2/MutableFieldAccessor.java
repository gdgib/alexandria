package com.g2forge.alexandria.record.v2;

import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.tuple.ITuple1GS;

import lombok.Data;

@Data
public class MutableFieldAccessor<Record, Field> implements IFieldAccessor<Record, Field> {
	protected final IFieldType<? super Record, ? super Record, Field> type;

	protected final IRecordAccessor<Record> recordAccessor;

	@Override
	public Field get0() {
		final IFunction1<? super Record, ? extends Field> getter = getType().getGetter();
		final Record record = getRecordAccessor().getRecord();
		return getter.apply(record);
	}

	@Override
	public ITuple1GS<Field> set0(Field value) {
		final IConsumer2<? super Record, ? super Field> setter = getType().getSetter();
		final Record record = getRecordAccessor().getRecord();
		setter.accept(record, value);
		return this;
	}

}
