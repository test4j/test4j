package org.jtester.hamcrest.matcher.property;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
@Test(groups = { "jtester", "assertion" })
public class PropertyEqualMatcherTest extends JTester {
	public void testProperIsArray() {
		Map actual = new HashMap() {
			{
				this.put("key1", new String[] { "value1", "value2" });
			}
		};
		want.object(actual).propertyEq("key1", new String[] { "value1", "value2" });
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testProperIsArray_failure() {
		Map actual = new HashMap() {
			{
				this.put("key1", new String[] { "value1" });
			}
		};

		want.object(actual).propertyEq("key1", "value1");
	}

	public void testPropertyActualIsArray() {
		List list = toList(User.newInstance(124, ""), User.newInstance(125, ""));
		want.object(list).propertyEq("id", toList(124, 125));

		want.object(list).propertyEq("id", toList(User.newInstance(124, ""), User.newInstance(125, "")));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testPropertyActualIsArray_Failure() {
		List list = toList(User.newInstance(124, ""));
		want.object(list).propertyEq("id", User.newInstance(124, ""));
	}

	public void testProper_Normal() {
		Map actual = new HashMap() {
			{
				this.put("key1", "value1");
			}
		};
		want.object(actual).propertyEq("key1", "value1");
	}

	public void testProper_NormalPoJo() {
		User user = User.newInstance(125, "darui.wu");
		want.object(user).propertyEq("name", "darui.wu").propertyEq("id", new HashMap() {
			{
				put("id", 125);
			}
		});
	}

	@Test(dataProvider = "matchData")
	public void testProperEqual(Object actual, Object expected, String property, EqMode[] modes, boolean match) {
		PropertyEqualMatcher matcher = new PropertyEqualMatcher(expected, property, modes);
		try {
			MatcherAssert.assertThat(actual, matcher);
			want.bool(match).isEqualTo(true);
		} catch (AssertionError error) {
			error.printStackTrace();
			want.bool(match).isEqualTo(false);
		}
	}

	@DataProvider
	public Iterator matchData() {
		return new DataIterator() {
			{
				data(newUser("abc"), "abc", "name", null, true);
				data(newUser("abc"), null, "name", null, false);
				data(newUser("abc"), null, "name", new EqMode[] { EqMode.IGNORE_DEFAULTS }, true);
				data(newUser("abc"), newUser("abc"), "name", null, true);
				data(newUser("abc"), "abc", "name", null, true);
				data(newUser("abc"), newMap("abc"), "name", null, true);
				data(toList(newUser("abc"), newUser("darui.wu")), toArray("abc", "darui.wu"), "name", null, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap("darui.wu")), "name",
						null, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), null), "name",
						new EqMode[] { EqMode.IGNORE_DEFAULTS }, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("darui.wu"), newMap("abc")), "name",
						new EqMode[] { EqMode.IGNORE_ORDER }, true);
			}
		};
	}

	private static User newUser(String name) {
		return User.newInstance(123, name);
	}

	private static Map newMap(final String name) {
		return new HashMap() {
			{
				put("id", 123);
				put("name", name);
			}
		};
	}
}
