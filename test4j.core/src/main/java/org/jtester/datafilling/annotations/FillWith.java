package org.jtester.datafilling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jtester.datafilling.strategy.AttributeStrategy;

/**
 * Annotation to drive the value of the annotated attribute.
 * <p>
 * If specified, this annotation will take precedence over all other annotations
 * which influence the value of an attribute. In few words, this annotation
 * dictates law in terms of which value the attribute ultimately will get.
 * </p>
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FillWith {

	/**
	 * The strategy that will populate the annotated attribute
	 * 
	 * @return The strategy that will populate the annotated attribute
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends AttributeStrategy> value();

	/**
	 * It allows clients to write a comment on the usage of this annotation
	 * 
	 * @return The comment string
	 */
	String comment() default "";
}
