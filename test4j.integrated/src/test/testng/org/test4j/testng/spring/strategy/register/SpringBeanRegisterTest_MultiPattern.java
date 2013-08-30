package org.test4j.testng.spring.strategy.register;

import org.test4j.fortest.service.UserService;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

/**
 * 验证多个表达符合接口类型时获取实现
 * 
 * @author darui.wudr
 * 
 */
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.impl.*DaoImpl"), /** <br> */
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
@Test(groups = "jtester")
public class SpringBeanRegisterTest_MultiPattern extends JTester implements IDatabase, ISpring {
	@SpringBeanByName
	private UserService userService;

	public void checkUserAnotherDao_NotFound() {
		Object userDao = spring.getBean("userAnotherDao");
		want.object(userDao).notNull();
	}

	public void checkUserServiceBean() {
		want.object(userService).notNull();
		Object o = spring.getBean("userService");
		want.object(o).same(userService);
	}
}
