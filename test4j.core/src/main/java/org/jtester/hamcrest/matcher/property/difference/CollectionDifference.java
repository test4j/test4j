package org.jtester.hamcrest.matcher.property.difference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for holding the difference between two collections or arrays.
 */
public class CollectionDifference extends Difference {

	/* The differences per index */
	private Map<Integer, Difference> elementDifferences = new HashMap<Integer, Difference>();

	/*
	 * The indexes of the left elements that were missing in the right
	 * collection
	 */
	private List<Integer> leftMissingIndexes = new ArrayList<Integer>();

	/*
	 * The indexes of the right elements that were missing in the left
	 * collection
	 */
	private List<Integer> rightMissingIndexes = new ArrayList<Integer>();

	/* The left object as a list */
	private List<?> leftList;

	/* The right object as a list */
	private List<?> rightList;

	/**
	 * Creates a difference.
	 * 
	 * @param message
	 *            A message describing the difference
	 * @param leftValue
	 *            The left instance
	 * @param rightValue
	 *            The right instance
	 * @param leftList
	 *            The left instance as a list
	 * @param rightList
	 *            The right instance as a list
	 */
	public CollectionDifference(String message, Object leftValue, Object rightValue, List<?> leftList, List<?> rightList) {
		super(message, leftValue, rightValue);
		this.leftList = leftList;
		this.rightList = rightList;
	}

	/**
	 * Adds a difference for the element at the given index.
	 * 
	 * @param index
	 *            The element index
	 * @param difference
	 *            The difference, not null
	 */
	public void addElementDifference(int index, Difference difference) {
		elementDifferences.put(index, difference);
	}

	/**
	 * Gets all element differences per index.
	 * 
	 * @return The differences, not null
	 */
	public Map<Integer, Difference> getElementDifferences() {
		return elementDifferences;
	}

	/**
	 * Adds an index of a left element that is missing in the right collection.
	 * 
	 * @param index
	 *            The left element index
	 */
	public void addLeftMissingIndex(int index) {
		leftMissingIndexes.add(index);
	}

	/**
	 * Gets the indexes of the left elements that were missing in the right
	 * collection.
	 * 
	 * @return The indexes, not null
	 */
	public List<Integer> getLeftMissingIndexes() {
		return leftMissingIndexes;
	}

	/**
	 * Adds an index of a right element that is missing in the left collection.
	 * 
	 * @param index
	 *            The right element index
	 */
	public void addRightMissingIndex(int index) {
		rightMissingIndexes.add(index);
	}

	/**
	 * Gets the indexes of the right elements that were missing in the left
	 * collection.
	 * 
	 * @return The indexes, not null
	 */
	public List<Integer> getRightMissingIndexes() {
		return rightMissingIndexes;
	}

	/**
	 * @return The left instance as a list
	 */
	public List<?> getLeftList() {
		return leftList;
	}

	/**
	 * @return The right instance as a list
	 */
	public List<?> getRightList() {
		return rightList;
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