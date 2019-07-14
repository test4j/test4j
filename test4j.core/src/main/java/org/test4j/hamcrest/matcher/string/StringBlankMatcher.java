package org.test4j.hamcrest.matcher.string;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class StringBlankMatcher extends TypeSafeMatcher<String> {
    String actual;

    public void describeTo(Description description) {
        description.appendText("expected is empty string, but actual is [" + this.actual + "]");
    }

    @Override
    protected boolean matchesSafely(String item) {
        if (item == null) {
            return true;
        }
        actual = String.valueOf(item);
        return "".equals(actual.trim());
    }
}
