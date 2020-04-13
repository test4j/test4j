package org.test4j.hamcrest.matcher.property;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.test4j.hamcrest.diff.DiffFactory;
import org.test4j.hamcrest.diff.DiffMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.reflector.PropertyAccessor;

import java.util.List;

@SuppressWarnings("rawtypes")
public class PropertiesEqualMatcher extends BaseMatcher {
    private final Object expected;

    private final String[] properties;

    private final EqMode[] modes;

    private DiffMap difference;

    public PropertiesEqualMatcher(Object expected, String[] properties, EqMode[] modes) {
        this.expected = expected;
        this.properties = properties;
        if (this.properties == null || this.properties.length == 0) {
            throw new RuntimeException("the properties can't be empty.");
        }
        this.modes = modes;
    }

    public PropertiesEqualMatcher(Object expected, String[] properties) {
        this(expected, properties, null);
    }

    @Override
    public boolean matches(Object actual) {
        if (actual == null) {
            buff.append("properties equals matcher, the actual value can't be null.");
            return false;
        }
        if (ArrayHelper.isCollOrArray(actual)) {
            List list = ListHelper.toList(actual);
            return this.matchList(list);
        } else {
            return this.matchPoJo(actual);
        }
    }

    private boolean matchList(List array) {
        if (ArrayHelper.isCollOrArray(this.expected) == false) {
            buff.append("property of List/Array equals matcher, the expected value should be an Array or Collection, but is a type[");
            buff.append(this.expected == null ? "null" : this.expected.getClass().getName());
            buff.append("] object.\n");
            return false;
        }
        List expected = ListHelper.toList(this.expected);
        if (expected.size() > array.size()) {
            buff.append("the size of expected array is greater then the size of actual array.");
        }
        List<List> actuals = PropertyAccessor.getPropertiesOfList(array, properties, true);
        List<List> expects = PropertyAccessor.getPropertiesOfList(expected, properties, false);
        this.difference = DiffFactory.diffBy(actuals, expects, modes);
        return !this.difference.hasDiff();
    }

    private boolean matchPoJo(Object pojo) {
        List actuals = PropertyAccessor.getPropertiesOfPoJo(pojo, properties, true);
        List expects = null;
        if (ArrayHelper.isCollOrArray(this.expected)) {
            expects = ListHelper.toList(this.expected);
        } else {
            expects = PropertyAccessor.getPropertiesOfPoJo(this.expected, properties, false);
        }
        this.difference = DiffFactory.diffBy(actuals, expects, modes);
        return !this.difference.hasDiff();
    }

    private final StringBuilder buff = new StringBuilder();

    @Override
    public void describeTo(Description description) {
        description.appendText(buff.toString());
        if (difference.hasDiff()) {
            String message = "Incorrect value for properties: " + ArrayHelper.toString(this.properties);
            description.appendText(message);
            description.appendText(difference.message());
        }
    }
}
