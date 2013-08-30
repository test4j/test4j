package org.jtester.datafilling.common;

import org.jtester.datafilling.strategy.AttributeStrategy;

public class FillingConstants {
	/**
	 * How many times it is allowed to PoJoGen to create an instance of the same
	 * class in a recursive hierarchy
	 */
	public static final int MAX_DEPTH = 1;

	/**
	 * The default string length that PoJoGen will assign to an annotated
	 * attribute
	 */
	public static final int STR_DEFAULT_LENGTH = 10;

	/** The default number of elements for a collection-type element */
	public static final int ARRAY_DEFAULT_SIZE = 1;

	/** The default encoding for Strings */
	public static final String STR_DEFAULT_ENCODING = "UTF-8";

	/** The name of the {@link AttributeStrategy} interface */
	public static final String PoJoGen_ATTRIBUTE_STRATEGY_METHOD_NAME = "getValue";

	private FillingConstants() {
		throw new AssertionError();
	}
}
