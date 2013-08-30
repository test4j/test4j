package org.jtester.hamcrest.matcher.array;

import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.MatcherAssert;
import org.jtester.hamcrest.matcher.array.SizeOrLengthMatcher.SizeOrLengthMatcherType;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SizeOrLengthMatcherTest extends JTester {

	public void test_AssertMessage() {
		Matcher matcher = new SizeOrLengthMatcher(2, SizeOrLengthMatcherType.EQ);
		Object actuals = new int[] { 1, 2, 3 };
		try {
			MatcherAssert.assertThat(actuals, matcher);
			want.fail("之前应该已抛出异常");
		} catch (Throwable e) {
			String message = e.getMessage();
			want.string(message).contains("but actual size is[3].");
		}
	}
}
