package org.jtester.hamcrest.matcher.property.difference;

import static java.lang.Integer.MAX_VALUE;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for holding the difference between all elements of two collections or
 * arrays.
 */
public class UnorderedCollectionDifference extends Difference {

	/* The differences per left-index and right-index */
	private Map<Integer, Map<Integer, Difference>> elementDifferences = new HashMap<Integer, Map<Integer, Difference>>();

	/* The best matching left and right indexes */
	private Map<Integer, Integer> bestMatchingIndexes = new HashMap<Integer, Integer>();

	/* The matching score of the best matching indexes */
	private int bestMatchingScore = MAX_VALUE;

	/* The left object as a list */
	private List<?> leftList;

	/* The right object as a list */
	private List<?> rightList;

	/**
	 * Creates a difference.
	 * 
	 * @param message
	 *            a message describing the difference
	 * @param leftValue
	 *            the left instance
	 * @param rightValue
	 *            the right instance
	 * @param leftList
	 *            The left instance as a list
	 * @param rightList
	 *            The right instance as a list
	 */
	public UnorderedCollectionDifference(String message, Object leftValue, Object rightValue, List<?> leftList,
			List<?> rightList) {
		super(message, leftValue, rightValue);
		this.leftList = leftList;
		this.rightList = rightList;
	}

	/**
	 * Adds a difference or a match for the elements at the given left and right
	 * index.
	 * 
	 * @param leftIndex
	 *            The index of the left element
	 * @param rightIndex
	 *            The index of the right element
	 * @param difference
	 *            The difference, null for a match
	 */
	public void addElementDifference(int leftIndex, int rightIndex, Difference difference) {
		Map<Integer, Difference> rightDifferences = elementDifferences.get(leftIndex);
		if (rightDifferences == null) {
			rightDifferences = new HashMap<Integer, Difference>();
			elementDifferences.put(leftIndex, rightDifferences);
		}
		rightDifferences.put(rightIndex, difference);
	}

	/**
	 * Gets the difference between the elements with the given indexes.
	 * 
	 * @param leftIndex
	 *            The left element index
	 * @param rightIndex
	 *            The right element index
	 * @return The difference, null if not found or if there is no difference
	 */
	public Difference getElementDifference(int leftIndex, int rightIndex) {
		Map<Integer, Difference> rightDifferences = elementDifferences.get(leftIndex);
		if (rightDifferences == null) {
			return null;
		}
		return rightDifferences.get(rightIndex);
	}

	/**
	 * Gets all element differences per left index and right index. A null
	 * difference means a match.
	 * 
	 * @return The differences, not null
	 */
	public Map<Integer, Map<Integer, Difference>> getElementDifferences() {
		return elementDifferences;
	}

	/**
	 * Sets the given left and right index as best matching elements.
	 * 
	 * @param leftIndex
	 *            The left index
	 * @param rightIndex
	 *            The right index
	 */
	public void setBestMatchingIndexes(int leftIndex, int rightIndex) {
		bestMatchingIndexes.put(leftIndex, rightIndex);
	}

	/**
	 * Gets the indexes of the best matching element differences.
	 * 
	 * @return The indexes, not null
	 */
	public Map<Integer, Integer> getBestMatchingIndexes() {
		return bestMatchingIndexes;
	}

	/**
	 * Gets the matching score of the best matching indexes.
	 * 
	 * @return The score
	 */
	public int getBestMatchingScore() {
		return bestMatchingScore;
	}

	/**
	 * Gets the matching score of the best matching indexes.
	 * 
	 * @param bestMatchingScore
	 *            The score
	 */
	public void setBestMatchingScore(int bestMatchingScore) {
		this.bestMatchingScore = bestMatchingScore;
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