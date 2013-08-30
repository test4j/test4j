package org.jtester.module.spring.utility;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class SpringInitHelperTest implements JTester {
    @Test
    public void testInvokeSpringInitMethod() {
        want.string(privateMethod).isEqualTo("privateMethod");
        want.string(protectedMethod).isEqualTo("protectedMethod");
    }

    private static String privateMethod = "unknown";

    @SpringInitMethod
    private void privateMethod() {
        privateMethod = "privateMethod";
    }

    private static String protectedMethod = "unknown";

    @SpringInitMethod
    protected void protectedMethod() {
        protectedMethod = "protectedMethod";
    }
}
