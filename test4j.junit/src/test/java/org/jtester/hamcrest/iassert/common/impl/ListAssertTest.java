package org.jtester.hamcrest.iassert.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.fortest.beans.User;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.junit.JTester;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class ListAssertTest implements JTester {
    List<User> users;

    @Before
    public void initData() {
        users = new ArrayList<User>();
        users.add(new User("first1", "last1"));
        users.add(new User("first2", "last2"));
    }

    @Test
    public void testPropertyCollectionLenientEq() {
        String[][] expecteds = new String[][] { { "first2", "last2" }, { "first1", "last1" } };
        want.collection(users).propertyEq(new String[] { "first", "last" }, expecteds, EqMode.IGNORE_ORDER);
    }

    @Test
    public void testReflectionEqMap() {
        List<Map> list = new ArrayList() {
            {
                add(new HashMap() {
                    {
                        this.put("id", 1);
                        this.put("name", "darui.wu");
                    }
                });
                add(new HashMap() {
                    {
                        this.put("id", 2);
                        this.put("name", "jobs.he");
                    }
                });
            }
        };
        want.list(list).reflectionEqMap(2, new DataMap() {
            {
                this.put("id", 1, 2);
                this.put("name", "darui.wu", "jobs.he");
            }
        });
    }

    @Test
    public void testSizeBetween() {
        String[] arr = new String[] { "a", "b", "c" };
        want.list(arr).sizeBetween(1, 3);
    }

    @Test(expected = AssertionError.class)
    public void testSizeBetween_Failure() {
        String[] arr = new String[] { "a", "b", "c" };
        want.list(arr).sizeBetween(4, 5);
    }

    @Test(expected = AssertionError.class)
    public void testSizeBetween_Failure2() {
        String[] arr = new String[] { "a", "b", "c" };
        want.list(arr).sizeBetween(1, 2);
    }
}
