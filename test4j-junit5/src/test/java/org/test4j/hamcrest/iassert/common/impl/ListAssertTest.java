package org.test4j.hamcrest.iassert.common.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class ListAssertTest extends Test4J {
    List<User> users;

    @BeforeEach
    public void initData() {
        users = new ArrayList<User>();
        users.add(new User("first1", "last1"));
        users.add(new User("first2", "last2"));
    }

    @Test
    public void testPropertyCollectionLenientEq() {
        String[][] expecteds = new String[][]{{"first2", "last2"}, {"first1", "last1"}};
        want.collection(users).eqByProperties(new String[]{"first", "last"}, expecteds, EqMode.IGNORE_ORDER);
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
        want.list(list).eqDataMap(new DataMap(2) {
            {
                this.kv("id", 1, 2);
                this.kv("name", "darui.wu", "jobs.he");
            }
        });
    }

    @Test
    public void testSizeBetween() {
        String[] arr = new String[]{"a", "b", "c"};
        want.list(arr).sizeBetween(1, 3);
    }

    @Test
    public void testSizeBetween_Failure() {
        String[] arr = new String[]{"a", "b", "c"};
        want.exception(() ->
                        want.list(arr).sizeBetween(4, 5),
                AssertionError.class);
    }

    @Test
    public void testSizeBetween_Failure2() {
        String[] arr = new String[]{"a", "b", "c"};
        want.exception(() -> want.list(arr).sizeBetween(1, 2),
                AssertionError.class);
    }
}
