package com.g2forge.alexandria.java.text.escape;

import org.junit.Assert;

import com.g2forge.alexandria.java.text.escape.v2.IEscaper;

public abstract class ATestEscapeType {
	protected abstract IEscaper getEscaper();

	public void test(String escaped, String unescaped) {
		final IEscaper escaper = getEscaper();
		Assert.assertEquals(escaped, escaper.escape(unescaped));
		Assert.assertEquals(unescaped, escaper.unescape(escaped));
	}
}
