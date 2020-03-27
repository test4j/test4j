package org.test4j.hamcrest.matcher.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.mockito.Matches;
import org.test4j.hamcrest.matcher.modes.ItemsMode;
import org.test4j.junit5.Test4J;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ListEveryItemMatcherTest extends Test4J {
    Matcher m1 = the.string().contains("abc");
    ListEveryItemMatcher matcher = new ListEveryItemMatcher(m1, ItemsMode.AnyItems);

    @Test
    public void testMatches_Collection() {
        MatcherAssert.assertThat(Arrays.asList("ddd abc ddd", "ddddd"), matcher);
    }

    @Test
    public void testMatches_Collection_Regex() {
        Matcher<?> regular = new Matches("abc[2-4]{2,4}abc");
        ListEveryItemMatcher matcher = new ListEveryItemMatcher(regular, ItemsMode.AnyItems);
        MatcherAssert.assertThat(Arrays.asList("abc234abc", "ddddd"), matcher);
    }

    @Test
    public void testMatches_Collection_Failure() {
        want.exception(() -> MatcherAssert.assertThat(Arrays.asList("ddd ddd ddd", "ddddd"), matcher), AssertionError.class);
    }

    @Test
    public void testMatches_Array() {
        MatcherAssert.assertThat(new String[]{"ddd abc ddd", "ddddd"}, matcher);
    }

    @Test
    public void testMatches_Array_Failure() {
        want.exception(() -> MatcherAssert.assertThat(new String[]{"ddd ddd ddd", "ddddd"}, matcher), AssertionError.class);
    }

    @Test
    public void testMatches_Map() {
        Matcher m1 = the.string().contains("abc");
        ListEveryItemMatcher matcher = new ListEveryItemMatcher(m1, ItemsMode.AnyItems);
        Map map = new HashMap() {
            private static final long serialVersionUID = 1L;

            {
                put("key1", "ddd abc ddd");
                put("key2", "dddd");
            }
        };
        MatcherAssert.assertThat(map, matcher);

    }

    @Test
    public void testMatches_Map_Failure() {
        Map map = new HashMap() {
            private static final long serialVersionUID = 1L;

            {
                put("key1", "ddd ddd ddd");
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
