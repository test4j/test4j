package org.jtester.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import ext.jtester.hamcrest.MatcherAssert;
import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
@Test(groups = { "jtester", "assertion" })
public class PropertyMatcherTest extends JTester {
	PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.collection().hasAllItems("aaa", "bbb"));

	public void testMatches_Collection() {
		List<User> users = Arrays.asList(new User("aaa", "eebbdaf"), new User("bbb", "lastname"), new User("ccc",
				"lastname"));
		MatcherAssert.assertThat(users, matcher);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_Collection_Failure() {
		List<User> users = Arrays.asList(new User("aaa", "eebbdaf"), new User("bbbb", "lastname"), new User("ccc",
				"lastname"));
		MatcherAssert.assertThat(users, matcher);
	}

	public void testMatches_SingleValue() {
		PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.string().isEqualTo("aaa"));
		MatcherAssert.assertThat(new User("aaa", "eebbdaf"), matcher);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_SingleValue_Failure() {
		PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.collection().reflectionEq(
				new String[] { "aaa" }));
		MatcherAssert.assertThat(new User("bbb", "eebbdaf"), matcher);
	}
}
