package org.jtester.hamcrest.matcher.string;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@Test(groups = { "jtester", "assertion" })
public class StringContainMatcherTest extends JTester {

	@Test(dataProvider = "dataForStringContains")
	public void testMatches(String actual, String expected, boolean doesMatch, StringMode[] modes) {
		StringContainMatcher matcher = new StringContainMatcher(new String[] { expected }, modes);

		boolean match = matcher.matches(actual);
		want.bool(match).is(doesMatch);
	}

	@DataProvider
	public Object[][] dataForStringContains() {
		return new Object[][] {
				{ "", null, false, null },// <br>
				{ "'abc' \"ABCD\"", "'abcd'", true, new StringMode[] { StringMode.IgnoreCase, StringMode.SameAsQuato } },// <br>
				{ " abc \t\n abcc ", "c abc", true, new StringMode[] { StringMode.SameAsSpace } },// <br>
				{ "'abc' \"ABCD\"", "'abcd'", false, new StringMode[] { StringMode.IgnoreCase } },// <br>
				{ " abc \t\n abcc ", "c abc", false, new StringMode[] { StringMode.IgnoreCase } } // <br>}
		};
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_ActualIsNull() {
		MatcherAssert.assertThat(null, new StringContainMatcher(new String[] { "" }, null));
	}
}
