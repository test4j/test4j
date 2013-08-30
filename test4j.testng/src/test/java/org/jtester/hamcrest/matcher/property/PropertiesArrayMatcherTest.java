package org.jtester.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class PropertiesArrayMatcherTest extends JTester {

	@Test
	public void testMatches() {
		want.object(new User("dfasdf", "eedaf")).propertyEq(new String[] { "first", "last" },
				new Object[] { "dfasdf", "eedaf" });
	}

	@Test
	public void testMatches_Array() {
		List<User> users = Arrays.asList(new User("dfa123sdf", "abc"), new User("firs123tname", "abc"));
		want.collection(users).propertyMatch("first", the.collection().hasAllItems("dfa123sdf", "firs123tname"))
				.propertyEq("last", new String[] { "abc", "abc" });
	}

	public void testMatches_Map() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("first", "aaaaa 123 ddd");
		maps.put("last", "abc");
		want.map(maps).propertyMatch("first", the.string().contains("123")).propertyEq("last", "abc");
	}
}
