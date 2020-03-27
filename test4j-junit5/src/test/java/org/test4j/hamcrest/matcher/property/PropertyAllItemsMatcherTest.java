package org.test4j.hamcrest.matcher.property;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import org.test4j.model.User;

@SuppressWarnings("unchecked")
public class PropertyAllItemsMatcherTest extends Test4J {
    PropertyAllItemsMatcher matcher = new PropertyAllItemsMatcher("first", the.string().regular("\\w+\\d+\\w+"));

    @Test
    public void testMatches_List_AllItemsMatchAll() {
        MatcherAssert.assertThat(users(), matcher);
    }

    @Test
    public void testMatches_Array_AllItemsMatchAll() {
        User[] users = users().toArray(new User[0]);
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_List_HasItemNotMatch() {
        List<User> users = users();
        users.add(new User("aasdf", "dfddd"));
        want.exception(() ->
                        MatcherAssert.assertThat(users, matcher)
                , AssertionError.class);
    }

    @Test
    public void testMatches_SingleValue_PropMatch() {
        want.object(new User("firs3445tname", "")).propertyMatch("first", the.string().regular("\\w+\\d+\\w+"));
    }

    @Test
    public void testMatches_SingleValue_PropNotMatch() {
        want.exception(() ->
                        want.object(new User("firs dddt name", "")).propertyMatch("first", the.string().regular("\\w+\\d+\\w+"))
                , AssertionError.class);
    }

    static List<User> usersArr = null;

    private static List<User> users() {
        usersArr = new ArrayList<User>();
        usersArr.add(new User("firs3445tname", "lastname"));
        usersArr.add(new User("ee333ee", "ddddd"));

        return usersArr;
    }
}
