package org.jtester.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JTester;
import org.junit.Test;

import ext.jtester.hamcrest.MatcherAssert;

@SuppressWarnings("unchecked")
public class PropertyMatcherTest implements JTester {
    PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.collection().hasAllItems("aaa", "bbb"));

    @Test
    public void testMatches_Collection() {
        List<User> users = Arrays.asList(new User("aaa", "eebbdaf"), new User("bbb", "lastname"), new User("ccc",
                "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test(expected = AssertionError.class)
    public void testMatches_Collection_Failure() {
        List<User> users = Arrays.asList(new User("aaa", "eebbdaf"), new User("bbbb", "lastname"), new User("ccc",
                "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_SingleValue() {
        PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.string().isEqualTo("aaa"));
        MatcherAssert.assertThat(new User("aaa", "eebbdaf"), matcher);
    }

    @Test(expected = AssertionError.class)
    public void testMatches_SingleValue_Failure() {
        PropertyItemMatcher matcher = new PropertyItemMatcher("first", the.collection().reflectionEq(
                new String[] { "aaa" }));
        MatcherAssert.assertThat(new User("bbb", "eebbdaf"), matcher);
    }
}
