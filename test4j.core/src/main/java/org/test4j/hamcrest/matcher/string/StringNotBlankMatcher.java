package org.test4j.hamcrest.matcher.string;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class StringNotBlankMatcher extends TypeSafeMatcher<String> {
    String actual;

    public void describeTo(Description description) {
        description.appendText("expect a not null and not empty string, but actual is [" + this.actual + "]");
    }

    @Override
    protected boolean matchesSafely(String item) {
        this.actual = item;
        return !"".equals(actual.trim());
    }
}
