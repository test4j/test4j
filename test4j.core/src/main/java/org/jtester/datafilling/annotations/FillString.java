package org.jtester.datafilling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jtester.datafilling.common.FillingConstants;

@Target(value = { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FillString {
	/** If specified, it allows clients to specify an exact value for the string */
	String value() default "";

	/**
	 * The length of the String for the annotated attribute. It defaults to
	 * {@link FillingConstants#STR_DEFAULT_LENGTH}
	 */
	int length() default FillingConstants.STR_DEFAULT_LENGTH;

	/** It allows clients to specify a comment on this annotation */
	String comment() default "";
}
