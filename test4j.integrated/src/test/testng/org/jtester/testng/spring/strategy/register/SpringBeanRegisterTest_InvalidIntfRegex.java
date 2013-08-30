package org.jtester.testng.spring.strategy.register;

import mockit.Mock;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.tracer.TracerHelper;
import org.jtester.testng.JTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*222Service") })
public class SpringBeanRegisterTest_InvalidIntfRegex extends JTester implements IDatabase, ISpring {
	@SpringBeanByName
	private UserService userService;

	@SpringBeanByName
	private UserAnotherDao userAnotherDao;

	@BeforeMethod
	public void disabledTracer() {
		new MockUp<TracerHelper>() {
			@Mock
			public boolean doesTracerEnabled() {
				return false;
			}
		};
	}

	@Test(description = "查找userAnotherDao的实现类失败的case", groups = "for-test")
	public void getSpringBean() {
		want.object(userService).notNull();
		Object o = spring.getBean("userAnotherDao");
		want.object(o).same(userAnotherDao);

		Object userDao = spring.getBean("userDao");
		want.object(userDao).notNull();
	}
}
