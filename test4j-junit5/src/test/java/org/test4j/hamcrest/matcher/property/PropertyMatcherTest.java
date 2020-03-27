package org.test4j.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import org.test4j.model.User;

@SuppressWarnings("unchecked")
public class PropertyMatcherTest extends Test4J {
    PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.collection().hasAllItems("aaa", "bbb"));

    @Test
    public void testMatches_Collection() {
        List<User> users = Arrays.asList(new User("aaa", "eebbdaf"), new User("bbb", "lastname"), new User("ccc",
                "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_Collection_Failure() {
        List<User> users = Arrays.asList(new User("aaa", "eebbdaf"), new User("bbbb", "lastname"), new User("ccc",
                "lastname"));
        want.exception(() ->
                        MatcherAssert.assertThat(users, matcher)
                , AssertionError.class);
    }

    @Test
    public void testMatches_SingleValue() {
        PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.string().isEqualTo("aaa"));
        MatcherAssert.assertThat(new User("aaa", "eebbdaf"), matcher);
    }

    @Test
    public void testMatches_SingleValue_Failure() {
        PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.collection().eqReflect(
                new String[]{"aaa"}));
        want.exception(() ->
                        MatcherAssert.assertThat(new User("bbb", "eebbdaf"), matcher)
                , AssertionError.class);
    }
}
