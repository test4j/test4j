package org.jtester.module.spring.strategy;

import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.runner.RunWith;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserDaoImpl2;
import org.test4j.junit.JTesterRunner;
import org.test4j.testng.JTester;

/**
 * 这个基类混合了junit和testng的运行方式
 * 
 * @author darui.wudr
 */
@RunWith(JTesterRunner.class)
@SpringContext(share = true, value = "org/jtester/module/spring/testedbeans/xml/beans.xml", allowLazy = true)
public abstract class Parent extends JTester {

    @SpringBeanFrom
    UserDao userDao = new UserDaoImpl2();
}
