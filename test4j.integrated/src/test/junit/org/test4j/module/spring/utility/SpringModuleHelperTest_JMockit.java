package org.test4j.module.spring.utility;

import java.lang.reflect.Field;

import mockit.NonStrict;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.jmockit.utility.JMockitModuleHelper;
import org.test4j.module.spring.annotations.SpringBeanByName;

public class SpringModuleHelperTest_JMockit extends Test4J {

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
