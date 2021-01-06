package org.test4j.asserts.iassert.object.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class MapAssertTest extends Test4J {
    private Map<String, String> maps = null;

    @BeforeEach
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

    @Test
    public void hasKeys_fail1() {
        want.exception(() ->
                want.map(maps).hasKeys("one", "four"), AssertionError.class);
    }

    @Test
    public void hasKeys_fail2() {
        want.exception(() -> want.map(maps).hasKeys("five"), AssertionError.class);
    }

    @Test
    public void testHasValues() {
        want.map(maps).hasValues("my first value", "my third value");
        want.map(maps).hasValues("my second value");
    }

    @Test
    public void hasValues_fail1() {
        want.exception(() -> want.map(maps).hasValues("unkown", "my third value"), AssertionError.class);
    }

    @Test
    public void hasValues_fail2() {
        want.exception(() -> want.map(maps).hasValues("unkown"), AssertionError.class);
    }

    @Test
    public void hasEntry() {
        want.map(maps).hasEntry("two", "my second value", "three");
    }

    @Test
    public void hasEntry_fail() {
        want.exception(() -> want.map(maps).hasEntry("two", "my second value", "three", "ddd"), AssertionError.class);
    }

    @Test
    public void hasEntry2() {
        Entry<?, ?> entry = maps.entrySet().iterator().next();
        want.map(maps).hasEntry(entry);
    }

    @Test
    public void testPropertyEq() {
        Map actual = new HashMap() {
            {
                this.put("key1", "value1");
                this.put("key2", null);
            }
        };
        want.exception(() ->
                want.map(actual).eqByProperties("key2", new HashMap()), AssertionError.class);
    }
}
