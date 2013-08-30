package org.jtester.hamcrest.matcher.property.difference;

@SuppressWarnings({ "rawtypes" })
public class ClassDifference extends Difference {

	private Class leftClass;
	private Class rightClass;

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
	public ClassDifference(String message, Object leftValue, Object rightValue, Class leftClass, Class rightClass) {
		super(message, leftValue, rightValue);
		this.leftClass = leftClass;
		this.rightClass = rightClass;
	}

	public Class getLeftClass() {
		return leftClass;
	}

	public Class getRightClass() {
		return rightClass;
	}

	@Override
	public <T, A> T accept(DifferenceVisitor<T, A> visitor, A argument) {
		return visitor.visit(this, argument);
	}
}
