package org.jtester.testng.spring.strategy.register;

import mockit.NonStrict;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

/**
 * @Scene :使用@AutoBeanInject 来自动注入spring bean，但是无法查找到属性的实现类<br>
 *        忽略错误，改属性的bean不注入到spring容器
 * @author darui.wudr
 * 
 */
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl") })
@Test(groups = "jtester")
public class SpringBeanRegisterTest_NotFoundImplementClazz extends JTester {
	@SpringBeanByName
	UserService userService;

	@SpringBeanFrom
	@NonStrict
	UserAnotherDao userAnotherDao;

	@Test(description = "找不到属性的实现类时_不注入该springbean")
	public void getSpringBean_AutoBeanInjectNotFindImplement() {
		want.object(userService).notNull();

		UserDao userDao = reflector.getField(userService, "userDao");
		want.object(userDao).isNull();

		UserAnotherDao userAnotherDao = reflector.getField(userService, "userAnotherDao");
		want.object(userAnotherDao).notNull();
	}
}
