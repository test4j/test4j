package org.jtester.hamcrest.iassert.object.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jtester.testng.JTester;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class MapAssertTest extends JTester {
	private Map<String, String> maps = null;

	@BeforeMethod
	public void setup() {
		maps = new HashMap<String, String>();
		maps.put("one", "my first value");
		maps.put("two", "my second value");
		maps.put("three", "my third value");
	}

	public void testHasKeys() {
		want.map(maps).hasKeys("one", "two");
		want.map(maps).hasKeys("three");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void hasKeys_fail1() {
		want.map(maps).hasKeys("one", "four");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void hasKeys_fail2() {
		want.map(maps).hasKeys("five");
	}

	public void testHasValues() {
		want.map(maps).hasValues("my first value", "my third value");
		want.map(maps).hasValues("my second value");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void hasValues_fail1() {
		want.map(maps).hasValues("unkown", "my third value");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void hasValues_fail2() {
		want.map(maps).hasValues("unkown");
	}

	public void hasEntry() {
		want.map(maps).hasEntry("two", "my second value", "three");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void hasEntry_fail() {
		want.map(maps).hasEntry("two", "my second value", "three", "ddd");
	}

	public void hasEntry2() {
		Entry<?, ?> entry = maps.entrySet().iterator().next();
		want.map(maps).hasEntry(entry);
	}

	@Test(expectedExceptions = { NoSuchFieldRuntimeException.class, AssertionError.class })
	public void testPropertyEq() {
		Map actual = new HashMap() {
			{
				this.put("key1", "value1");
				this.put("key2", null);
			}
		};
		want.map(actual).propertyEq("key2", new HashMap());
	}
}
