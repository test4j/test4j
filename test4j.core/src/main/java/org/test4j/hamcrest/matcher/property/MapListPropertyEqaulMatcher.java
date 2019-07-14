package org.test4j.hamcrest.matcher.property;

import static org.test4j.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.reflector.PropertyAccessor;


/**
 * 把实际对象按照Map中的key值取出来，进行反射比较
 *
 * @author darui.wudr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MapListPropertyEqaulMatcher extends BaseMatcher {

    private final List<Map<String, ? extends Object>> expected;

    private final Set<String> keySet = new HashSet<>();

    private EqMode[] modes;

    public MapListPropertyEqaulMatcher(IDataMap expected, EqMode[] modes) {
        if (expected == null) {
            throw new AssertionError("MapPropertyEqaulMatcher, the expected map can't be null.");
        }
        this.expected = expected.toList();
        this.keySet.addAll(expected.keySet());
        this.modes = modes;
    }

    public MapListPropertyEqaulMatcher(List<Map<String, ? extends Object>> expected, EqMode[] modes) {
        if (expected == null) {
            throw new AssertionError("MapPropertyEqaulMatcher, the expected map can't be null.");
        }
        this.expected = expected;
        expected.stream().forEach(map -> this.keySet.addAll(map.keySet()));
        this.modes = modes;
    }

    public boolean matches(Object actual) {
        if (actual == null) {
            this.self.appendText("MapPropertyEqaulMatcher, the actual object can't be null or list/array.");
            return false;
        }
        if (ArrayHelper.isCollOrArray(actual) == false) {
            this.self.appendText("MapPropertyEqaulMatcher, the actual object must be an array or a list.");
            return false;
        }
        List list = ListHelper.toList(actual);

        if (this.expected.size() != list.size()) {
            this.self.appendText("MapPropertyEqaulMatcher, the size ofexpeced object is " + this.expected.size()
                    + ", but the size of actual list is " + list.size() + ".");
            return false;
        }
        Set<String> keys = this.getAllKeys();

        List<Map<String, ?>> actuals = getObjectArrayFromList(list, keys, false);

        ReflectionComparator reflectionComparator = createRefectionComparator(modes);
        this.difference = reflectionComparator.getDifference(expected, actuals);
        return difference == null;
    }

    private List<Map<String, ?>> getObjectArrayFromList(List list, Set<String> keys, boolean isExpected) {
        List<Map<String, ?>> result = new ArrayList<Map<String, ?>>();
        for (Object o : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : keys) {
                try {
                    Object value = PropertyAccessor.getPropertyByOgnl(o, key, true);
                    map.put(key, value);
                } catch (NoSuchFieldRuntimeException e) {
                    if (isExpected) {
                        map.put(key, null);
                    } else {
                        throw e;
                    }
                }
            }
            result.add(map);
        }
        return result;
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
