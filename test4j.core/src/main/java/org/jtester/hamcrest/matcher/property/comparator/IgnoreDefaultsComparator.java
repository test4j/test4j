package org.jtester.hamcrest.matcher.property.comparator;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;

/**
 * A comparator that filters out java-defaults.
 * If the left object is null, false or 0, both objects are considered equal.
 * This implements the IGNORE_DEFAULTS comparison mode.
 *
 */
public class IgnoreDefaultsComparator implements Comparator {


    /**
     * Returns true if the left object is a java default
     *
     * @param left  The left object
     * @param right The right object
     * @return True if left is null, false or 0
     */
    public boolean canCompare(Object left, Object right) {
        // object types
        if (left == null) {
            return true;
        }
        // primitive boolean types
        if (left instanceof Boolean && !(Boolean) left) {
            return true;
        }
        // primitive character types
        if (left instanceof Character && (Character) left == 0) {
            return true;
        }
        // primitive int/long/double/float types
        if (left instanceof Number && ((Number) left).doubleValue() == 0) {
            return true;
        }
        return false;
    }


    /**
     * Always returns null: both objects are equal.
     *
     * @param left                 The left object
     * @param right                The right object
     * @param onlyFirstDifference  True if only the first difference should be returned
     * @param reflectionComparator The root comparator for inner comparisons, not null
     * @return null
     */
    public Difference compare(Object left, Object right, boolean onlyFirstDifference, ReflectionComparator reflectionComparator) {
        // ignore
        return null;
    }
}
