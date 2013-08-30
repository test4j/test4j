package org.test4j.module;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.Test4JException;

public class Test4JExceptionTest implements Test4J {
    @Test(expected = Test4JException.class)
    public void exception1() {
        throw new Test4JException("message");
    }

    @Test(expected = Test4JException.class)
    public void exception2() {
        throw new Test4JException(new Exception("message"));
    }

    @Test(expected = Test4JException.class)
    public void exception3() {
        throw new Test4JException("message", new Exception("message"));
    }
}
