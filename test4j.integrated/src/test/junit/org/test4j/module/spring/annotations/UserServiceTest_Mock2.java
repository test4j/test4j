package org.test4j.module.spring.annotations;

import mockit.Mocked;

import org.junit.Ignore;
import org.junit.Test;
import org.test4j.fortest.hibernate.AddressService;
import org.test4j.fortest.hibernate.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.inject.annotations.Inject;

@Ignore
@SpringContext({ "classpath:/org/test4j/fortest/hibernate/project.xml" })
public class UserServiceTest_Mock2 implements Test4J {
    @SpringBeanByName("userService")
    private UserService    userService1;

    @Mocked
    @Inject(targets = "userService1")
    private AddressService addressService1;

    @Test
    public void findAddress() {
        want.object(addressService1).notNull();
        want.object(userService1).notNull();
        new Expectations() {
            {
                when(addressService1.findAddress()).thenReturn("文三路131#");
            }
        };
        String address = userService1.findAddress();
        want.string(address).contains("131#");
    }
}
