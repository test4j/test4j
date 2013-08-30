package org.jtester.module.spring.annotations;

import mockit.Mocked;

import org.jtester.fortest.hibernate.AddressService;
import org.jtester.fortest.hibernate.UserService;
import org.junit.Ignore;
import org.junit.Test;
import org.test4j.junit.JTester;
import org.test4j.module.inject.annotations.Inject;

@Ignore
@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project.xml" })
public class UserServiceTest_Mock1 implements JTester {
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
