package org.jtester.hamcrest.matcher.property.difference;

/**
 * A class for holding the difference between two objects.
 */
public class Difference {

	/* The left result value */
	private Object leftValue;

	/* The right result value */
	private Object rightValue;

	/* A message describing the difference */
	private String message;

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
	public Difference(String message, Object leftValue, Object rightValue) {
		this.message = message;
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	/**
	 * Gets the left value.
	 * 
	 * @return the value
	 */
	public Object getLeftValue() {
		return leftValue;
	}

	/**
	 * Gets the right value.
	 * 
	 * @return the value
	 */
	public Object getRightValue() {
		return rightValue;
	}

	/**
	 * Gets the message indicating the kind of difference.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
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
	public <T, A> T accept(DifferenceVisitor<T, A> visitor, A argument) {
		return visitor.visit(this, argument);
	}

	@Override
	public String toString() {
		return this.message;
	}
}