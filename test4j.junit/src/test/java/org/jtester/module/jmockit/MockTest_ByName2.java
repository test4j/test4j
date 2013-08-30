package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.formock.SomeInterface;
import org.jtester.fortest.formock.SpringBeanService;
import org.jtester.fortest.formock.SpringBeanService.SpringBeanServiceImpl1;
import org.jtester.junit.JTester;
import org.jtester.module.inject.annotations.Inject;
import org.junit.Test;

public class MockTest_ByName2 implements JTester {

    private SpringBeanService springBeanService1 = new SpringBeanServiceImpl1();

    private SpringBeanService springBeanService2 = new SpringBeanServiceImpl1();

    @Inject(targets = { "springBeanService1", "springBeanService2" }, properties = { "dependency1", "dependency1" })
    @Mocked
    private SomeInterface     someInterface1;

    @Inject(targets = { "springBeanService1", "springBeanService2" }, properties = { "dependency2", "dependency2" })
    @Mocked
    private SomeInterface     someInterface2;

    @Test
    public void testMock_ByName() {
        want.object(springBeanService1.getDependency1()).not(the.object().same(someInterface1));
        want.object(springBeanService1.getDependency2()).not(the.object().same(someInterface2));

        want.object(springBeanService2.getDependency1()).not(the.object().same(someInterface1));
        want.object(springBeanService2.getDependency2()).not(the.object().same(someInterface2));
    }
}
