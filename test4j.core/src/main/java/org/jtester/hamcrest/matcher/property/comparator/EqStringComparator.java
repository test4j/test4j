package org.jtester.hamcrest.matcher.property.comparator;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.jtester.tools.commons.DateHelper;

@SuppressWarnings("rawtypes")
public class EqStringComparator implements Comparator {

    public boolean canCompare(Object left, Object right) {
        return left instanceof String || left == null;
    }

    public Difference compare(Object left, Object right, boolean onlyFirstDifference,
                              ReflectionComparator reflectionComparator) {
        if (left == right) {// check if the same instance is referenced
            return null;
        } else if (left == null) {// check if the left value is null
            return new Difference("Left value null", left, right);
        } else if (right == null) { // check if the right value is null
            return new Difference("Right value null", left, right);
        }
        String _right = toString(right, left);
        String _left = toString(left, right);
        if (_left.equals(_right)) {
            return null;
        } else {
            return new Difference("Different object values", left, right);
        }
    }

    /**
     * 将obj对象转换为String对象
     * 
     * @param obj
     * @param format
     * @return
     */
    public static String toString(Object obj, Object format) {
        if (obj instanceof Date) {
            String _format = format instanceof String ? (String) format : "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat df = DateHelper.getDateFormat(_format);
            return df.format((Date) obj);
        }
        if (obj instanceof Enum) {
            return ((Enum) obj).name();
        }
        return String.valueOf(obj);
    }
}
