package org.test4j.hamcrest.matcher.property;

import static org.test4j.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;

import java.util.Collection;
import java.util.List;


import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.test4j.hamcrest.matcher.property.difference.Difference;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.test4j.hamcrest.matcher.property.report.DefaultDifferenceReport;
import org.test4j.hamcrest.matcher.property.report.DifferenceReport;

/**
 * 以反射的方式验证2个对象是否相等
 *
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class ReflectionEqualMatcher extends BaseMatcher {
    private Object expected;

    private EqMode[] modes;

    public ReflectionEqualMatcher(Object expected, EqMode[] modes) {
        this.expected = expected;
        this.modes = modes == null ? null : modes.clone();
    }

    public ReflectionEqualMatcher(Collection expected, EqMode[] modes) {
        this.expected = expected;
        this.modes = modes == null ? null : modes.clone();
    }

    public <T extends Object> ReflectionEqualMatcher(T[] expected, EqMode[] modes) {
        this.expected = expected == null ? null : expected.clone();
        this.modes = modes == null ? null : modes.clone();
    }

    public ReflectionEqualMatcher(List expected, EqMode[] modes) {
        this.expected = expected;
        this.modes = modes == null ? null : modes.clone();
    }

    private Difference difference;

    public boolean matches(Object actual) {
        ReflectionComparator reflectionComparator = createRefectionComparator(modes);
        this.difference = reflectionComparator.getDifference(expected, actual);
        return difference == null;
    }

    public void describeTo(Description description) {
        if (difference != null) {
            DifferenceReport differenceReport = new DefaultDifferenceReport();
            description.appendText(differenceReport.createReport(difference));
        }
    }
}
