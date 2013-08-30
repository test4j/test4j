package org.jtester.hamcrest.matcher.string;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.TypeSafeMatcher;

public class StringNotBlankMatcher extends TypeSafeMatcher<String> {
	String actual;

	public void describeTo(Description description) {
		description.appendText("expect a not null and not empty string, but actual is [" + this.actual + "]");
	}

	@Override
	public final boolean matches(Object item) {
		if (item == null) {
			return false;
		} else {
			return String.class.isInstance(item) && matchesSafely((String) item);
		}
	}

	@Override
	protected boolean matchesSafely(String item) {
		this.actual = item;
		return !"".equals(actual.trim());
	}
}
