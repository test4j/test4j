package org.jtester.testng.spring.annotations;

import mockit.Mocked;

import org.jtester.fortest.hibernate.AddressService;
import org.jtester.fortest.hibernate.UserService;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project.xml" })
@Test(groups = { "jtester", "hibernate" })
public class UserServiceTest_Mock2 extends JTester {
	@SpringBeanByName("userService")
	private UserService userService1;

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
