package org.jtester.hamcrest.matcher.property.comparator;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.difference.MapDifference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;

import static org.jtester.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Comparator for maps. This will compare all values with corresponding keys.
 */
public class MapComparator implements Comparator {


    /**
     * Returns true when both values are not null and instance of Map
     *
     * @param left  The left object
     * @param right The right object
     * @return True for maps
     */
    public boolean canCompare(Object left, Object right) {
        if (left == null || right == null) {
            return false;
        }
        if ((left instanceof Map && right instanceof Map)) {
            return true;
        }
        return false;
    }


    /**
     * Compares the given maps by looping over the keys and comparing their values.
     * The key values are compared using a strict reflection comparison.
     *
     * @param left                 The left map, not null
     * @param right                The right map, not null
     * @param onlyFirstDifference  True if only the first difference should be returned
     * @param reflectionComparator The root comparator for inner comparisons, not null
     * @return A MapDifference or null if both maps are equal
     */
    public Difference compare(Object left, Object right, boolean onlyFirstDifference, ReflectionComparator reflectionComparator) {
        Map<?, ?> leftMap = (Map<?, ?>) left;
        Map<?, ?> rightMap = (Map<?, ?>) right;

        // Create copy from which we can remove elements.
        Map<Object, Object> rightCopy = new HashMap<Object, Object>(rightMap);

        ReflectionComparator keyReflectionComparator = createRefectionComparator();
        MapDifference difference = new MapDifference("Different elements", left, right, leftMap, rightMap);

        for (Map.Entry<?, ?> leftEntry : leftMap.entrySet()) {
            Object leftKey = leftEntry.getKey();
            Object leftValue = leftEntry.getValue();

            boolean found = false;
            Iterator<Map.Entry<Object, Object>> rightIterator = rightCopy.entrySet().iterator();
            while (rightIterator.hasNext()) {
                Map.Entry<Object, Object> rightEntry = rightIterator.next();
                Object rightKey = rightEntry.getKey();
                Object rightValue = rightEntry.getValue();

                // compare keys using strict reflection compare
                boolean isKeyEqual = keyReflectionComparator.isEqual(leftKey, rightKey);
                if (isKeyEqual) {
                    found = true;
                    rightIterator.remove();

                    // compare values
                    Difference elementDifference = reflectionComparator.getDifference(leftValue, rightValue, onlyFirstDifference);
                    if (elementDifference != null) {
                        difference.addValueDifference(leftKey, elementDifference);
                        if (onlyFirstDifference) {
                            return difference;
                        }
                    }
                    break;
                }
            }

            if (!found) {
                difference.addLeftMissingKey(leftKey);
            }
        }

        for (Object rightKey : rightCopy.keySet()) {
            difference.addRightMissingKey(rightKey);
        }

        if (difference.getValueDifferences().isEmpty() && difference.getLeftMissingKeys().isEmpty() && difference.getRightMissingKeys().isEmpty()) {
            return null;
        }
        return difference;
    }
}
