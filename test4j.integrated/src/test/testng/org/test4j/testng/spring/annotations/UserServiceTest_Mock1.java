package org.test4j.testng.spring.annotations;

import mockit.Mocked;

import org.test4j.fortest.hibernate.AddressService;
import org.test4j.fortest.hibernate.UserService;
import org.test4j.module.inject.annotations.Inject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "classpath:/org/test4j/fortest/hibernate/project.xml" })
@Test(groups = { "test4j", "hibernate" })
public class UserServiceTest_Mock1 extends Test4J {
    @SpringBeanByName("userService")
    private UserService    userService;

    @Mocked
    @Inject(targets = "userService")
    private AddressService addressService;

    @Test
    public void findAddress() {
        want.object(addressService).notNull();
        want.object(userService).notNull();
        new Expectations() {
            {
                when(addressService.findAddress()).thenReturn("文二路120#");
            }
        };
        String address = userService.findAddress();
        want.string(address).contains("120#");
    }

    @Test
    public void findAddress02() {
        want.object(addressService).notNull();
        want.object(userService).notNull();
        new Expectations() {
            {
                when(addressService.findAddress()).thenReturn("文二路120#");
            }
        };
        String address = userService.findAddress();
        want.string(address).contains("120#");
    }
}
