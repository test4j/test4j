package org.test4j.testng.spring;

import mockit.Mocked;

import org.test4j.fortest.formock.SomeInterface;
import org.test4j.fortest.formock.SpringBeanService;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/mockbeans-withdependency.xml" })
@Test(groups = "manual")
public class MockBeanTest_MissingDependency extends Test4J {
    @SpringBeanByName
    private SpringBeanService springBeanService1;

    @Mocked
    protected SomeInterface   dependency2;

    public void testDependency_MockBean() {
        want.object(springBeanService1.getDependency1()).notNull();
        want.object(springBeanService1.getDependency2()).notNull();
    }

    @Mocked
    private SpringBeanService springBeanService2;

    @Test
    public void testDependency_UnMockBean() {
        new Expectations() {
            {
                when(springBeanService2.getDependency1()).thenReturn(dependency2);
                when(springBeanService2.getDependency2()).thenReturn(dependency2);
            }
        };
        want.object(springBeanService2.getDependency1()).notNull();
        want.object(springBeanService2.getDependency2()).notNull();
    }
}
