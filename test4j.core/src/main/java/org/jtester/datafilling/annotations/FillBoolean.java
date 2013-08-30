package org.jtester.datafilling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to customise a boolean value on the annotated field
 */
@Target(value = { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FillBoolean {
	/**
	 * The value to assign to the annotated attribute.
	 * 
	 * @return The value to assign to the annotated attribute
	 */
	boolean value();
}
