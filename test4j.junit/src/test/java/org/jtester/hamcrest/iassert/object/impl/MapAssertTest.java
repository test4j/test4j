package org.jtester.hamcrest.iassert.object.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jtester.junit.JTester;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class MapAssertTest implements JTester {
    private Map<String, String> maps = null;

    @Before
    public void setup() {
        maps = new HashMap<String, String>();
        maps.put("one", "my first value");
        maps.put("two", "my second value");
        maps.put("three", "my third value");
    }

    @Test
    public void testHasKeys() {
        want.map(maps).hasKeys("one", "two");
        want.map(maps).hasKeys("three");
    }

    @Test(expected = AssertionError.class)
    public void hasKeys_fail1() {
        want.map(maps).hasKeys("one", "four");
    }

    @Test(expected = AssertionError.class)
    public void hasKeys_fail2() {
        want.map(maps).hasKeys("five");
    }

    @Test
    public void testHasValues() {
        want.map(maps).hasValues("my first value", "my third value");
        want.map(maps).hasValues("my second value");
    }

    @Test(expected = AssertionError.class)
    public void hasValues_fail1() {
        want.map(maps).hasValues("unkown", "my third value");
    }

    @Test(expected = AssertionError.class)
    public void hasValues_fail2() {
        want.map(maps).hasValues("unkown");
    }

    @Test
    public void hasEntry() {
        want.map(maps).hasEntry("two", "my second value", "three");
    }

    @Test(expected = AssertionError.class)
    public void hasEntry_fail() {
        want.map(maps).hasEntry("two", "my second value", "three", "ddd");
    }

    @Test
    public void hasEntry2() {
        Entry<?, ?> entry = maps.entrySet().iterator().next();
        want.map(maps).hasEntry(entry);
    }

    @Test(expected = AssertionError.class)
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
