package org.jtester.hamcrest.matcher.property.comparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jtester.hamcrest.matcher.property.difference.CollectionDifference;
import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.jtester.tools.commons.ListHelper;

/**
 * Comparator for collections and arrays. All elements are compared in the same
 * order, i.e. element 1 of the left collection with element 1 of the right
 * collection and so on.
 */
public class ListComparator implements Comparator {

	/**
	 * Returns true when both objects are arrays or collections.
	 * 
	 * @param left
	 *            The left object
	 * @param right
	 *            The right object
	 * @return True in case of arrays/collections
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
	 * Compared the given collections/arrays.
	 * 
	 * @param left
	 *            The left collection/array, not null
	 * @param right
	 *            The right collection/array, not null
	 * @param onlyFirstDifference
	 *            True if only the first difference should be returned
	 * @param reflectionComparator
	 *            The root comparator for inner comparisons, not null
	 * @return A CollectionDifference or null if both collections are equal
	 */
	@SuppressWarnings({ "rawtypes" })
	public Difference compare(Object left, Object right, boolean onlyFirstDifference,
			ReflectionComparator reflectionComparator) {
		List leftList = ListHelper.toList(left);
		List rightList = ListHelper.toList(right);

		int elementIndex = -1;
		CollectionDifference difference = new CollectionDifference("Different elements", left, right, leftList,
				rightList);

		Iterator<?> leftIterator = leftList.iterator();
		Iterator<?> rightIterator = rightList.iterator();
		while (leftIterator.hasNext() && rightIterator.hasNext()) {
			elementIndex++;
			Object leftItem = leftIterator.next();
			Object rightItem = rightIterator.next();
			Difference elementDifference = reflectionComparator.getDifference(leftItem, rightItem, onlyFirstDifference);
			if (elementDifference != null) {
				difference.addElementDifference(elementIndex, elementDifference);
				if (onlyFirstDifference) {
					return difference;
				}
			}
		}

		// check for missing elements
		int leftElementIndex = elementIndex;
		while (leftIterator.hasNext()) {
			leftIterator.next();
			difference.addLeftMissingIndex(++leftElementIndex);
		}
		int rightElementIndex = elementIndex;
		while (rightIterator.hasNext()) {
			rightIterator.next();
			difference.addRightMissingIndex(++rightElementIndex);
		}

		if (difference.getElementDifferences().isEmpty() && difference.getLeftMissingIndexes().isEmpty()
				&& difference.getRightMissingIndexes().isEmpty()) {
			return null;
		}
		return difference;
	}
}
