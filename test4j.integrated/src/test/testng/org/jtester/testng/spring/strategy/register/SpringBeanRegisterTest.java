package org.jtester.testng.spring.strategy.register;

import org.jtester.fortest.service.UserAnotherDaoImpl;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.Property;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "spring" })
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class SpringBeanRegisterTest extends JTester implements IDatabase, ISpring {
	@SpringBeanByName(properties = {
			@Property(name = "userAnotherDao", ref = "userRefDao", clazz = UserAnotherDaoImpl.class),
			@Property(name = "myName", value = "I am test") })
	private UserService userService;

	@Test
	public void testRegister() {
		Object bean = spring.getBean("userRefDao");
		want.object(bean).notNull();

		Object ref = reflector.getField(userService, "userAnotherDao");
		want.object(ref).notNull().same(bean);
	}

	public void testRegister2() {
		String value = reflector.getField(userService, "myName");
		want.string(value).isEqualTo("I am test");
	}
}
