package org.test4j.module;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class Test4JExceptionTest extends Test4J {
    @Test(expectedExceptions = { Test4JException.class })
    public void exception1() {
        throw new Test4JException("message");
    }

    @Test(expectedExceptions = { Test4JException.class })
    public void exception2() {
        throw new Test4JException(new Exception("message"));
    }

    @Test(expectedExceptions = { Test4JException.class })
    public void exception3() {
        throw new Test4JException("message", new Exception("message"));
    }
}
