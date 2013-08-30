package org.jtester.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import ext.jtester.hamcrest.MatcherAssert;
import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
@Test(groups = { "jtester", "assertion" })
public class PropertyAnyItemMatcherTest extends JTester {
	PropertyAnyItemMatcher matcher = new PropertyAnyItemMatcher("first", the.string().regular("\\w+\\d+\\w+"));

	@Test
	public void testMatches_List_HasPropMatch() {
		List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname"));
		MatcherAssert.assertThat(users, matcher);
	}

	@Test
	public void testMatches_Array_HasPropMatch() {
		User[] users = new User[] { new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname") };
		MatcherAssert.assertThat(users, matcher);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_List_HasPropNotMatch() {
		List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("eaafsd", "lastname"));
		MatcherAssert.assertThat(users, matcher);
	}
}
