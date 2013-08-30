/*  Copyright (c) 2000-2006 hamcrest.org
 */
package ext.jtester.hamcrest.core;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Factory;
import ext.jtester.hamcrest.Matcher;


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
