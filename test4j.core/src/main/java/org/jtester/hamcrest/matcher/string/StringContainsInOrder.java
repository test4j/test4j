package org.jtester.hamcrest.matcher.string;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Factory;
import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.TypeSafeMatcher;

public class StringContainsInOrder extends TypeSafeMatcher<String> {
	private final Iterable<String> substrings;

	private final StringMode[] modes;

	public StringContainsInOrder(Iterable<String> substrings) {
		this.substrings = substrings;
		this.modes = new StringMode[] {};
	}

	public StringContainsInOrder(Iterable<String> substrings, StringMode[] modes) {
		this.substrings = substrings;
		this.modes = modes;
	}

	String message = "";

	@Override
	public boolean matchesSafely(String s) {
		if (s == null) {
			message = "the actual can't be null.";
			return false;
		}
		String actual = StringMode.getStringByMode(s, modes);
		int fromIndex = 0;

		for (String substring : substrings) {
			if (substring == null) {
				message = "the sub string can't be null.";
				return false;
			}
			String expected = StringMode.getStringByMode(substring, modes);
			int index = actual.indexOf(expected, fromIndex);
			if (index == -1) {
				message = String.format("the string[%s] not contain substring[%s] from index %d.", actual, expected,
						fromIndex);
				return false;
			} else {
				fromIndex = index + expected.length();
			}
		}
		return true;
	}

	@Override
	public void describeMismatchSafely(String item, Description mismatchDescription) {
		mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
	}

	public void describeTo(Description description) {
		description.appendText("a string containing ").appendValueList("", ", ", "", substrings);
		if (modes != null && modes.length > 0) {
			description.appendText(" in order by modes" + StringMode.toString(modes));
		}
		if ("".endsWith(message) == false) {
			description.appendText(", but ").appendText(message);
		}
	}

	@Factory
	public static Matcher<String> stringContainsInOrder(Iterable<String> substrings) {
		return new StringContainsInOrder(substrings);
	}
}
