package org.test4j.hamcrest;

import org.junit.jupiter.api.Test;
import org.test4j.model.User;
import org.test4j.module.ICore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestArrayAssert implements ICore {
    @Test
    public void test1() {
        want.array(new String[]{"aaaa", "bbbb"}).hasAllItems("aaaa", "bbbb");
        want.array(new int[]{1, 2}).hasItems(1);
        want.array(new double[]{1, 2.0d}).hasItems(1d);

        want.array(new int[]{1, 2, 3}).hasAllItems(1, 3);
    }

    @Test
    public void test2() {
        want.exception(() ->
                        want.array(new int[]{1, 2, 3}).hasItems(Arrays.asList(1, 4)),
                AssertionError.class);
    }

    @Test
    public void testPropertyMatch() {
        User user = User.mock();
        want.object(user).propertyMatch("addresses", the.collection().eqByProperties("id", new int[]{2, 3}));
    }

    @Test
    public void testPropertyMatcher2() {
        User user = User.mock();

        want.object(user).propertyMatch("addresses", the.collection().eqByProperties("id", new int[]{2, 3}));
    }

    protected static List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        return users;
    }
}
