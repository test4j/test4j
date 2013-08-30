package org.jtester.hamcrest.matcher.string;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.TypeSafeMatcher;

public abstract class StringMatcher extends TypeSafeMatcher<String> {
	protected StringMode[] modes = null;

	protected String expected;

	private StringBuilder buff = new StringBuilder();

	public StringMatcher(String expected) {
		this.expected = expected;
	}

	public void setStringModes(StringMode... modes) {
		this.modes = modes;
	}

	@Override
	protected boolean matchesSafely(String item) {
		String expected = StringMode.getStringByMode(this.expected, modes);
		String actual = StringMode.getStringByMode(item == null ? null : String.valueOf(item), modes);
		boolean match = this.match(expected, actual);
		if (match == false) {
			this.description(expected, actual);
		}
		return match;
	}

	protected abstract boolean match(String expected, String actual);

	protected abstract String relationship();

	public void describeTo(Description description) {
		description.appendText(buff.toString());
	}

	private void description(String expected, String actual) {
		buff.append("expected string ");
		buff.append(this.relationship());
		buff.append(" ").append(expected);
		buff.append("\n, but actual string is:");
		buff.append(actual).append(".\n");
	}
}
