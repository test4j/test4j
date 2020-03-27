package org.test4j.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import org.test4j.model.User;

@SuppressWarnings("unchecked")
public class PropertyAnyItemMatcherTest extends Test4J {
    PropertyAnyItemMatcher matcher = new PropertyAnyItemMatcher("first", the.string().regular("\\w+\\d+\\w+"));

    @Test
    public void testMatches_List_HasPropMatch() {
        List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_Array_HasPropMatch() {
        User[] users = new User[]{new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname")};
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_List_HasPropNotMatch() {
        List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("eaafsd", "lastname"));
        want.exception(() ->
                        MatcherAssert.assertThat(users, matcher)
                , AssertionError.class);
    }
}
