package org.test4j.hamcrest.matcher.property;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.test4j.hamcrest.diff.DiffFactory;
import org.test4j.hamcrest.diff.DiffMap;
import org.test4j.hamcrest.matcher.modes.EqMode;

import java.util.Collection;
import java.util.List;
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

    private DiffMap difference;

    @Override
    public boolean matches(Object actual) {
        this.difference = DiffFactory.diffBy(actual, expected, modes);
        return !difference.hasDiff();
    }

    @Override
    public void describeTo(Description description) {
        if (difference.hasDiff()) {
            description.appendText(difference.message());
        }
    }
}
