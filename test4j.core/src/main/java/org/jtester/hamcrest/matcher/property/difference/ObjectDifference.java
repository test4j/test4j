package org.jtester.hamcrest.matcher.property.difference;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for holding the difference between two objects.
 */
public class ObjectDifference extends Difference {

	/* The differences per field name */
	private Map<String, Difference> fieldDifferences = new HashMap<String, Difference>();

	/**
	 * Creates a difference.
	 * 
	 * @param message
	 *            a message describing the difference
	 * @param leftValue
	 *            the left instance
	 * @param rightValue
	 *            the right instance
	 */
	public ObjectDifference(String message, Object leftValue, Object rightValue) {
		super(message, leftValue, rightValue);
	}

	/**
	 * Adds a difference for the field with the given name.
	 * 
	 * @param fieldName
	 *            The field name, not null
	 * @param difference
	 *            The difference, not null
	 */
	public void addFieldDifference(String fieldName, Difference difference) {
		fieldDifferences.put(fieldName, difference);
	}

	/**
	 * Gets all differences per field name.
	 * 
	 * @return The differences, not null
	 */
	public Map<String, Difference> getFieldDifferences() {
		return fieldDifferences;
	}

	/**
	 * Double dispatch method. Dispatches back to the given visitor.
	 * <p/>
	 * All subclasses should copy this method in their own class body.
	 * 
	 * @param visitor
	 *            The visitor, not null
	 * @param argument
	 *            An optional argument for the visitor, null if not applicable
	 * @return The result
	 */
	@Override
	public <T, A> T accept(DifferenceVisitor<T, A> visitor, A argument) {
		return visitor.visit(this, argument);
	}

}