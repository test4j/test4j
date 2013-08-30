package org.jtester.datafilling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jtester.datafilling.common.FillingConstants;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.strategy.EmptyStrategy;

@Target(value = { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface FillList {
	/**
	 * The number of elements to create for the collection
	 */
	int size() default FillingConstants.ARRAY_DEFAULT_SIZE;

	/**
	 * The strategy that will populate the annotated attribute.
	 * <p>
	 * The default, in order to make the strategy actually <i>optional</i> is
	 * type Object. At runtime, only if both the value of this annotation and
	 * the collection element type are Objects, then the collection will be set
	 * with type {@link Object}, otherwise the collection element type will win.
	 * </p>
	 * 
	 * @return The strategy that will populate the annotated attribute
	 */
	Class<? extends AttributeStrategy> collectionElementStrategy() default EmptyStrategy.class;

	/**
	 * The strategy that will populate a map key on an attribute of type Map.
	 * <p>
	 * The default, in order to make the strategy actually <i>optional</i> is
	 * type Object. At runtime, only if both the value of this annotation and
	 * the collection element type are Objects, then the collection will be set
	 * with type {@link Object}, otherwise the collection element type will win.
	 * </p>
	 * 
	 * @return The strategy that will populate a map key on an attribute of type
	 *         Map.
	 */
	Class<? extends AttributeStrategy> mapKeyStrategy() default EmptyStrategy.class;

	/**
	 * The strategy that will populate a map element on an attribute of type
	 * Map.
	 * <p>
	 * The default, in order to make the strategy actually <i>optional</i> is
	 * type Object. At runtime, only if both the value of this annotation and
	 * the collection element type are Objects, then the collection will be set
	 * with type {@link Object}, otherwise the collection element type will win.
	 * </p>
	 * 
	 * @return The strategy that will populate a map element on an attribute of
	 *         type Map.
	 */
	Class<? extends AttributeStrategy<?>> mapElementStrategy() default EmptyStrategy.class;

	/** It allows clients to specify a comment on this annotation */
	String comment() default "";
}
