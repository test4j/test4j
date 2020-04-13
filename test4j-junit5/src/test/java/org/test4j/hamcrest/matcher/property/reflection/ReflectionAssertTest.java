package org.test4j.hamcrest.matcher.property.reflection;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.hamcrest.matcher.modes.ItemsMode;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.Employee;
import org.test4j.model.Manager;
import org.test4j.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

public class ReflectionAssertTest extends Test4J {
    @Test
    public void propertyMatch() {
        Manager manager = new Manager();
        manager.setName("I am darui.wu");
        want.object(manager).propertyMatch("name", the.string().contains("darui"));
    }

    @Test
    public void propertyMatch_AssertFail() {
        Manager manager = new Manager();
        manager.setName("I am darui.wu");
        want.exception(() ->
                        want.object(manager).propertyMatch("name", the.string().contains("darui1"))
                , AssertionError.class);
    }

    @Test
    public void propertyEq() {
        Employee employee = new Employee();
        want.object(employee).eqByProperties("name", null);
        employee.setName("my name");
        want.object(employee).eqByProperties("name", "my name");
    }

    @Test
    public void propertyMatch2() {
        Employee employee = new Employee();
        want.object(employee).propertyMatch("name", the.string().isNull());
        employee.setName("my name");
        want.object(employee).propertyMatch("name", the.string().isEqualTo("my name"));
        want.object(employee).eqByProperties("name", "my name");
    }

    @Test
    public void propertyMatch_forlist() {
        List<Employee> list = createEmployee();
        want.collection(list).sizeEq(4).propertyMatch("name", the.collection().hasItems("test name 1"));
    }

    @Test
    public void propertyMatch_forlist_failure() {
        List<Employee> list = createEmployee();
        want.exception(() ->
                        want.collection(list).sizeEq(4).propertyMatch("name", the.string().contains("name 1"))
                , AssertionError.class);
    }

    @Test
    public void propertyMatch_formap() {
        Map<String, String> map = createStringMap();
        want.map(map).eqByProperties("key1", "test1").eqByProperties("key2", "test2");
    }

    @Test
    public void propertyMatch_formap_NoKey() {
        Map<String, String> map = createStringMap();
        try {
            want.map(map).eqByProperties("key3", null);
            want.fail();
        } catch (Exception e) {
            want.object(e).eqToString(the.string().contains("can't find property"));
        }
    }

    @Test
    public void propertyMatch_formap_list() {
        List<Map<String, String>> list = createMapList();
        want.collection(list).propertyMatch("key1", the.collection().hasItems("test1"));
    }

    @Test
    public void propertyMatchOne_forlist() {
        List<Employee> list = createEmployee();
        want.collection(list).sizeEq(4).propertyMatch(ItemsMode.AnyItems, "name", the.string().contains("name 1"));
    }

    @Test
    public void propertyMatchOne_forlist_failure() {
        List<Employee> list = createEmployee();
        want.exception(() ->
                        want.collection(list).sizeEq(4).propertyMatch("name", the.string().contains("name 5"))
                , AssertionError.class);
    }

    @Test
    public void lenientEq_failure() {
        String[] expected = {"1", "2", "3", "4", "17", "18", "19", "20", "22", "23", "50"};
        String[] received = {"1", "3", "4", "2", "17", "18", "19", "20", "21", "22", "23"};
        want.exception(() ->
                        want.array(received).isEqualTo(expected, EqMode.IGNORE_ORDER)
                , AssertionError.class);
    }

    @Test
    public void lenientEq() {
        String[] expected = {"1", "2", "3", "4", "17", "18", "19", "20", "22", "23", "50"};
        String[] received = {"1", "3", "4", "2", "17", "18", "19", "20", "50", "22", "23"};

        want.array(received).eqIgnoreOrder(expected);
    }

    private static List<Employee> createEmployee() {
        List<Employee> list = new ArrayList<Employee>();
        for (int loop = 1; loop < 5; loop++) {
            Employee employee = new Employee();
            employee.setName("test name " + loop);
            list.add(employee);
        }
        return list;
    }

    private static Map<String, String> createStringMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "test1");
        map.put("key2", "test2");
        return map;
    }

    private static List<Map<String, String>> createMapList() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int loop = 1; loop < 5; loop++) {
            Map<String, String> map = createStringMap();
            list.add(map);
        }
        return list;
    }

    @Test
    public void testEvaluatesToTheTheLogicalConjunctionOfManyOtherMatchers() {
        assertThat("good", allOf(equalTo("good"), equalTo("good"), equalTo("good"), equalTo("good"), equalTo("good")));
    }

    @Test
    public void testPropertyEq() {
        Map<String, String> map = new HashMap<String, String>() {
            private static final long serialVersionUID = 1951222957450829884L;

            {
                put("wikiName", "my name");
                put("age", "34");
            }
        };
        want.object(map).eqByProperties(new String[]{"wikiName", "age"}, new String[]{"my name", "34"});
    }

    @Test
    public void testPropertyEq_Error() {
        Map<String, String> map = new HashMap<String, String>() {
            private static final long serialVersionUID = 1951222957450829884L;

            {
                put("wikiName", "my name");
                put("age", "34");
            }
        };
        want.exception(() ->
                        want.object(map).eqByProperties(new String[]{"wikiName", "age"}, new String[]{"my name", "35"})
                , AssertionError.class);
    }

    @Test
    public void testPropertyEq_Exception() {
        Map<String, String> map = new HashMap<String, String>() {
            private static final long serialVersionUID = 1951222957450829884L;

            {
                put("wikiName", "my name");
                put("age", "34");
            }
        };
        want.exception(() ->
                        want.object(map).eqByProperties(new String[]{"wikiName", "age"}, new String[]{"my name"})
                , AssertionError.class);
    }

    @Test
    public void testAllPropertyMatch() {
        User[] users = new User[]{new User("adabcd", ""), new User("aaaabceee", "")};
        want.array(users).propertyMatch(ItemsMode.AllItems, "first", the.string().contains("abc"));
    }

    @Test
    public void testPropertiesMatch() {
        List<User> users = Arrays.asList(new User("dfa123sdf", "abc"), new User("firs123tname", "abc"));
        want.collection(users).propertyMatch(ItemsMode.AllItems, "first", the.string().contains("123"))
                .eqByProperties("last", new String[]{"abc", "abc"});
    }

    @Test
    public void testPropertiesMatch_singlevalue() {
        User user = new User("df123asdf", "abc");
        want.object(user).propertyMatch("first", the.string().contains("123")).eqByProperties("last", "abc");
    }

    @Test
    public void testPropertiesMatch_simplevalue() {
        User user = new User("df123asdf", "abc");
        want.object(user).propertyMatch("first", the.string().contains("123")).eqByProperties("last", "abc");
    }

    /**
     * 单个StringMode参数的case
     */
    @Test
    public void testPropertyEq_StringMode() {
        User user = new User("df123 asdf", "abc");
        want.object(user).eqByProperties("first", "df123asdf", StringMode.IgnoreSpace);
    }

    /**
     * 多个StringMode的case
     */
    @Test
    public void testPropertyEq_StringModes() {
        User user = new User("df123 ASDF", "abc");
        want.object(user).eqByProperties("first", "df123asdf", StringMode.IgnoreSpace, StringMode.IgnoreCase);
    }
}
