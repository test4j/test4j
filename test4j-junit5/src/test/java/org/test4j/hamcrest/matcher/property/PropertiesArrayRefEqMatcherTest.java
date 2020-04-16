package org.test4j.hamcrest.matcher.property;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

import java.util.*;

public class PropertiesArrayRefEqMatcherTest extends Test4J {
    PropertiesArrayRefEqMatcher matcher = new PropertiesArrayRefEqMatcher(new String[]{"first", "last"},
            new String[][]{{"aaa", "bbb"}, {"ccc", "ddd"}});

    @Test
    public void testMatches_PropListEqExpected() {
        List<User> users = Arrays.asList(new User("aaa", "bbb"), new User("ccc", "ddd"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_PropList_HasPropNotEqExpected() {
        List<User> users = Arrays.asList(new User("aaa", "bbb"), new User("ccc", "dddd"));
        want.exception(() ->
                        MatcherAssert.assertThat(users, matcher)
                , AssertionError.class);
    }

    @Test
    public void testMatches_SingleValue_PropListEqExpected() {
        PropertiesArrayRefEqMatcher m = new PropertiesArrayRefEqMatcher(new String[]{"first", "last"},
                new String[][]{{"aaa", "bbb"}});
        MatcherAssert.assertThat(new User("aaa", "bbb"), m);
    }

    @Test
    public void testMatches_SingleValue_HasPropNotEqExpected() {
        PropertiesArrayRefEqMatcher m = new PropertiesArrayRefEqMatcher(new String[]{"first", "last"},
                new String[][]{{"aaa", "bbb"}});
        want.exception(() ->
                        MatcherAssert.assertThat(new User("aaa", "bbbb"), m)
                , AssertionError.class);
    }

    @Test
    public void testMatches_MapList_PropListEqExpected() {
        List<Map<String, String>> maps = maps("ccc", "ddd");

        MatcherAssert.assertThat(maps, matcher);
    }

    @Test
    public void testMatches_MapList_HasPropNotEqExpected() {
        List<Map<String, String>> maps = maps("ccc", "dddd");
        want.exception(() ->
                        MatcherAssert.assertThat(maps, matcher)
                , AssertionError.class);
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
