package org.jtester.hamcrest.matcher.string;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@Test(groups = { "jtester", "assertion" })
public class IgnoreAllSpaceMatcherTest extends JTester {

	@Test(dataProvider = "spaceMatcherData")
	public void testMatches(String expected, String actual, boolean doesMatch) {
		StringMatcher matcher = new StringEqualMatcher(expected);
		matcher.setStringModes(StringMode.IgnoreSpace);

		boolean match = matcher.matches(actual);
		want.bool(match).isEqualTo(doesMatch);
	}

	@DataProvider
	public Object[][] spaceMatcherData() {
		return new Object[][] { { "", "", true },// <br>
				{ null, "", false }, /** <br> */
				{ "\n\t\b\f", "", true }, /** <br> */
				{ " d ", "d", true } /** <br> */
		};
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_ActualIsNull() {
		MatcherAssert.assertThat(null, new StringEqualMatcher(""));
	}
}
