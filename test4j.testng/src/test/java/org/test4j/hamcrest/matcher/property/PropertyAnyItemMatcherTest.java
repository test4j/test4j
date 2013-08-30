package org.test4j.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import org.test4j.fortest.beans.User;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.MatcherAssert;

@SuppressWarnings("unchecked")
@Test(groups = { "test4j", "assertion" })
public class PropertyAnyItemMatcherTest extends Test4J {
    PropertyAnyItemMatcher matcher = new PropertyAnyItemMatcher("first", the.string().regular("\\w+\\d+\\w+"));

    @Test
    public void testMatches_List_HasPropMatch() {
        List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_Array_HasPropMatch() {
        User[] users = new User[] { new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname") };
        MatcherAssert.assertThat(users, matcher);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testMatches_List_HasPropNotMatch() {
        List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("eaafsd", "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }
}
