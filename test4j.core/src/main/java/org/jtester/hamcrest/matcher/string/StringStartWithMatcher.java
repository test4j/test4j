package org.jtester.hamcrest.matcher.string;

public class StringStartWithMatcher extends StringMatcher {

	public StringStartWithMatcher(String expected) {
		super(expected);
	}

	@Override
	protected boolean match(String expected, String actual) {
		if (expected == null) {
			return false;
		} else {
			return actual.startsWith(expected);
		}
	}

	@Override
	protected String relationship() {
		return "start with by modes" + StringMode.toString(modes);
	}
}
