package org.test4j.hamcrest.matcher.property;

import static org.test4j.tools.commons.ArrayHelper.toArray;
import static org.test4j.tools.commons.ListHelper.toList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;

import org.test4j.junit5.Test4J;

import org.test4j.model.User;
import org.test4j.tools.datagen.DataProvider;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class PropertyEqualMatcherTest extends Test4J {

    @Test
    public void testProperIsArray() {
        Map actual = new HashMap() {
            {
                this.put("key1", new String[]{"value1", "value2"});
            }
        };
        want.object(actual).eqByProperties("key1", new String[]{"value1", "value2"});
    }

    @Test
    public void testProperIsArray_failure() {
        Map actual = new HashMap() {
            {
                this.put("key1", new String[]{"value1"});
            }
        };
        want.exception(() ->
                        want.object(actual).eqByProperties("key1", "value1"),
                AssertionError.class);
    }

    @Test
    public void testPropertyActualIsArray() {
        List list = toList(User.mock(124, ""), User.mock(125, ""));
        want.object(list).eqByProperties("id", toList(124, 125));

        want.object(list).eqByProperties("id", toList(User.mock(124, ""), User.mock(125, "")));
    }

    @Test
    public void testPropertyActualIsArray_Failure() {
        List list = toList(User.mock(124, ""));
        want.object(list).eqByProperties("id", User.mock(124, ""));
    }

    @Test
    public void testProper_Normal() {
        Map actual = new HashMap() {
            {
                this.put("key1", "value1");
            }
        };
        want.object(actual).eqByProperties("key1", "value1");
    }

    @Test
    public void testProper_NormalPoJo() {
        User user = User.mock(125, "darui.wu");
        want.object(user).eqByProperties("name", "darui.wu").eqByProperties("id", new HashMap() {
            {
                put("id", 125);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("matchData")
    public void testProperEqual(Object actual, Object expected, String property, EqMode[] modes, boolean match) {
        PropertyEqualMatcher matcher = new PropertyEqualMatcher(expected, property, modes);
        try {
            MatcherAssert.assertThat(actual, matcher);
            want.bool(match).isEqualTo(true);
        } catch (AssertionError error) {
            error.printStackTrace();
            want.bool(match).isEqualTo(false);
        }
    }

    public static Iterator matchData() {
        return new DataProvider() {
            {
                data(newUser("abc"), "abc", "name", null, true);
                data(newUser("abc"), null, "name", null, false);
                data(newUser("abc"), null, "name", new EqMode[]{EqMode.IGNORE_DEFAULTS}, true);
                data(newUser("abc"), newUser("abc"), "name", null, true);
                data(newUser("abc"), "abc", "name", null, true);
                data(newUser("abc"), newMap("abc"), "name", null, true);
                data(toList(newUser("abc"), newUser("darui.wu")), toArray("abc", "darui.wu"), "name", null, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap("darui.wu")), "name",
                        null, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), null), "name",
                        new EqMode[]{EqMode.IGNORE_DEFAULTS}, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("darui.wu"), newMap("abc")), "name",
                        new EqMode[]{EqMode.IGNORE_ORDER}, true);
            }
        };
    }

    private static User newUser(String name) {
        return User.mock(123, name);
    }

    private static Map newMap(final String name) {
        return new HashMap() {
            {
                put("id", 123);
                put("name", name);
            }
        };
    }
}
