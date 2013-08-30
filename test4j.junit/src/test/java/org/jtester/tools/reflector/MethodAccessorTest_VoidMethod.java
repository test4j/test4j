package org.jtester.tools.reflector;

import org.jtester.fortest.reflector.TestException;
import org.jtester.fortest.reflector.TestObject;
import org.jtester.junit.JTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MethodAccessorTest_VoidMethod implements JTester {

    private MethodAccessor<Void> throwingMethod;

    @Before
    public void setUp() {
        throwingMethod = new MethodAccessor<Void>(TestObject.class, "throwingMethod");
    }

    @After
    public void tearDown() {
        throwingMethod = null;
    }

    /**
     * Test method for
     * {@link com.j2speed.accessor.AbstractMethodAccessor#invokeBase(java.lang.Object[])}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testInvoke() throws Exception {
        Object target = new TestObject();
        new MethodAccessor<Void>(target, "nonThrowingMethod").invoke(target, new Object[0]);
        try {
            throwingMethod.invoke(target, new Object[0]);
            want.fail("Expected test exception");
        } catch (Exception e) {
            want.object(e).clazIs(TestException.class);
        }
        try {
            throwingMethod.invoke(target, new Object[] { "wrong" });
            want.fail("Expected test exception");
        } catch (Throwable e) {
            want.object(e).clazIs(IllegalArgumentException.class);
        }
    }
}
