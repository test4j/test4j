package org.test4j.module.spring.strategy;

import org.junit.runner.RunWith;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserDaoImpl2;
import org.test4j.junit.Test4JRunner;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;

/**
 * 这个基类混合了junit和testng的运行方式
 * 
 * @author darui.wudr
 */
@RunWith(Test4JRunner.class)
@SpringContext(share = true, value = "org/test4j/module/spring/testedbeans/xml/beans.xml", allowLazy = true)
public abstract class Parent extends Test4J {

    @SpringBeanFrom
    UserDao userDao = new UserDaoImpl2();
}
