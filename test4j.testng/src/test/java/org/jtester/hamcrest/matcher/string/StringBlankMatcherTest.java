package org.jtester.hamcrest.matcher.string;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@Test(groups = { "jtester", "assertion" })
public class StringBlankMatcherTest extends JTester {

	@Test(dataProvider = "blankString_true")
	public void testMatches_True(String input) {
		StringBlankMatcher matcher = new StringBlankMatcher();
		MatcherAssert.assertThat(input, matcher);
	}

	@DataProvider
	public Object[][] blankString_true() {
		return new Object[][] { { "" },// <br>
				{ "   " }, /** <br> */
				{ "\n\t\b\f" } /** <br> */
		};
	}

	@Test(dataProvider = "blankString_false")
	public void testMatches_False(String input) {
		StringBlankMatcher matcher = new StringBlankMatcher();
		try {
			MatcherAssert.assertThat(input, matcher);
		} catch (Error e) {
			String message = e.getMessage();
			want.string(message).contains("expected is empty string,  but actual", StringMode.IgnoreSpace);
		}
	}

	@DataProvider
	public Object[][] blankString_false() {
		return new Object[][] { { null }, // <br>
				{ " d " } /** <br> */
		};
	}
}
