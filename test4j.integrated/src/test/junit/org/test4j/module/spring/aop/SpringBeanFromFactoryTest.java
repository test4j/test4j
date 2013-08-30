package org.test4j.module.spring.aop;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.JTester;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/jtester/module/spring/testedbeans/aop/proxybeans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class SpringBeanFromFactoryTest implements JTester {

    UserDao userDao = new UserDao() {
                        public List<User> findUserByPostcode(String postcode) {
                            return null;
                        }

                        public void insertUser(User user) {

                        }

                        public List<User> findAllUser() {
                            MessageHelper.info("find all user");
                            return Arrays.asList(new User(), new User());
                        }

                        public int partialNotMock() {
                            return 0;
                        }

                    };

    @Test
    public void test() {
        UserService userService = spring.getBean("userService");
        List<User> users = userService.findAllUser();
        want.collection(users).sizeEq(2);
    }
}
