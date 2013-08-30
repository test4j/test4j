package org.jtester.hamcrest.matcher.property.comparator;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.difference.ObjectDifference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;

import static org.jtester.hamcrest.matcher.property.reflection.HibernateUtil.*;

/**
 * Comparator that can handle Hibernate proxies.
 * <p/>
 * Special thanks to Tim Peeters for helping us with the implementation.
 *
 */
public class HibernateProxyComparator implements Comparator {


    /**
     * Returns true if one or both of the objects is a Hibernate proxy.
     *
     * @param left  The left object
     * @param right The right object
     * @return True if one is a proxy
     */
    public boolean canCompare(Object left, Object right) {
        return isHibernateProxy(left) || isHibernateProxy(right);
    }


    /**
     * Compares the given objects. If one of the objects is a proxy, the proxy is initialized and the wrapped values
     * are compared. If both objects are proxies and both objects are not yet loaded (initialized) only the idendtifiers
     * are compared. This avoids performing unnecessary loads from the database (potentially retrieving a huge
     * amount of data). If the ids of the proxies are identical, the objects are considered identical, if not the
     * objects are considered different.
     *
     * @param left                 The left object, not null
     * @param right                The right object, not null
     * @param onlyFirstDifference  True if only the first difference should be returned
     * @param reflectionComparator The root comparator for inner comparisons, not null
     * @return A ObjectDifference or null if both maps are equal
     */
    public Difference compare(Object left, Object right, boolean onlyFirstDifference, ReflectionComparator reflectionComparator) {
        if (isUninitialized(left) && isUninitialized(right)) {
            String leftType = getEntitiyName(left);
            String rightType = getEntitiyName(right);
            if (leftType == null || !leftType.equals(rightType)) {
                return new ObjectDifference("Different hibernate proxy types. Left: " + leftType + ", right: " + rightType, left, right);
            }

            Object leftIndentifier = getIdentifier(left);
            Object rightIdentifier = getIdentifier(right);
            Difference identifierDifference = reflectionComparator.getDifference(leftIndentifier, rightIdentifier, onlyFirstDifference);
            if (identifierDifference != null) {
                ObjectDifference difference = new ObjectDifference("Different hibernate proxy values", left, right);
                difference.addFieldDifference("<proxy id>", identifierDifference);
                return difference;
            }
            return null;
        }

        // get the actual value if the value is wrapped by a Hibernate proxy
        Object leftUnproxied = getUnproxiedValue(left);
        Object rightUnproxied = getUnproxiedValue(right);
        return reflectionComparator.getDifference(leftUnproxied, rightUnproxied, onlyFirstDifference);
    }


}