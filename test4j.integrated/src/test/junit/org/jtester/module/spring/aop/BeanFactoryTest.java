package org.jtester.module.spring.aop;

import org.jtester.fortest.service.UserAnotherDaoImpl;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.tracer.spring.SpringBeanTracer;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class BeanFactoryTest implements JTester {
    @Test
    public void testXmlBeanDefinitionReader() {
        Resource beanRes = new ClassPathResource("org/jtester/module/spring/testedbeans/xml/beans.xml");
        Resource dbRes = new ClassPathResource("org/jtester/module/spring/testedbeans/xml/data-source.xml");

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions(new Resource[] { beanRes, dbRes });
        BeanDefinitionRegistry registry = reader.getRegistry();
        want.bool(registry.containsBeanDefinition("userDao")).is(true);
        want.bool(registry.containsBeanDefinition("userAnotherDao")).is(false);

        registry.registerBeanDefinition("userAnotherDao", new RootBeanDefinition(UserAnotherDaoImpl.class,
                AbstractBeanDefinition.AUTOWIRE_BY_NAME));
        want.bool(registry.containsBeanDefinition("userAnotherDao")).is(true);

        UserService userService = (UserService) factory.getBean("userService");
        want.object(userService).notNull();
        want.object(userService).propertyMatch("userAnotherDao", the.object().notNull());
    }

    @Test
    public void testClassPathContext_autoTracer() {
        Resource beanRes = new ClassPathResource("org/jtester/module/spring/testedbeans/xml/beans.xml");
        Resource dbRes = new ClassPathResource("org/jtester/module/spring/testedbeans/xml/data-source.xml");

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions(new Resource[] { beanRes, dbRes });
        BeanDefinitionRegistry registry = reader.getRegistry();
        SpringBeanTracer.addTracerBeanDefinition(registry);

        Object pointcut = factory.getBean("jtester-internal-methodname-pointcut");
        want.object(pointcut).notNull();
        Object advice = factory.getBean("jtester-internal-springbeantracer");
        want.object(advice).notNull();
        Object advisor = factory.getBean("jtester-internal-beantracer-advisor");
        want.object(advisor).notNull();
        UserService userService = (UserService) factory.getBean("userService");
        want.object(userService).notNull();
    }

    @Test
    public void testClassPathContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "org/jtester/module/spring/testedbeans/xml/data-source.xml",
                "org/jtester/module/spring/testedbeans/xml/beans.xml",
                "org/jtester/module/tracer/spring/jtester-bean-tracer.xml" });
        boolean exists = context.containsBeanDefinition("jtester-internal-beantracer-advisor");
        want.bool(exists).isEqualTo(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        BeanDefinition pointcut = beanFactory.getBeanDefinition("jtester-internal-methodname-pointcut");
        want.object(pointcut).notNull();
        BeanDefinition advice = beanFactory.getBeanDefinition("jtester-internal-springbeantracer");
        want.object(advice).notNull();
        BeanDefinition advisor = beanFactory.getBeanDefinition("jtester-internal-beantracer-advisor");
        want.object(advisor).notNull();
    }
}
