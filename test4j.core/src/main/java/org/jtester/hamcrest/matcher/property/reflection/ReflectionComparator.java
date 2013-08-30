package org.jtester.hamcrest.matcher.property.reflection;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.comparator.Comparator;
import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.module.JTesterException;

/**
 * A comparator for comparing two values by reflection.
 * <p/>
 * The actual comparison of the values is implemented as a comparator chain. The
 * chain is passed as an argument during the construction. Each of the
 * comparators in the chain is able to compare some types of values. E.g. a
 * CollectionComparator is able to compare collections, simple values such as
 * integers are compared by the SimpleCasesComparator... A number of 'leniency
 * levels' can also be added to the chain: e.g. the
 * LenientOrderCollectionComparator ignores the actual ordering of 2
 * collections.
 * <p/>
 * The preferred way of creating new instances is by using the
 * ReflectionComparatorFactory. This factory will make sure that a correct
 * comparator chain is assembled.
 * <p/>
 * A readable report differences can be created using the DifferenceReport.
 * 
 */
public class ReflectionComparator {

	/**
	 * The comparator chain.
	 */
	protected List<Comparator> comparators;

	/**
	 * A cache of results, so that comparisons are only performed once and
	 * infinite loops because of cycles are avoided A different cache is used
	 * dependent on whether only the first difference is required or whether we
	 * need all differences, since the resulting {@link Difference} objects
	 * differ.
	 */
	protected Map<Object, Map<Object, Difference>> firstDifferenceCachedResults = new IdentityHashMap<Object, Map<Object, Difference>>();
	protected Map<Object, Map<Object, Difference>> allDifferencesCachedResults = new IdentityHashMap<Object, Map<Object, Difference>>();

	/**
	 * Creates a comparator that will use the given chain.
	 * 
	 * @param comparators
	 *            The comparator chain, not null
	 */
	public ReflectionComparator(List<Comparator> comparators) {
		this.comparators = comparators;
	}

	/**
	 * Checks whether there is no difference between the left and right objects.
	 * 
	 * @param expectedValue
	 *            the left instance
	 * @param actualValue
	 *            the right instance
	 * @return true if there is no difference, false otherwise
	 */
	public boolean isEqual(Object expectedValue, Object actualValue) {
		Difference difference = getDifference(expectedValue, actualValue, true);
		return difference == null;
	}

	/**
	 * Checks whether there is a difference between the left and right objects.
	 * 
	 * @param expectedValue
	 *            the left instance
	 * @param actualValue
	 *            the right instance
	 * @return the difference, null if there is no difference
	 */
	public Difference getDifference(Object expectedValue, Object actualValue) {
		return getDifference(expectedValue, actualValue, false);
	}

	/**
	 * Checks whether there are differences between the left and right objects.
	 * This will return the root difference of the whole difference tree
	 * containing all the differences between the objects.
	 * 
	 * @param expectedValue
	 *            the left instance
	 * @param actualValue
	 *            the right instance
	 * @param onlyFirstDifference
	 *            True if the comparison should stop at the first differnece
	 * @return the root difference, null if there is no difference
	 */
	public Difference getDifference(Object expectedValue, Object actualValue, boolean onlyFirstDifference) {
		// check whether difference is available in cache
		Map<Object, Difference> cachedResult = getCachedDifference(expectedValue, onlyFirstDifference);
		if (cachedResult != null) {
			if (cachedResult.containsKey(actualValue)) {
				// found difference in cache, return cached value
				return cachedResult.get(actualValue);
			}
		} else {
			cachedResult = new IdentityHashMap<Object, Difference>();
			saveResultInCache(expectedValue, cachedResult, onlyFirstDifference);
		}
		cachedResult.put(actualValue, null);

		// perform actual comparison by iterating over the comparators
		boolean compared = false;
		Difference result = null;
		for (Comparator comparator : comparators) {
			boolean canCompare = comparator.canCompare(expectedValue, actualValue);
			if (canCompare) {
				result = comparator.compare(expectedValue, actualValue, onlyFirstDifference, this);
				compared = true;
				break;
			}
		}

		// check whether a suitable comparator was found
		if (!compared) {
			throw new JTesterException(
					"Could not determine differences. No comparator found that is able to compare the values. Left: "
							+ expectedValue + ", right " + actualValue);
		}

		// register outcome in cache
		cachedResult.put(actualValue, result);
		return result;
	}

	protected void saveResultInCache(Object left, Map<Object, Difference> cachedResult, boolean onlyFirstDifference) {
		if (onlyFirstDifference) {
			firstDifferenceCachedResults.put(left, cachedResult);
		} else {
			allDifferencesCachedResults.put(left, cachedResult);
		}
	}

	protected Map<Object, Difference> getCachedDifference(Object left, boolean onlyFirstDifference) {
		if (onlyFirstDifference) {
			return firstDifferenceCachedResults.get(left);
		} else {
			return allDifferencesCachedResults.get(left);
		}
	}
}
