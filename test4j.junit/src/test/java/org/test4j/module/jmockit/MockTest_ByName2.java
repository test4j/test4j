package org.test4j.module.jmockit;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.formock.SomeInterface;
import org.test4j.fortest.formock.SpringBeanService;
import org.test4j.fortest.formock.SpringBeanService.SpringBeanServiceImpl1;
import org.test4j.junit.Test4J;
import org.test4j.module.inject.annotations.Inject;

public class MockTest_ByName2 implements Test4J {

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
