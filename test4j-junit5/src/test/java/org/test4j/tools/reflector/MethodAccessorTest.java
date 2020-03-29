package org.test4j.tools.reflector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.exception.NoSuchMethodRuntimeException;
import org.test4j.junit5.Test4J;
import org.test4j.reflector.TestObject;

@SuppressWarnings({"rawtypes"})
public class MethodAccessorTest extends Test4J {
    TestObject test = null;

    private MethodAccessor getPrivate;

    private MethodAccessor setPrivate;

    @BeforeEach
    public void setUp() throws Exception {
        test = new TestObject();
        getPrivate = MethodAccessor.method(test, "getPrivate", new Class[0]);
        setPrivate = MethodAccessor.method(test, "setPrivate", new Class[]{int.class});
    }

    @AfterEach
    public void tearDown() throws Exception {
        getPrivate = null;
        setPrivate = null;
    }

    @Test
    public void testMethodAccessor1() {
        want.exception(() ->
                        MethodAccessor.method(new Object(), "missing")
                , NoSuchMethodRuntimeException.class);
    }

    @Test
    public void testMethodAccessor2() {
        want.exception(() ->
                        MethodAccessor.method(new TestObject(), "missing")
                , NoSuchMethodRuntimeException.class);
    }

    @Test
    public void testMethodAccessor3() {
        want.exception(() ->
                        MethodAccessor.method(null, "missing")
                , NullPointerException.class);
    }

    /**
     * Test method for
     * .
     *
     * @throws Exception
     * @throws Throwable
     */
    @Test
    public void testInvoke() throws Exception {
        int expected = 26071973;
        want.number(getPrivate.<Integer>invoke(test, new Object[0]).intValue()).isEqualTo(expected);

        int newValue = 26072007;
        want.number(setPrivate.<Integer>invoke(test, new Object[]{newValue})).isEqualTo(expected);
        want.number(getPrivate.<Integer>invoke(test, new Object[]{})).isEqualTo(newValue);
    }

    @ParameterizedTest
    @MethodSource("invokeMethodData")
    public void testInvokeMethod(String methodName, String value) throws Exception {
        MethodAccessor accessor = MethodAccessor.method(ChildClaz.class, methodName);
        accessor.invoke(new ChildClaz(), new Object[0]);
        String result = ParentClaz.method;
        want.string(result).isEqualTo(value);
    }

    public static Object[][] invokeMethodData() {
        return new Object[][]{{"parentStaticMethod", "parentStaticMethod"},// <br>
                {"childStaticMethod", "childStaticMethod"},// <br>
                {"staticMethod", "child static method"},// <br>
                {"setMethod", "child set method"},// <br>
                {"setParentMethod", "parent set method"}};
    }

    @SuppressWarnings("unused")
    public static class ParentClaz {

        public static String method = null;

        private static void parentStaticMethod() {
            method = "parentStaticMethod";
        }

        private static void staticMethod() {
            method = "parent static method";
        }

        private void setMethod() {
            method = "parent set method";
        }

        private void setParentMethod() {
            method = "parent set method";
        }
    }

    @SuppressWarnings("unused")
    public static class ChildClaz extends ParentClaz {
        private static void childStaticMethod() {
            method = "childStaticMethod";
        }

        private static void staticMethod() {
            method = "child static method";
        }

        private void setMethod() {
            method = "child set method";
        }
    }
}
