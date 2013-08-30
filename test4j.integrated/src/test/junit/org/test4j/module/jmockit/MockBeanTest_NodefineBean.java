package org.test4j.module.jmockit;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.formock.SomeInterface;
import org.test4j.fortest.formock.SpringBeanService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/springbean service.xml" })
public class MockBeanTest_NodefineBean implements Test4J {
    @SpringBeanByName
    private SpringBeanService springBeanService1;

    @Mocked
    @SpringBeanFrom
    protected SomeInterface   dependency1;

    @SpringBeanFrom
    @Mocked
    protected SomeInterface   dependency2;

    @Test
    public void testDependency() {
        want.object(springBeanService1).notNull();
        want.object(springBeanService1.getDependency1()).notNull();
        want.object(springBeanService1.getDependency2()).notNull();
    }
}
