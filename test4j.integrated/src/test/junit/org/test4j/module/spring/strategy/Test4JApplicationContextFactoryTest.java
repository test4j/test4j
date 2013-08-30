package org.test4j.module.spring.strategy;

import org.junit.Test;
import org.test4j.fortest.formock.SomeInterface.SomeInterfaceImpl1;
import org.test4j.fortest.formock.SomeInterface.SomeInterfaceImpl2;
import org.test4j.fortest.formock.SpringBeanService;
import org.test4j.junit.Test4J;

public class Test4JApplicationContextFactoryTest implements Test4J {
    private static final String TO_BE_OVERRIDEN_BEAN_NAME = "toBeOverriden";
    private static final String ANOTHER_BEAN_NAME         = "springBeanService";

    private Test4JSpringContext context;

    @Test
    public void testNoOverride() throws Throwable {
        // MockBeanRegister.cleanRegister();
        String[] locations = new String[] { "org/test4j/module/spring/testedbeans/xml/mock-spring-beans-test.xml" };
        context = new Test4JSpringContext(locations, true, false);

        want.object(context.getBean(TO_BE_OVERRIDEN_BEAN_NAME)).clazIs(SomeInterfaceImpl1.class);

        SpringBeanService anotherBean = (SpringBeanService) context.getBean(ANOTHER_BEAN_NAME);
        want.object(anotherBean).notNull();
        want.object(anotherBean.getDependency1()).clazIs(SomeInterfaceImpl1.class);
        want.object(anotherBean.getDependency2()).clazIs(SomeInterfaceImpl1.class);
    }

    @Test
    public void testOverride() throws Throwable {
        // MockBeanRegister.cleanRegister();
        String[] locations = new String[] { "org/test4j/module/spring/testedbeans/xml/mock-spring-beans-test.xml" };
        context = new Test4JSpringContext(locations, true, false);

        SpringBeanService anotherBean = (SpringBeanService) context.getBean(ANOTHER_BEAN_NAME);
        want.object(anotherBean).notNull();
        want.object(anotherBean.getDependency1()).clazIs(SomeInterfaceImpl1.class);
        want.object(anotherBean.getDependency2()).clazIs(SomeInterfaceImpl1.class);

        want.object(context.getBean(TO_BE_OVERRIDEN_BEAN_NAME)).clazIs(SomeInterfaceImpl1.class);
    }

    @Test(expected = AssertionError.class)
    public void testOverride_failure() throws Throwable {
        // MockBeanRegister.cleanRegister();
        String[] locations = new String[] { "org/test4j/module/spring/testedbeans/xml/mock-spring-beans-test.xml" };
        context = new Test4JSpringContext(locations, true, false);

        SpringBeanService anotherBean = (SpringBeanService) context.getBean(ANOTHER_BEAN_NAME);
        want.object(anotherBean).notNull();
        want.object(anotherBean.getDependency1()).clazIs(SomeInterfaceImpl2.class);
    }
}
