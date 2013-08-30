/*  Copyright (c) 2000-2006 hamcrest.org
 */
package ext.test4j.hamcrest.object;

import java.util.EventObject;

import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;
import ext.test4j.hamcrest.TypeSafeDiagnosingMatcher;


/**
 * Tests if the value is an event announced by a specific object.
 */
public class IsEventFrom extends TypeSafeDiagnosingMatcher<EventObject> {
    private final Class<?> eventClass;
    private final Object source;

    public IsEventFrom(Class<?> eventClass, Object source) {
        this.eventClass = eventClass;
        this.source = source;
    }

    @Override
    public boolean matchesSafely(EventObject item, Description mismatchDescription) {
        if (!eventClass.isInstance(item)) {
          mismatchDescription.appendText("item type was " + item.getClass().getName());
          return false;
        }
        
        if (!eventHasSameSource(item)) {
          mismatchDescription.appendText("source was ").appendValue(item.getSource());
          return false;
        }
        return true;
    }

    
    private boolean eventHasSameSource(EventObject ev) {
        return ev.getSource() == source;
    }

    public void describeTo(Description description) {
        description.appendText("an event of type ")
                .appendText(eventClass.getName())
                .appendText(" from ")
                .appendValue(source);
    }

    /**
     * Constructs an IsEventFrom Matcher that returns true for any object
     * derived from <var>eventClass</var> announced by <var>source</var>.
     */
    @Factory
    public static Matcher<EventObject> eventFrom(Class<? extends EventObject> eventClass, Object source) {
        return new IsEventFrom(eventClass, source);
    }

    /**
     * Constructs an IsEventFrom Matcher that returns true for any object
     * derived from {@link java.util.EventObject} announced by <var>source
     * </var>.
     */
    @Factory
    public static Matcher<EventObject> eventFrom(Object source) {
        return eventFrom(EventObject.class, source);
    }
}
