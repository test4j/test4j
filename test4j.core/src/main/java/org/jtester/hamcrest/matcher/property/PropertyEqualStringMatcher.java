package org.jtester.hamcrest.matcher.property;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.tools.commons.ArrayHelper;
import org.jtester.tools.reflector.PropertyAccessor;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;

@SuppressWarnings("rawtypes")
public class PropertyEqualStringMatcher extends BaseMatcher {
	private final String expected;

	private final String property;

	private final StringMode[] modes;

	public PropertyEqualStringMatcher(String expected, String properties, StringMode[] modes) {
		this.expected = expected;
		this.property = properties;
		if (this.property == null) {
			throw new RuntimeException("the properties can't be empty.");
		}
		this.modes = modes;
	}

	public PropertyEqualStringMatcher(String expected, String property) {
		this(expected, property, null);
	}

	public boolean matches(Object actual) {
		if (actual == null) {
			buff.append("properties equals matcher, the actual value can't be null.");
			return false;
		}
		if (ArrayHelper.isCollOrArray(actual)) {
			buff.append("PropertyEqualStringMatcher can only accept PoJo Object or Map, but actual is Array/List.");
			return false;
		}

		Object propValue = PropertyAccessor.getPropertyByOgnl(actual, property, true);

		String expectedString = StringMode.getStringByMode(this.expected, modes);
		String actualString = StringMode.getStringByMode(propValue == null ? null : String.valueOf(propValue), modes);

		boolean match = expectedString == null ? actualString == null : expectedString.equals(actualString);
		if (match == false) {
			this.description(expectedString, actualString);
		}
		return match;
	}

	private void description(String expected, String actual) {
		buff.append("expected property eq by modes");
		buff.append(" ").append(expected);
		buff.append("\n, but actual string is:");
		buff.append(actual).append(".\n");
	}

	private final StringBuilder buff = new StringBuilder();

	public void describeTo(Description description) {
		description.appendText(buff.toString());
	}
}
