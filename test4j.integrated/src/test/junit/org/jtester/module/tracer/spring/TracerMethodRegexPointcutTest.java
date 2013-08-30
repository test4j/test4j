package org.jtester.module.tracer.spring;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.strategy.JTesterSpringContext;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TracerMethodRegexPointcutTest implements JTester {
    @SpringBeanByName(claz = FinalUserDao.class)
    UserAnotherDao userAnotherDao;

    /**
     * 测试@Tracer可以过滤 final的类的aop
     */
    @Test
    // (description = "测试在cglib加强的情况下,final类型的类被忽略")
    public void testGetClassFilter() {
        String[] locations = new String[] { "org/jtester/module/spring/testedbeans/xml/beans.xml",
                "org/jtester/module/spring/testedbeans/xml/data-source.cglib.xml" };
        ClassPathXmlApplicationContext context = new JTesterSpringContext(locations, true);
        context.refresh();
        UserAnotherDao bean = (UserAnotherDao) context.getBean("userAnotherDao");
        want.object(bean).notNull();
    }
}
