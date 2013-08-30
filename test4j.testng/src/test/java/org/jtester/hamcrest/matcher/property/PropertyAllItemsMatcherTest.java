package org.jtester.hamcrest.matcher.property;

import java.util.ArrayList;
import java.util.List;

import ext.jtester.hamcrest.MatcherAssert;
import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
@Test(groups = { "jtester", "assertion" })
public class PropertyAllItemsMatcherTest extends JTester {
	PropertyAllItemsMatcher matcher = new PropertyAllItemsMatcher("first", the.string().regular("\\w+\\d+\\w+"));

	@Test
	public void testMatches_List_AllItemsMatchAll() {
		MatcherAssert.assertThat(users(), matcher);
	}

	@Test
	public void testMatches_Array_AllItemsMatchAll() {
		User[] users = users().toArray(new User[0]);
		MatcherAssert.assertThat(users, matcher);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_List_HasItemNotMatch() {
		List<User> users = users();
		users.add(new User("aasdf", "dfddd"));
		MatcherAssert.assertThat(users, matcher);
	}

	public void testMatches_SingleValue_PropMatch() {
		want.object(new User("firs3445tname", "")).propertyMatch("first", the.string().regular("\\w+\\d+\\w+"));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_SingleValue_PropNotMatch() {
		want.object(new User("firs dddt name", "")).propertyMatch("first", the.string().regular("\\w+\\d+\\w+"));
	}

	static List<User> usersArr = null;

	private static List<User> users() {
		usersArr = new ArrayList<User>();
		usersArr.add(new User("firs3445tname", "lastname"));
		usersArr.add(new User("ee333ee", "ddddd"));

		return usersArr;
	}
}
