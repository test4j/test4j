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
public class PropertyEqualMatcher extends BaseMatcher {
    private final Object expected;

    private final String property;

    private final EqMode[] modes;

    private final StringBuilder buff = new StringBuilder();

    private DiffMap difference;

    public PropertyEqualMatcher(Object expected, String properties, EqMode[] modes) {
        this.expected = expected;
        this.property = properties;
        if (this.property == null) {
            throw new RuntimeException("the properties can't be empty.");
        }
        this.modes = modes;
    }

    public PropertyEqualMatcher(Object expected, String property) {
        this(expected, property, null);
    }

    @Override
    public boolean matches(Object actual) {
        if (actual == null) {
            buff.append("properties equals matcher, the actual value can't be null.");
            return false;
        }
        if (ArrayHelper.isCollOrArray(actual)) {
            return this.matchList(ListHelper.toList(actual));
        } else {
            return this.matchPoJo(actual);
        }
    }

    private boolean matchList(List array) {
        List actuals = PropertyAccessor.getPropertyOfList(array, property, true);
        List expects = ListHelper.toList(this.expected);
        expects = PropertyAccessor.getPropertyOfList(expects, property, false);

        this.difference = DiffFactory.diffBy(actuals, expects, modes);
        return !difference.hasDiff();
    }

    private boolean matchPoJo(Object pojo) {
        Object actuals = PropertyAccessor.getPropertyByOgnl(pojo, property, true);
        Object expects = PropertyAccessor.getPropertyByOgnl(this.expected, property, false);
        this.difference = DiffFactory.diffBy(actuals, expects, modes);
        return !difference.hasDiff();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(buff.toString());
        if (difference.hasDiff()) {
            String message = "Incorrect value for properties: " + ArrayHelper.toString(this.property);
            description.appendText(message);
            description.appendText(difference.message());
        }
    }
}
