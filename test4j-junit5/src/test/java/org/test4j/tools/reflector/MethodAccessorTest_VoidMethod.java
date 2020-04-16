package org.test4j.tools.reflector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test4j.exception.Test4JException;
import org.test4j.junit5.Test4J;
import org.test4j.reflector.TestObject;

public class MethodAccessorTest_VoidMethod extends Test4J {

    private MethodAccessor throwingMethod;

    @BeforeEach
    public void setUp() {
        throwingMethod = MethodAccessor.method(TestObject.class, "throwingMethod");
    }

    @AfterEach
    public void tearDown() {
        throwingMethod = null;
    }

    /**
     * Test method for
     * .
     *
     * @throws Exception
     */
    @Test
    public void testInvoke() throws Exception {
        Object target = new TestObject();
        MethodAccessor.method(target, "nonThrowingMethod").invoke(target, new Object[0]);
        want.exception(() ->
                        throwingMethod.invoke(target, new Object[0])
                , Test4JException.class);
        want.exception(() ->
                        throwingMethod.invoke(target, new Object[]{"wrong"})
                , IllegalArgumentException.class);
    }
}
