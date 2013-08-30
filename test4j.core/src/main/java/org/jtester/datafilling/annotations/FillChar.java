package org.jtester.datafilling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to customise min and max values of a char type attribute or
 * constructor parameter.
 */
@Target(value = { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FillChar {
	char value() default ' ';

	char min() default 0;

	char max() default 0;

	String comment() default "";
}
