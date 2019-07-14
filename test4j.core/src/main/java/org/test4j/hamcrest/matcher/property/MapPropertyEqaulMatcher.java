package org.test4j.hamcrest.matcher.property;

import static org.test4j.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.test4j.hamcrest.matcher.property.difference.Difference;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.test4j.hamcrest.matcher.property.report.DefaultDifferenceReport;
import org.test4j.hamcrest.matcher.property.report.DifferenceReport;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.reflector.PropertyAccessor;


/**
 * 把实际对象按照Map中的key值取出来，进行反射比较
 *
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class MapPropertyEqaulMatcher extends BaseMatcher {

    private final Map<String, ?> expected;

    private EqMode[] modes;

    public MapPropertyEqaulMatcher(Map<String, ? extends Object> expected, EqMode[] modes) {
        this.expected = expected;
        this.modes = modes;
        if (expected == null) {
            throw new AssertionError("MapPropertyEqaulMatcher, the expected map can't be null.");
        }
    }

    public boolean matches(Object actual) {
        if (actual == null) {
            this.self.appendText("MapPropertyEqaulMatcher, the actual object can't be null or list/array.");
            return false;
        }

        Map<String, Object> map = this.getValueByMapKeys(actual);

        ReflectionComparator reflectionComparator = createRefectionComparator(modes);
        this.difference = reflectionComparator.getDifference(this.expected, map);
        return difference == null;
    }

    private Map<String, Object> getValueByMapKeys(Object actual) {
        Map<String, Object> map = new HashMap<>();
        for (String property : expected.keySet()) {
            Object value = PropertyAccessor.getPropertyByOgnl(actual, property, true);
            map.put(property, value);
        }
        return map;
    }

    private StringDescription self = new StringDescription();

    private Difference difference;

    public void describeTo(Description description) {
        description.appendText(self.toString());
        if (difference != null) {
            DifferenceReport differenceReport = new DefaultDifferenceReport();
            description.appendText(differenceReport.createReport(difference));
        }
    }
}
