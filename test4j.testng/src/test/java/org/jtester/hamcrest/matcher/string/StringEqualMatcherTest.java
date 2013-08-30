package org.jtester.hamcrest.matcher.string;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@Test(groups = { "jtester", "assertion" })
public class StringEqualMatcherTest extends JTester {

	@Test(dataProvider = "data_StringEqualMatcher_Equals")
	public void testMatches_Equals(String expected, Object actual, boolean doesMatch, StringMode[] modes) {
		StringEqualMatcher matcher = new StringEqualMatcher(expected);

		matcher.setStringModes(modes);
		boolean match = matcher.matches(actual);
		want.bool(match).isEqualTo(doesMatch);
	}

	@DataProvider
	public Object[][] data_StringEqualMatcher_Equals() {
		return new Object[][] {
				{ null, "", false, null },
				{ "a b c", "a\f\rb\tc", true, new StringMode[] { StringMode.IgnoreSpace } },// <br>
				{ "ABC", "AbC", true, new StringMode[] { StringMode.IgnoreCase } }, // <br>
				{ "'abc\"\n\t\"abc'", "'abc' 'abc'", true,
						new StringMode[] { StringMode.SameAsQuato, StringMode.SameAsSpace } }, // <br>
				{ "a b c", "abc", false, null },// <br>
				{ "ABC", "AbC", false, null }, // <br>
				{ "'abc\"\n\t\"abc'", "'abc' 'abc'", false, null } // <br>
		};
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_ActualIsNull() {
		MatcherAssert.assertThat(null, new StringEqualMatcher(""));
	}
}
