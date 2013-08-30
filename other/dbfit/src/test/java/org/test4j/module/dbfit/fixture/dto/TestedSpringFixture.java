package org.test4j.module.dbfit.fixture.dto;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.module.dbfit.fixture.dto.DtoPropertyFixture;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringBeanByName;

@SpringContext({ "org/test4j/fortest/spring/beans.xml", "org/test4j/fortest/spring/data-source.xml" })
public class TestedSpringFixture extends DtoPropertyFixture {
    @SpringBeanByName
    private UserService userService;

    public void insertUser(User user) {
        userService.insertUser(user);
    }
}
