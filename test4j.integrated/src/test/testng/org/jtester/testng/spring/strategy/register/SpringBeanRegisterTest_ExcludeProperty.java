package org.jtester.testng.spring.strategy.register;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") }, excludeProperties = { "userDao" })
@Test(groups = "jtester")
public class SpringBeanRegisterTest_ExcludeProperty extends JTester implements IDatabase, ISpring {
	@SpringBeanByName
	private UserService userService;

	@SpringBeanByName
	private UserAnotherDao userAnotherDao;

	public void getSpringBean() {
		want.object(userService).notNull();
		Object o = spring.getBean("userAnotherDao");
		want.object(o).same(userAnotherDao);
	}

	@Test(expectedExceptions = NoSuchBeanDefinitionException.class)
	public void getSpringBean_NoSuchBean() {
		Object userDao = spring.getBean("userDao");
		want.object(userDao).notNull();
	}
}
