package org.jtester.hamcrest.matcher.property.comparator;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;

/**
 * A comparator that compares primitive types by value and not by type.
 * You can for example compare a double with an integer value.
 */
public class IgnoreNumberComparator implements Comparator {


    /**
     * Returns true if both objects are not null and instances of Number or Character.
     *
     * @param left  The left object
     * @param right The right object
     * @return True for Numbers and Charaters
     */
    public boolean canCompare(Object left, Object right) {
        if (left == null || right == null) {
            return false;
        }
        if ((left instanceof Character || left instanceof Number) && (right instanceof Character || right instanceof Number)) {
            return true;
        }
        return false;
    }

    /**
     * Compares the two values by converting them to a double and comparing these double values.
     *
     * @param left                 The left Number or Character, not null
     * @param right                The right Number or Character, not null
     * @param onlyFirstDifference  True if only the first difference should be returned
     * @param reflectionComparator The root comparator for inner comparisons, not null
     * @return A difference if the values are different, null otherwise
     */
    public Difference compare(Object left, Object right, boolean onlyFirstDifference, ReflectionComparator reflectionComparator) {
        // check if right and left have same number value (including NaN and Infinity)
        Double leftDouble = getDoubleValue(left);
        Double rightDouble = getDoubleValue(right);
        if (!leftDouble.equals(rightDouble)) {
            return new Difference("Different primitive values", left, right);
        }
        return null;
    }


    /**
     * Gets the double value for the given left Character or Number instance.
     *
     * @param object the Character or Number, not null
     * @return the value as a Double (this way NaN and infinity can be compared)
     */
    private Double getDoubleValue(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        return (double) ((Character) object).charValue();
    }
}
