package org.test4j.module.spring.utility;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringInitMethod;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/beans.xml",
        "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
public class SpringInitHelperTest implements Test4J {
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
