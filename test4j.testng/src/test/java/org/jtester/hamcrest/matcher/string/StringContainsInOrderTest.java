package org.jtester.hamcrest.matcher.string;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "assertion")
public class StringContainsInOrderTest extends JTester {

	@Test
	public void testMatchesSafely() {
		String actual = "abcefg";
		want.string(actual).containsInOrder("abc", "efg");
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatchesSafely_failure() {
		String actual = "abcefg";
		want.string(actual).containsInOrder("abc", "bce");
	}

	/**
	 * 在不忽略大小写的情况下,字符串"Abc Efg"并包含子串"abc","efg"
	 */
	@Test(expectedExceptions = AssertionError.class)
	public void testContainsInOrder_NoModes() {
		String actual = "Abc Efg";
		want.string(actual).containsInOrder(new String[] { "abc", "efg" });
	}

	/**
	 * 在忽略大小写的情况下,字符串"Abc Efg"包含子串"abc","efg"
	 */
	@Test
	public void testContainsInOrder_HasModes() {
		String actual = "Abc Efg";
		want.string(actual).containsInOrder(new String[] { "abc", "efg" }, StringMode.IgnoreCase);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testContainInOrder_ActualStringCanNotBeNull() {
		want.string(null).contains("");
	}

	public void testContainInOrder_SubStringCanNotBeNull() {
		try {
			want.string("").contains((String) null);
			throw new RuntimeException("error");
		} catch (AssertionError e) {
			String message = e.getMessage();
			want.string(message).contains("the sub string can't be null");
		}
	}
}
