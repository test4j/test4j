package org.jtester.hamcrest.matcher.string;

public class StringEndWithMatcher extends StringMatcher {

	public StringEndWithMatcher(String expected) {
		super(expected);
	}

	@Override
	protected boolean match(String expected, String actual) {
		if (expected == null) {
			return false;
		} else {
			return actual.endsWith(expected);
		}
	}

	@Override
	protected String relationship() {
		return "end with by modes" + StringMode.toString(modes);
	}
}
