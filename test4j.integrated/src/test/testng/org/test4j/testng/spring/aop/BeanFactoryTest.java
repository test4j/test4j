package org.test4j.testng.spring.aop;

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
import org.test4j.fortest.service.UserAnotherDaoImpl;
import org.test4j.fortest.service.UserService;
import org.test4j.module.tracer.spring.SpringBeanTracer;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class BeanFactoryTest extends Test4J {
    @Test
    public void testXmlBeanDefinitionReader() {
        Resource beanRes = new ClassPathResource("org/test4j/module/spring/testedbeans/xml/beans.xml");
        Resource dbRes = new ClassPathResource("org/test4j/module/spring/testedbeans/xml/data-source.xml");

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
        Resource beanRes = new ClassPathResource("org/test4j/module/spring/testedbeans/xml/beans.xml");
        Resource dbRes = new ClassPathResource("org/test4j/module/spring/testedbeans/xml/data-source.xml");

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions(new Resource[] { beanRes, dbRes });
        BeanDefinitionRegistry registry = reader.getRegistry();
        SpringBeanTracer.addTracerBeanDefinition(registry);

        Object pointcut = factory.getBean("test4j-internal-methodname-pointcut");
        want.object(pointcut).notNull();
        Object advice = factory.getBean("test4j-internal-springbeantracer");
        want.object(advice).notNull();
        Object advisor = factory.getBean("test4j-internal-beantracer-advisor");
        want.object(advisor).notNull();
        UserService userService = (UserService) factory.getBean("userService");
        want.object(userService).notNull();
    }

    @Test
    public void testClassPathContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "org/test4j/module/spring/testedbeans/xml/data-source.xml",
                "org/test4j/module/spring/testedbeans/xml/beans.xml",
                "org/test4j/module/tracer/spring/test4j-bean-tracer.xml" });
        boolean exists = context.containsBeanDefinition("test4j-internal-beantracer-advisor");
        want.bool(exists).isEqualTo(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        BeanDefinition pointcut = beanFactory.getBeanDefinition("test4j-internal-methodname-pointcut");
        want.object(pointcut).notNull();
        BeanDefinition advice = beanFactory.getBeanDefinition("test4j-internal-springbeantracer");
        want.object(advice).notNull();
        BeanDefinition advisor = beanFactory.getBeanDefinition("test4j-internal-beantracer-advisor");
        want.object(advisor).notNull();
    }
}
