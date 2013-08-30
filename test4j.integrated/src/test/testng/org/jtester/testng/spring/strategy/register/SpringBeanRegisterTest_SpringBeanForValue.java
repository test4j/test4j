package org.jtester.testng.spring.strategy.register;

import org.jtester.fortest.service.BeanClazzUserServiceImpl;
import org.jtester.fortest.service.UserService;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
@Test(groups = "jtester")
public class SpringBeanRegisterTest_SpringBeanForValue extends JTester {

	@SpringBeanByName(claz = BeanClazzUserServiceImpl.class)
	private UserService userService1;

	@SpringBeanByName("userService")
	private UserService userService2;

	public void getSpringBean() {
		String serviceName1 = userService1.getServiceName();
		want.string(serviceName1).isEqualTo("BeanClazzUserServiceImpl");

		String serviceName2 = userService2.getServiceName();
		want.string(serviceName2).isEqualTo("UserServiceImpl");
	}
}
