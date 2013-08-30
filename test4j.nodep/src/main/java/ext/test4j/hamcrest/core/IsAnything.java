/*  Copyright (c) 2000-2006 hamcrest.org
 */
package ext.test4j.hamcrest.core;

import ext.test4j.hamcrest.BaseMatcher;
import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;


/**
 * A matcher that always returns <code>true</code>.
 */
public class IsAnything<T> extends BaseMatcher<T> {

    private final String message;

    public IsAnything() {
        this("ANYTHING");
    }

    public IsAnything(String message) {
        this.message = message;
    }

    public boolean matches(Object o) {
        return true;
    }

    public void describeTo(Description description) {
        description.appendText(message);
    }

    /**
     * This matcher always evaluates to true.
     */
    @Factory
    public static Matcher<Object> anything() {
        return new IsAnything<Object>();
    }

    /**
     * This matcher always evaluates to true.
     *
     * @param description A meaningful string used when describing itself.
     */
    @Factory
    public static Matcher<Object> anything(String description) {
        return new IsAnything<Object>(description);
    }
}
