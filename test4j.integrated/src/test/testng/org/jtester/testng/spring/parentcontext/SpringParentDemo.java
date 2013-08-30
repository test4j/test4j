package org.jtester.testng.spring.parentcontext;

import org.jtester.fortest.service.UserServiceImpl;
import org.jtester.testng.JTester;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

public class SpringParentDemo extends JTester {
	@SuppressWarnings("unused")
	@Test
	public void demoSpringParent() {
		ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext(new String[] {
				"org/jtester/module/spring/testedbeans/xml/data-source.xml",
				"org/jtester/module/spring/testedbeans/xml/beans.xml" });

		Object userService1 = parent.getBean("userService");
		want.object(userService1).notNull();

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "org/jtester/module/spring/testedbeans/xml/beans-child.xml" }, parent);

		Object userService2 = context.getBean("userService");
		UserServiceImpl impl = reflector.getSpringAdvisedTarget(userService2);
		want.object(userService2).notNull();
	}
}
