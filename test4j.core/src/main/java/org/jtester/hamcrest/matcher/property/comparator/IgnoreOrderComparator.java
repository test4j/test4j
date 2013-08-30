package org.jtester.hamcrest.matcher.property.comparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.difference.UnorderedCollectionDifference;
import org.jtester.hamcrest.matcher.property.reflection.MatchingScoreCalculator;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.jtester.tools.commons.ListHelper;

/**
 * A comparator for collections and arrays that ignores the order of both
 * collections. Both collections are found equal if they both contain the same
 * elements (in any order). This implements the LENIENT_ORDER comparison mode.
 */
public class IgnoreOrderComparator implements Comparator {

	/**
	 * Returns true if both objects are not null and are both Arrays or
	 * Collections.
	 * 
	 * @param left
	 *            The left object
	 * @param right
	 *            The right object
	 * @return True for Arrays and Collections
	 */
	public boolean canCompare(Object left, Object right) {
		if (left == null || right == null) {
			return false;
		}
		if ((left.getClass().isArray() || left instanceof Collection)
				&& (right.getClass().isArray() || right instanceof Collection)) {
			return true;
		}
		return false;
	}

	/**
	 * Compares the given collections/arrays but ignoring the actual order of
	 * the elements. This will first try to find a sequence that is an exact
	 * match. If no such sequence can be found, the difference of all elements
	 * with all other elements are calculated one by one.
	 * 
	 * @param left
	 *            The left array/collection, not null
	 * @param right
	 *            The right array/collection, not null
	 * @param onlyFirstDifference
	 *            True if only the first difference should be returned
	 * @param reflectionComparator
	 *            The root comparator for inner comparisons, not null
	 * @return An UnorderedCollectionDifference or null if both collections are
	 *         equal
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Difference compare(Object left, Object right, boolean onlyFirstDifference,
			ReflectionComparator reflectionComparator) {
		// Convert to list and compare as collection
		Collection left_coll = ListHelper.toList(left);
		ArrayList leftList = new ArrayList(left_coll);
		Collection right_coll = ListHelper.toList(right);
		ArrayList rightList = new ArrayList(right_coll);

		// check whether a combination exists
		boolean isEqual = isEqual(leftList, rightList, 0, reflectionComparator);
		if (isEqual) {
			// found a match
			return null;
		}

		// no match found, determine all differences
		UnorderedCollectionDifference difference = new UnorderedCollectionDifference(
				"Collections/arrays are different", left, right, leftList, rightList);
		if (onlyFirstDifference) {
			return difference;
		}
		fillAllDifferences(leftList, rightList, reflectionComparator, difference);
		fillBestMatchingIndexes(leftList, rightList, difference);
		return difference;
	}

	/**
	 * Recursively checks whether there is a sequence so that both collections
	 * have matching elements. This will loop over the elements of the left list
	 * and then try to find a match for these elements in the right list. If a
	 * match is found, the element is removed from the right collection and the
	 * comparison is recursively performed again on the remaining elements.
	 * <p/>
	 * NOTE: because difference are cached in the reflection comparator,
	 * comparing two elements that were already compared should be very fast.
	 * 
	 * @param leftList
	 *            The left list, not null
	 * @param rightList
	 *            The right list, not null
	 * @param leftIndex
	 *            The current index in the left collection
	 * @param reflectionComparator
	 *            reflectionComparator The comparator for the element
	 *            comparisons, not null
	 * @return True if a match is found
	 */
	@SuppressWarnings({ "unchecked" })
	protected boolean isEqual(ArrayList<Object> leftList, ArrayList<Object> rightList, int leftIndex,
			ReflectionComparator reflectionComparator) {
		if (leftIndex >= leftList.size()) {
			// end of the recursion
			// if there are no more elements left in the right and left
			// collections, a match is found
			return (rightList.isEmpty());
		}

		Object leftValue = leftList.get(leftIndex);
		for (int rightIndex = 0; rightIndex < rightList.size(); rightIndex++) {
			Object rightValue = rightList.get(rightIndex);

			Difference elementDifference = reflectionComparator.getDifference(leftValue, rightValue, true);
			if (elementDifference != null) {
				// elements are not matching
				continue;
			}

			// match found, try to find a match for the remaining elements
			ArrayList<Object> rightListClone = (ArrayList<Object>) rightList.clone();
			rightListClone.remove(rightIndex);

			boolean isEqual = isEqual(leftList, rightListClone, leftIndex + 1, reflectionComparator);
			if (isEqual) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculates the difference of all elements in the left list with all
	 * elements of the right list. The result is added to the given difference.
	 * <p/>
	 * NOTE: because difference are cached in the reflection comparator,
	 * comparing two elements that were already compared should be very fast.
	 * 
	 * @param leftList
	 *            The left list, not null
	 * @param rightList
	 *            The right list, not null
	 * @param reflectionComparator
	 *            The comparator for element comparisons, not null
	 * @param difference
	 *            The root difference to which all differences will be added,
	 *            not null
	 */
	protected void fillAllDifferences(ArrayList<Object> leftList, ArrayList<Object> rightList,
			ReflectionComparator reflectionComparator, UnorderedCollectionDifference difference) {
		// loops over all left and right elements to calculate the differences
		for (int leftIndex = 0; leftIndex < leftList.size(); leftIndex++) {
			Object leftValue = leftList.get(leftIndex);
			for (int rightIndex = 0; rightIndex < rightList.size(); rightIndex++) {
				Object rightValue = rightList.get(rightIndex);
				Difference elementDifference = reflectionComparator.getDifference(leftValue, rightValue, false);
				difference.addElementDifference(leftIndex, rightIndex, elementDifference);
			}
		}
	}

	/**
	 * Calculates the indexes of the best matching differences for the given
	 * unordered collection difference. The resulting indexes are set on the
	 * given difference.
	 * <p/>
	 * Note: The unordered collection difference should contain the differences
	 * of all left-elements with all right-elements.
	 * 
	 * @param leftList
	 *            The left list, not null
	 * @param rightList
	 *            The right list, not null
	 * @param difference
	 *            The difference to which all indexes will be added, not null
	 */
	protected void fillBestMatchingIndexes(ArrayList<Object> leftList, ArrayList<Object> rightList,
			UnorderedCollectionDifference difference) {
		ArrayList<Integer> leftIndexes = createIndexList(leftList.size());
		ArrayList<Integer> rightIndexes = createIndexList(rightList.size());
		removeMatchingIndexes(leftIndexes, rightIndexes, difference);
		setBestMatchingIndexes(leftIndexes, rightIndexes, difference);
	}

	/**
	 * Actual implementation of the best match finding algorithm. This will loop
	 * over the different elements in the collections to the match with the
	 * lowest total matching score. These indexes are then set on the given
	 * difference. The matching scores are determined by the given
	 * matchingScoreCalculator.
	 * 
	 * @param leftIndexes
	 *            The current remaining indexes in the left collection, not null
	 * @param rightIndexes
	 *            The current remaining indexes in the right collection, not
	 *            null
	 * @param difference
	 *            The difference to which all indexes will be added, not null
	 */
	protected void setBestMatchingIndexes(ArrayList<Integer> leftIndexes, ArrayList<Integer> rightIndexes,
			UnorderedCollectionDifference difference) {
		MatchingScoreCalculator matchingScoreCalculator = createMatchingScoreCalculator();
		Map<Integer, Map<Integer, Difference>> differences = difference.getElementDifferences();

		for (Integer leftIndex : leftIndexes) {
			int score = Integer.MAX_VALUE;
			for (Integer rightIndex : rightIndexes) {
				Difference elementDifference = differences.get(leftIndex).get(rightIndex);

				int matchingScore = matchingScoreCalculator.calculateMatchingScore(elementDifference);
				if (matchingScore < score) {
					score = matchingScore;
					difference.setBestMatchingIndexes(leftIndex, rightIndex);
				}
			}
		}
	}

	/**
	 * Removes all left and right indexes for which there is a match in the
	 * given difference object.
	 * 
	 * @param leftIndexes
	 *            The indexes, not null
	 * @param rightIndexes
	 *            The indexes, not null
	 * @param difference
	 *            The collection difference, not null
	 */
	protected void removeMatchingIndexes(ArrayList<Integer> leftIndexes, ArrayList<Integer> rightIndexes,
			UnorderedCollectionDifference difference) {
		Map<Integer, Map<Integer, Difference>> differences = difference.getElementDifferences();
		Iterator<Integer> rightIterator = rightIndexes.iterator();
		while (rightIterator.hasNext()) {
			int rightIndex = rightIterator.next();
			Iterator<Integer> leftIterator = leftIndexes.iterator();
			while (leftIterator.hasNext()) {
				int leftIndex = leftIterator.next();
				Difference elementDifference = differences.get(leftIndex).get(rightIndex);
				if (elementDifference == null) {
					rightIterator.remove();
					leftIterator.remove();
				}
			}
		}
	}

	/**
	 * @param size
	 *            The nr of elements
	 * @return A list containing 0, 1, 2, ..., not null
	 */
	protected ArrayList<Integer> createIndexList(int size) {
		ArrayList<Integer> leftIndexes = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			leftIndexes.add(i);
		}
		return leftIndexes;
	}

	/**
	 * Creates the calculator for determining the matching scores of the
	 * differences. These scores are used to determine the best matching
	 * elements.
	 * 
	 * @return The instance, not null
	 */
	protected MatchingScoreCalculator createMatchingScoreCalculator() {
		return new MatchingScoreCalculator();
	}
}
