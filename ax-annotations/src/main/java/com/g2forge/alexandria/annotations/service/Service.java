package com.g2forge.alexandria.annotations.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.g2forge.alexandria.annotations.Handler;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
@Handler(ServiceAnnotationHandler.class)
public @interface Service {
	/**
	 * The service interfaces which the annotated type supports.
	 */
	public Class<?>[] value();
}
