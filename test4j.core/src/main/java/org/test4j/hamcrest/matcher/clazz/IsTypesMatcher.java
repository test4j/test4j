package org.test4j.hamcrest.matcher.clazz;


import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

@SuppressWarnings("rawtypes")
public class IsTypesMatcher extends BaseMatcher {
    private final Class[] types;

    public IsTypesMatcher(Class[] types) {
        this.types = types;
    }

    public boolean matches(Object actual) {
        if (actual == null) {
            return true;
        }
        if (types == null || types.length == 0) {
            return true;
        }
        for (Class type : types) {
            if (type.isInstance(actual) == false) {
                return false;
            }
        }
        return true;
    }

    public void describeTo(Description description) {
        // TODO Auto-generated method stub
    }
}
