package org.test4j.hamcrest.matcher.property;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.test4j.hamcrest.matcher.property.difference.Difference;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.test4j.hamcrest.matcher.property.report.DefaultDifferenceReport;
import org.test4j.hamcrest.matcher.property.report.DifferenceReport;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.datagen.IDataMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.test4j.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;
import static org.test4j.tools.commons.ListHelper.getProperties;
import static org.test4j.tools.commons.ListHelper.toList;


/**
 * 把实际对象按照Map中的key值取出来，进行反射比较
 *
 * @author darui.wudr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MapListPropertyEqualMatcher extends BaseMatcher {

    private final List<Map<String, ? extends Object>> expected;

    private final Set<String> keySet = new HashSet<>();

    private EqMode[] modes;

    public MapListPropertyEqualMatcher(IDataMap expected, EqMode[] modes) {
        if (expected == null) {
            throw new AssertionError("MapPropertyEqaulMatcher, the expected map can't be null.");
        }
        this.expected = expected.rows();
        this.keySet.addAll(expected.keySet());
        this.modes = modes;
    }

    public MapListPropertyEqualMatcher(List<Map<String, ? extends Object>> expected, EqMode[] modes) {
        if (expected == null) {
            throw new AssertionError("MapPropertyEqaulMatcher, the expected map can't be null.");
        }
        this.expected = expected;
        expected.stream().forEach(map -> this.keySet.addAll(map.keySet()));
        this.modes = modes;
    }

    public boolean matches(Object actual) {
        if (actual == null) {
            this.self.appendText("MapPropertyEqualMatcher, the actual object can't be null or list/array.");
            return false;
        }
        if (ArrayHelper.isCollOrArray(actual) == false) {
            this.self.appendText("MapPropertyEqualMatcher, the actual object must be an array or a list.");
            return false;
        }
        Set<String> keys = this.getAllKeys();
        List<Map<String, ?>> _actual = getProperties(toList(actual), keys, false);
        List<Map<String, ?>> _expected = getProperties(expected, keys, false);
        if (_expected.size() != _actual.size()) {
            this.self.appendText("MapPropertyEqualMatcher, the size of expected object is " + _expected.size()
                    + ", but the size of actual list is " + _actual.size() + ".");
            return false;
        }

        ReflectionComparator reflectionComparator = createRefectionComparator(modes);
        this.difference = reflectionComparator.getDifference(_expected, _actual);
        return difference == null;
    }


    private Set<String> getAllKeys() {
        Set<String> keys = new HashSet<String>();
        for (Map map : this.expected) {
            Set set = map.keySet();
            keys.addAll(set);
        }
        return keys;
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
