package org.jtester.datafilling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FillDouble {
	String value() default "";

	double min() default 0.0;

	double max() default 0.0;

	String comment() default "";
}
