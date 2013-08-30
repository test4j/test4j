package org.jtester.hamcrest.matcher.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.json.encoder.beans.test.GenicBean;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
@Test(groups = { "jtester", "assertion" })
public class PropertiesEqualMatcherTest extends JTester {

	public void testProperEqual_ComplexAndIgnoreAll() {
		GenicBean[] actuals = new GenicBean[] { GenicBean.newInstance("bean1", newUser("darui.wu")),// <br>
				GenicBean.newInstance("bean2", newMap("map2")) // <br>
		};
		List expected = new ArrayList() {
			{
				add(GenicBean.newInstance("bean2", null));
				add(toList("bean1", "darui.wu"));
			}
		};
		PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected,
				new String[] { "name", "refObject.name" }, new EqMode[] { EqMode.IGNORE_ORDER, EqMode.IGNORE_DEFAULTS });
		MatcherAssert.assertThat(actuals, matcher);
	}

	public void testProperEqual_ComplexAndIgnoreAll2() {
		GenicBean[] actuals = new GenicBean[] { GenicBean.newInstance("bean1", toList("list1", "list2")),
				GenicBean.newInstance("bean2", toList("list3", "list4")),
				GenicBean.newInstance("bean3", newUser("darui.wu")),
				GenicBean.newInstance("bean4", toList("list5", "list6")) };
		List expected = new ArrayList() {
			{
				add(toList("bean3", newUser("darui.wu")));
				add(GenicBean.newInstance("bean2", new String[] { "list4", "list3" }));
				add(GenicBean.newInstance("bean1", new String[] { "list1", null }));
				add(GenicBean.newInstance("bean4", new String[] { null, "list6" }));
			}
		};
		PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected, new String[] { "name", "refObject" },
				new EqMode[] { EqMode.IGNORE_ORDER, EqMode.IGNORE_DEFAULTS });
		MatcherAssert.assertThat(actuals, matcher);
	}

	@Test(dataProvider = "matchData")
	public void testProperEqual(Object actual, Object expected, String properties, EqMode[] modes, boolean match) {
		String[] props = properties.split(",");
		PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected, props, modes);
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
				data(newUser("abc"), toList(123, "abc"), "id,name", null, true);
				data(newUser("abc"), toList("abc", 123), "id,name", null, false);
				data(newUser("abc"), toList("abc", 123), "id,name", new EqMode[] { EqMode.IGNORE_ORDER }, true);
				data(newUser("abc"), newMap("abc"), "id,name", null, true);
				data(toList(newUser("abc"), newUser("darui.wu")), toArray("abc", "darui.wu"), "name", null, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap("darui.wu")), "name",
						null, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), null), "name",
						new EqMode[] { EqMode.IGNORE_DEFAULTS }, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("darui.wu"), newMap("abc")), "name",
						new EqMode[] { EqMode.IGNORE_ORDER }, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap("darui.wu")),
						"id,name", null, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap(null)), "id,name",
						new EqMode[] { EqMode.IGNORE_DEFAULTS }, true);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newMap(null), newUser("abc")), "id,name",
						new EqMode[] { EqMode.IGNORE_DEFAULTS }, false);
				data(toArray(newMap("abc"), newUser("darui.wu")), toList(newMap(null), newUser("abc")), "id,name",
						new EqMode[] { EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_ORDER }, false);
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
