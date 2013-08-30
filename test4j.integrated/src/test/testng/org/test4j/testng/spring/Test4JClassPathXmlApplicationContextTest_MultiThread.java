package org.test4j.testng.spring;

import java.util.List;

import mockit.NonStrict;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserDaoImpl;
import org.test4j.fortest.service.UserService;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = @BeanMap(intf = "**.UserAnotherDao", impl = "**.UserAnotherDaoImpl"))
@Test(groups = "test4j")
@SuppressWarnings("unused")
public class Test4JClassPathXmlApplicationContextTest_MultiThread extends Test4J implements IDatabase, ISpring {
    @SpringBeanFrom
    @NonStrict
    private UserService userService;

    @SpringBeanByName(claz = UserDaoImpl.class)
    private UserDao     userDao;

    int                 count = 0;

    /**
     * 测试在多线程中获取mocked bean
     * 
     * @throws InterruptedException
     */
    @Test
    // (groups = "for-test")
    public void testGetBean() throws InterruptedException {
        count = 0;
        new Expectations() {
            {
                userService.findAllUser();
                result = new Delegate() {
                    public List<User> findAllUser() {
                        count++;
                        return null;
                    }
                };
            }
        };
        userService.findAllUser();
        for (int loop = 0; loop < 10; loop++) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    UserService userService = spring.getBean("userService");
                    userService.findAllUser();
                }
            };

            new Thread(runnable).start();
        }
        Thread.sleep(200);
        want.number(count).isEqualTo(11);
    }
}
