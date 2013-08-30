package org.jtester.hamcrest.matcher.property.comparator;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;

/**
 * Interface for comparing 2 given objects.
 * <p/>
 * One should always first call canCompare before calling compare to perform the actual comparison.
 * If canCompare returns false, the comparator implementation is not suited to compare the given objects and compare
 * should not be called.
 */
public interface Comparator {


    /**
     * Checks whether this comparator can compare the given objects.
     * <p/>
     * This method should always be called before calling the compare method. If false is returned compare
     * should not be invoked.
     *
     * @param left  The left object
     * @param right The right object
     * @return True if compare can be called, false otherwise
     */
    boolean canCompare(Object left, Object right);


    /**
     * Compares the given objects and returns the difference (if any).
     * <p/>
     * The reflection comparator is passed as an argument and can be used to perform inner comparisons.
     * E.g. during the comparison of an object the given comparator can be used to compare the instance
     * fields of the object.
     *
     * @param left                 The left object
     * @param right                The right object
     * @param onlyFirstDifference  True if only the first difference should be returned
     * @param reflectionComparator The root comparator for inner comparisons, not null
     * @return The difference, null if a match is found
     */
    Difference compare(Object left, Object right, boolean onlyFirstDifference, ReflectionComparator reflectionComparator);

}
