package org.jtester.module.spring.annotations;

import mockit.Mocked;

import org.jtester.fortest.hibernate.AddressService;
import org.jtester.fortest.hibernate.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.inject.annotations.Inject;
import org.junit.Ignore;
import org.junit.Test;

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
