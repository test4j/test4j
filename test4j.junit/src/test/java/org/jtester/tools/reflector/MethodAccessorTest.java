package org.jtester.tools.reflector;

import org.jtester.fortest.reflector.TestObject;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.jtester.tools.exception.NoSuchMethodRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "rawtypes" })
public class MethodAccessorTest implements JTester {
    TestObject                      test = null;

    private MethodAccessor<Integer> getPrivate;

    private MethodAccessor<Integer> setPrivate;

    @Before
    public void setUp() throws Exception {
        test = new TestObject();
        getPrivate = new MethodAccessor<Integer>(test, "getPrivate", new Class[0]);
        setPrivate = new MethodAccessor<Integer>(test, "setPrivate", new Class[] { int.class });
    }

    @After
    public void tearDown() throws Exception {
        getPrivate = null;
        setPrivate = null;
    }

    @Test(expected = NoSuchMethodRuntimeException.class)
    public void testMethodAccessor1() {
        new MethodAccessor<Void>(new Object(), "missing");
    }

    @Test(expected = NoSuchMethodRuntimeException.class)
    public void testMethodAccessor2() {
        new MethodAccessor<Void>(new TestObject(), "missing");
    }

    @Test(expected = NullPointerException.class)
    public void testMethodAccessor3() {
        new MethodAccessor(null, "missing");
    }

    /**
     * Test method for
     * {@link com.j2speed.accessor.AbstractMethodAccessor#invokeBase(java.lang.Object[])}
     * .
     * 
     * @throws Exception
     * @throws Throwable
     */
    @Test
    public void testInvoke() throws Exception {
        int expected = 26071973;
        want.number(getPrivate.invoke(test, new Object[0]).intValue()).isEqualTo(expected);

        int newValue = 26072007;
        want.number(setPrivate.invoke(test, new Object[] { newValue })).isEqualTo(expected);
        want.number(getPrivate.invoke(test, new Object[] {})).isEqualTo(newValue);
    }

    @Test
    @DataFrom("invokeMethodData")
    public void testInvokeMethod(String methodName, String value) throws Exception {
        MethodAccessor<Void> accessor = new MethodAccessor<Void>(ChildClaz.class, methodName);
        accessor.invoke(new ChildClaz(), new Object[0]);
        String result = ParentClaz.method;
        want.string(result).isEqualTo(value);
    }

    public static Object[][] invokeMethodData() {
        return new Object[][] { { "parentStaticMethod", "parentStaticMethod" },// <br>
                { "childStaticMethod", "childStaticMethod" },// <br>
                { "staticMethod", "child static method" },// <br>
                { "setMethod", "child set method" },// <br>
                { "setParentMethod", "parent set method" } };
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
