package org.test4j.module.jmockit;

import mockit.NonStrict;

import org.junit.Test;
import org.test4j.junit.JTester;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.jmockit.extend.JMocketVerifications;

public class JMocketVerificationsTest implements JTester {

    @NonStrict
    Hello hello1;

    @Test
    public void testVerifyApi() {
        Hello hello = new Hello();
        hello.sayHello("darui.wu");

        new JMocketVerifications() {
            {
                hello1.sayHello(with("1", the.string().contains("wu")));
                times = 1;
            }
        };
    }

    public static class Hello {
        void sayHello(String name) {
            MessageHelper.info("hello world, " + name);
        }
    }
}
