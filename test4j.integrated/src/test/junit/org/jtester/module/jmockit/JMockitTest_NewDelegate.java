package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.junit.Test;

@SuppressWarnings("unused")
@AutoBeanInject
public class JMockitTest_NewDelegate implements JTester {

    @Mocked
    IPrinter printer;

    /**
     * 验证jMockit的断言异常不会被覆盖
     */
    @Test
    public void testNewDelegate() {
        new Expectations() {
            {
                printer.print(any(String.class));
                result = new Delegate() {
                    public void print(String text) {
                        want.string(text).eq("one1");
                    }
                };

                printer.print(any(String.class));
                result = new Delegate() {
                    public void print(String text) {
                        want.string(text).eq("two");
                    }
                };
            }
        };
        try {
            printer.print("one");

            want.fail();
        } catch (AssertionError error) {
            String message = error.getMessage();
            want.string(message).eq("Expected: \"one1\" but: was \"one\"", StringMode.IgnoreSpace);
        }
        printer.print("two");
    }
}

interface IPrinter {
    void print(String text);
}
