/*  Copyright (c) 2000-2006 hamcrest.org
 */
package ext.test4j.hamcrest.core;

import ext.test4j.hamcrest.BaseMatcher;
import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;


/**
 * Is the value the same object as another value?
 */
public class IsSame<T> extends BaseMatcher<T> {
    private final T object;
    
    public IsSame(T object) {
        this.object = object;
    }

    public boolean matches(Object arg) {
        return arg == object;
    }

    public void describeTo(Description description) {
        description.appendText("sameInstance(")
                .appendValue(object)
                .appendText(")");
    }
    
    /**
     * Creates a new instance of IsSame
     *
     * @param object The predicate evaluates to true only when the argument is
     *               this object.
     */
    @Factory
    public static <T> Matcher<T> sameInstance(T object) {
        return new IsSame<T>(object);
    }
}
