package org.test4j.hamcrest.matcher.property;

import static org.test4j.tools.commons.ArrayHelper.toArray;
import static org.test4j.tools.commons.ListHelper.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.GenicBean;
import org.test4j.model.User;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;

import org.test4j.tools.datagen.DataProvider;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class PropertiesEqualMatcherTest extends Test4J {
    @Test
    public void testProperEqual_ComplexAndIgnoreAll() {
        GenicBean[] actuals = new GenicBean[]{GenicBean.newInstance("bean1", newUser("darui.wu")),// <br>
                GenicBean.newInstance("bean2", newMap("map2")) // <br>
        };
        List expected = new ArrayList() {
            {
                add(GenicBean.newInstance("bean2", null));
                add(ListHelper.toList("bean1", "darui.wu"));
            }
        };
        PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected,
                new String[]{"name", "refObject.name"}, new EqMode[]{EqMode.IGNORE_ORDER, EqMode.IGNORE_DEFAULTS});
        MatcherAssert.assertThat(actuals, matcher);
    }

    @Test
    public void testProperEqual_ComplexAndIgnoreAll2() {
        GenicBean[] actuals = new GenicBean[]{GenicBean.newInstance("bean1", toList("list1", "list2")),
                GenicBean.newInstance("bean2", toList("list3", "list4")),
                GenicBean.newInstance("bean3", newUser("darui.wu")),
                GenicBean.newInstance("bean4", toList("list5", "list6"))};
        List expected = new ArrayList() {
            {
                add(toList("bean3", newUser("darui.wu")));
                add(GenicBean.newInstance("bean2", new String[]{"list4", "list3"}));
                add(GenicBean.newInstance("bean1", new String[]{"list1", null}));
                add(GenicBean.newInstance("bean4", new String[]{null, "list6"}));
            }
        };
        PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected, new String[]{"name", "refObject"},
                new EqMode[]{EqMode.IGNORE_ORDER, EqMode.IGNORE_DEFAULTS});
        MatcherAssert.assertThat(actuals, matcher);
    }

    @ParameterizedTest
    @MethodSource("matchData")
    public void testProperEqual(Object actual, Object expected, String properties, EqMode[] modes, boolean match) {
        String[] props = properties.split(",");
        PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected, props, modes);
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
                data(newUser("abc"), toList(123, "abc"), "id,name", null, true);
                data(newUser("abc"), toList("abc", 123), "id,name", null, false);
                data(newUser("abc"), toList("abc", 123), "id,name", new EqMode[]{EqMode.IGNORE_ORDER}, true);
                data(newUser("abc"), newMap("abc"), "id,name", null, true);
                data(toList(newUser("abc"), newUser("darui.wu")), toArray("abc", "darui.wu"), "name", null, true);
                data(ArrayHelper.toArray(newMap("abc"), newUser("darui.wu")),
                        toList(newUser("abc"), newMap("darui.wu")), "name", null, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), null), "name",
                        new EqMode[]{EqMode.IGNORE_DEFAULTS}, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("darui.wu"), newMap("abc")), "name",
                        new EqMode[]{EqMode.IGNORE_ORDER}, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap("darui.wu")),
                        "id,name", null, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newUser("abc"), newMap(null)), "id,name",
                        new EqMode[]{EqMode.IGNORE_DEFAULTS}, true);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newMap(null), newUser("abc")), "id,name",
                        new EqMode[]{EqMode.IGNORE_DEFAULTS}, false);
                data(toArray(newMap("abc"), newUser("darui.wu")), toList(newMap(null), newUser("abc")), "id,name",
                        new EqMode[]{EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_ORDER}, false);
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
