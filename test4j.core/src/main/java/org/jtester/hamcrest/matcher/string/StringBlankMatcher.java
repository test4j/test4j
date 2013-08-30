package org.jtester.hamcrest.matcher.string;

import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.TypeSafeMatcher;

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
