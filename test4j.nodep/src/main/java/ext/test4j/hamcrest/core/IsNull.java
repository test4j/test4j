/*  Copyright (c) 2000-2010 hamcrest.org
 */
package ext.test4j.hamcrest.core;

import static ext.test4j.hamcrest.core.IsNot.not;
import ext.test4j.hamcrest.BaseMatcher;
import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;

/**
 * Is the value null?
 */
public class IsNull<T> extends BaseMatcher<T> {
    public boolean matches(Object o) {
        return o == null;
    }

    public void describeTo(Description description) {
        description.appendText("null");
    }

    /**
     * Matches if value is null.
     */
    @Factory
    public static Matcher<Object> nullValue() {
        return new IsNull<Object>();
    }

    /**
     * Matches if value is not null.
     */
    @Factory
    public static Matcher<Object> notNullValue() {
        return not(nullValue());
    }

    /**
     * Matches if value is null. With type inference.
     */
    @Factory
    public static <T> Matcher<T> nullValue(Class<T> type) {
        return new IsNull<T>();
    }

    /**
     * Matches if value is not null. With type inference.
     */
    @Factory
    public static <T> Matcher<T> notNullValue(Class<T> type) {
        return not(nullValue(type));
    }
}

