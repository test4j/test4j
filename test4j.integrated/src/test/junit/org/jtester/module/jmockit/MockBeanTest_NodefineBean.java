package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.formock.SomeInterface;
import org.jtester.fortest.formock.SpringBeanService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/springbean service.xml" })
public class MockBeanTest_NodefineBean implements JTester {
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
