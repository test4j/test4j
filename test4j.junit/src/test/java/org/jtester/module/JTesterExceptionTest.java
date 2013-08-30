package org.jtester.module;

import org.jtester.junit.JTester;
import org.junit.Test;

public class JTesterExceptionTest implements JTester {
    @Test(expected = JTesterException.class)
    public void exception1() {
        throw new JTesterException("message");
    }

    @Test(expected = JTesterException.class)
    public void exception2() {
        throw new JTesterException(new Exception("message"));
    }

    @Test(expected = JTesterException.class)
    public void exception3() {
        throw new JTesterException("message", new Exception("message"));
    }
}
