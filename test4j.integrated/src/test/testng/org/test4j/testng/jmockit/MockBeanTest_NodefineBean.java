package org.test4j.testng.jmockit;

import mockit.Mocked;

import org.test4j.fortest.formock.SomeInterface;
import org.test4j.fortest.formock.SpringBeanService;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/springbean service.xml" })
@Test(groups = { "test4j", "mock-demo" })
public class MockBeanTest_NodefineBean extends Test4J {
    @SpringBeanByName
    private SpringBeanService springBeanService1;

    @Mocked
    @SpringBeanFrom
    protected SomeInterface   dependency1;

    @SpringBeanFrom
    @Mocked
    protected SomeInterface   dependency2;

    public void testDependency() {
        want.object(springBeanService1).notNull();
        want.object(springBeanService1.getDependency1()).notNull();
        want.object(springBeanService1.getDependency2()).notNull();
    }
}
