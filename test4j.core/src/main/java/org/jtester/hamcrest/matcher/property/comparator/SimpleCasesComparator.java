package org.jtester.hamcrest.matcher.property.comparator;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.jtester.tools.commons.DateHelper;

/**
 * Comparator for simple cases. Following cases are handled: left and right are
 * the same instance, left or right have a null value, left or right are
 * enumerations, left or right are java.lang classes and left or right are of
 * type Character or Number
 */
public class SimpleCasesComparator implements Comparator {

	/**
	 * Returns true if both object are the same instance, have a null value, are
	 * an Enum/Number/Character or are a java.lang type.
	 * 
	 * @param left
	 *            The left object
	 * @param right
	 *            The right object
	 * @return True for simple cases
	 */
	public boolean canCompare(Object left, Object right) {
		if (left == right) {
			return true;
		}
		if (left == null || right == null) {
			return true;
		}
		if ((left instanceof Character || left instanceof Number)
				&& (right instanceof Character || right instanceof Number)) {
			return true;
		}
		if (left.getClass().getName().startsWith("java.lang") || right.getClass().getName().startsWith("java.lang")) {
			return true;
		}
		if (left instanceof Enum && right instanceof Enum) {
			return true;
		}
		return false;
	}

	/**
	 * Compares the given values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param onlyFirstDifference
	 *            True if only the first difference should be returned
	 * @param reflectionComparator
	 *            The root comparator for inner comparisons, not null
	 * @return A Difference if both values are different, null otherwise
	 */
	public Difference compare(Object left, Object right, boolean onlyFirstDifference,
			ReflectionComparator reflectionComparator) {
		// check if the same instance is referenced
		if (left == right) {
			return null;
		}
		// check if the left value is null
		if (left == null) {
			return new Difference("Left value null", left, right);
		}
		// check if the right value is null
		if (right == null) {
			return new Difference("Right value null", left, right);
		}
		// check if right and left have same number value (including NaN and
		// Infinity)
		if ((left instanceof Character || left instanceof Number)
				&& (right instanceof Character || right instanceof Number)) {
			Double leftDouble = getDoubleValue(left);
			Double rightDouble = getDoubleValue(right);
			if (leftDouble.equals(rightDouble)) {
				return null;
			}
			return new Difference("Different primitive values", left, right);
		}
		if (left instanceof String && right instanceof Date) {
			SimpleDateFormat df = DateHelper.getDateFormat((String) left);
			right = df.format((Date) right);
		}
		// check if java objects are equal
		if (left.getClass().getName().startsWith("java.lang") || right.getClass().getName().startsWith("java.lang")) {
			if (left.equals(right)) {
				return null;
			}
			return new Difference("Different object values", left, right);
		}
		// check if enums are equal
		if (left instanceof Enum && right instanceof Enum) {
			if (left.equals(right)) {
				return null;
			}
			return new Difference("Different enum values", left, right);
		}
		return null;
	}

	/**
	 * Gets the double value for the given left Character or Number instance.
	 * 
	 * @param object
	 *            the Character or Number, not null
	 * @return the value as a Double (this way NaN and infinity can be compared)
	 */
	private Double getDoubleValue(Object object) {
		if (object instanceof Number) {
			return ((Number) object).doubleValue();
		}
		return (double) ((Character) object).charValue();
	}

}
