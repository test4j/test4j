package org.jtester.hamcrest.matcher.string;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.TypeSafeMatcher;

/**
 * 经过模式处理后，判断一个字符串是否包含另外一个字符串
 * 
 * @author darui.wudr
 * 
 */
public class StringContainMatcher extends TypeSafeMatcher<String> {

	private final String[] expecteds;

	private final StringMode[] modes;

	public StringContainMatcher(String[] expectedSubString, StringMode[] modes) {
		this.expecteds = expectedSubString;
		this.modes = modes;
	}

	@Override
	public void describeMismatchSafely(String item, Description mismatchDescription) {
		mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
	}

	public void describeTo(Description description) {
		description.appendText("a string containing ").appendValueList("", ", ", "", expecteds);
		if (modes != null && modes.length > 0) {
			description.appendText(" by modes" + StringMode.toString(modes));
		}
		if ("".endsWith(message) == false) {
			description.appendText(", but ").appendText(message);
		}
	}

	private String message = "";

	@Override
	protected boolean matchesSafely(String item) {
		if (item == null) {
			message = "the actual can't be null.";
			return false;
		}
		String actual = StringMode.getStringByMode(item, modes);

		for (String sub : this.expecteds) {
			if (sub == null) {
				message = "the sub string can't be null.";
				return false;
			}
			String expected = StringMode.getStringByMode(sub, modes);
			boolean result = actual.contains(expected);
			if (result == false) {
				message = String.format("the string[%s] not contain substring[%s].", actual, expected);
				return false;
			}
		}
		return true;
	}

	public static StringContainMatcher contains(String sub) {
		StringContainMatcher matcher = new StringContainMatcher(new String[] { sub }, null);
		return matcher;
	}
}
