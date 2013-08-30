package org.jtester.hamcrest.matcher.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ext.jtester.hamcrest.MatcherAssert;
import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class PropertiesArrayRefEqMatcherTest extends JTester {
	PropertiesArrayRefEqMatcher matcher = new PropertiesArrayRefEqMatcher(new String[] { "first", "last" },
			new String[][] { { "aaa", "bbb" }, { "ccc", "ddd" } });

	public void testMatches_PropListEqExpected() {
		List<User> users = Arrays.asList(new User("aaa", "bbb"), new User("ccc", "ddd"));
		MatcherAssert.assertThat(users, matcher);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_PropList_HasPropNotEqExpected() {
		List<User> users = Arrays.asList(new User("aaa", "bbb"), new User("ccc", "dddd"));
		MatcherAssert.assertThat(users, matcher);
	}

	public void testMatches_SingleValue_PropListEqExpected() {
		PropertiesArrayRefEqMatcher m = new PropertiesArrayRefEqMatcher(new String[] { "first", "last" },
				new String[][] { { "aaa", "bbb" } });
		MatcherAssert.assertThat(new User("aaa", "bbb"), m);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_SingleValue_HasPropNotEqExpected() {
		PropertiesArrayRefEqMatcher m = new PropertiesArrayRefEqMatcher(new String[] { "first", "last" },
				new String[][] { { "aaa", "bbb" } });
		MatcherAssert.assertThat(new User("aaa", "bbbb"), m);
	}

	public void testMatches_MapList_PropListEqExpected() {
		List<Map<String, String>> maps = maps("ccc", "ddd");

		MatcherAssert.assertThat(maps, matcher);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMatches_MapList_HasPropNotEqExpected() {
		List<Map<String, String>> maps = maps("ccc", "dddd");

		MatcherAssert.assertThat(maps, matcher);
	}

	private static List<Map<String, String>> maps(String first, String last) {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("first", "aaa");
		map1.put("last", "bbb");
		maps.add(map1);

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("first", first);
		map2.put("last", last);
		maps.add(map2);

		return maps;
	}
}
