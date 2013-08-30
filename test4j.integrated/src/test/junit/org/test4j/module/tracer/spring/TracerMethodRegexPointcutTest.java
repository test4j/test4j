package org.test4j.module.tracer.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.strategy.Test4JSpringContext;

public class TracerMethodRegexPointcutTest implements Test4J {
    @SpringBeanByName(claz = FinalUserDao.class)
    UserAnotherDao userAnotherDao;

    /**
     * 测试@Tracer可以过滤 final的类的aop
     */
    @Test
    // (description = "测试在cglib加强的情况下,final类型的类被忽略")
    public void testGetClassFilter() {
        String[] locations = new String[] { "org/test4j/module/spring/testedbeans/xml/beans.xml",
                "org/test4j/module/spring/testedbeans/xml/data-source.cglib.xml" };
        ClassPathXmlApplicationContext context = new Test4JSpringContext(locations, true);
        context.refresh();
        UserAnotherDao bean = (UserAnotherDao) context.getBean("userAnotherDao");
        want.object(bean).notNull();
    }
}
