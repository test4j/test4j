package org.jtester.module.spring.utility;

import java.lang.reflect.Field;

import mockit.NonStrict;

import org.jtester.junit.JTester;
import org.jtester.module.jmockit.utility.JMockitModuleHelper;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.junit.Test;

public class SpringModuleHelperTest_JMockit implements JTester {

    @Test
    public void testDoesSpringBeanFieldIllegal() throws Exception {
        Field field = TestedClazz.class.getDeclaredField("field");
        want.object(field).notNull();
        try {
            JMockitModuleHelper.doesSpringBeanFieldIllegal(field);
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).containsInOrder("@SpringBeanByName/@SpringBeanByType", "@NonStrict");
        }
    }
}

class TestedClazz {
    @SpringBeanByName
    @NonStrict
    Object field;
}
