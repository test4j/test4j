package org.jtester.hamcrest.matcher.array;

import java.util.Map;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;

public class MapMatcher extends BaseMatcher<Map<?, ?>> {
	private Object expected;

	private MapMatcherType type;

	public MapMatcher(Object expected, MapMatcherType type) {
		this.expected = expected;
		this.type = type;
	}

	public boolean matches(Object _actual) {
		if (_actual == null || !(_actual instanceof Map<?, ?>)) {
			return false;
		}
		Map<?, ?> actual = (Map<?, ?>) _actual;
		if (type == MapMatcherType.KEY) {
			return actual.containsKey(expected);
		} else {
			return actual.containsValue(expected);
		}
	}

	public void describeTo(Description description) {
		if (type == MapMatcherType.KEY) {
			description.appendText(String.format("the map must have the key %s", expected));
		} else {
			description.appendText(String.format("the map must have the value %s", expected));
		}
	}

	public static enum MapMatcherType {
		KEY, VALUE;
	}
}
