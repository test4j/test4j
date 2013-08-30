package org.test4j.testng.spring.parentcontext;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.test4j.fortest.service.UserServiceImpl;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

public class SpringParentDemo extends Test4J {
    @SuppressWarnings("unused")
    @Test
    public void demoSpringParent() {
        ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext(new String[] {
                "org/test4j/module/spring/testedbeans/xml/data-source.xml",
                "org/test4j/module/spring/testedbeans/xml/beans.xml" });

        Object userService1 = parent.getBean("userService");
        want.object(userService1).notNull();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "org/test4j/module/spring/testedbeans/xml/beans-child.xml" }, parent);

        Object userService2 = context.getBean("userService");
        UserServiceImpl impl = reflector.getSpringAdvisedTarget(userService2);
        want.object(userService2).notNull();
    }
}
