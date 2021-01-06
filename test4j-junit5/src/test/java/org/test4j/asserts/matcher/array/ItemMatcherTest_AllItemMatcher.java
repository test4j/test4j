package org.test4j.asserts.matcher.array;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.asserts.matcher.modes.ItemsMode;
import org.test4j.junit5.Test4J;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"unchecked", "rawtypes"})
public class ItemMatcherTest_AllItemMatcher extends Test4J {
    Matcher m1 = the.string().contains("abc");

    ListEveryItemMatcher matcher = new ListEveryItemMatcher(m1, ItemsMode.AllItems);

    @Test
    public void testMatches_Collection() {
        MatcherAssert.assertThat(Arrays.asList("ddd abc ddd", "ddddabcd"), matcher);
    }

    @Test
    public void testMatcher_Regular() {
        Matcher m2 = the.string().regular("\\w+\\d+\\w+");
        want.exception(() -> {
            ListEveryItemMatcher arrayMatcher = new ListEveryItemMatcher(m2, ItemsMode.AllItems);
            MatcherAssert.assertThat(new String[]{"ab345c", "abcd"}, arrayMatcher);
        }, AssertionError.class);
    }

    @Test
    public void testMatches_Collection_Failure() {
        want.exception(() ->
                        MatcherAssert.assertThat(Arrays.asList("ddd abc ddd", "ddddd"), matcher)
                , AssertionError.class);
    }

    @Test
    public void testMatches_Array() {
        MatcherAssert.assertThat(new String[]{"ddd abc ddd", "ddabcddd"}, matcher);
    }

    @Test
    public void testMatches_Array_Failure() {
        want.exception(() ->
                        MatcherAssert.assertThat(new String[]{"ddd abc ddd", "ddddd"}, matcher)
                , AssertionError.class);
    }

    @Test
    public void testMatches_Map() {
        Map map = new HashMap() {
            private static final long serialVersionUID = 1L;

            {
                put("key1", "ddd abc ddd");
                put("key2", "ddabcdd");
            }
        };
        MatcherAssert.assertThat(map, matcher);

    }

    @Test
    public void testMatches_Failure_Map() {
        Map map = new HashMap() {
            private static final long serialVersionUID = 1L;

            {
                put("key1", "ddd abc ddd");
                put("key2", "dddd");
            }
        };
        want.exception(() -> MatcherAssert.assertThat(map, matcher), AssertionError.class);
    }

    @Test
    public void testMatches_SingleValue() {
        MatcherAssert.assertThat("ddd abc ddd", matcher);
    }

    @Test
    public void testMatches_SingleValue_Failure() {
        want.exception(() -> MatcherAssert.assertThat("ddd ddd ddd", matcher), AssertionError.class);
    }

    @Test
    public void testMatches_SingleValueIsNull_Failure() {
        want.exception(() -> MatcherAssert.assertThat(null, matcher), AssertionError.class);
    }
}
