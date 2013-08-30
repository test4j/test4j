package org.jtester.module.spring;

import mockit.Mocked;

import org.jtester.fortest.formock.SomeInterface;
import org.jtester.fortest.formock.SpringBeanService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/mockbeans-withdependency.xml" })
public class MockBeanTest_MissingDependency implements JTester {
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
