package org.jtester.hamcrest.matcher.property.reflection;

/**
 * Modes defining how to compare two values. No mode means strict comparison.
 */
public enum EqMode {

	/**
	 * Ignore fields that do not have a default value for the left-hand
	 * (expected) side
	 */
	IGNORE_DEFAULTS,

	/**
	 * Do not compare the actual time/date value.<br>
	 * just that both left-hand (expected) and right-hand side are null or not
	 * null.
	 */
	IGNORE_DATES,

	/**
	 * Do not compare the order of collections and arrays.<br>
	 * Only check that all values of the left-hand (expected) side collection or
	 * array are also contained in the right-hand (actual) side and vice versa.
	 */
	IGNORE_ORDER,

	/**
	 * 
	 */
	EQ_STRING;
}
