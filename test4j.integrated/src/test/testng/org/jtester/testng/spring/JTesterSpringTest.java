package org.jtester.testng.spring;

import mockit.Mocked;

import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.SpringTestedContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.testng.annotations.Test;

@Test(groups = { "jtester" })
public class JTesterSpringTest extends MockedBeanByNameTest_Base implements IDatabase, ISpring {
	@SpringBeanByName
	protected UserService userService;

	@Mocked
	protected UserDao userDao;

	@Test
	public void testGetBeanFactory() {
		BeanFactory factory = (BeanFactory) SpringTestedContext.getSpringBeanFactory();
		want.object(factory).notNull();
		UserDao daoBean = (UserDao) factory.getBean("userDao");
		want.object(daoBean).notNull();
	}

	public void getSpringBean() {
		Object bean = spring.getBean("userService");
		want.object(bean).same(userService);
	}

	@Test(expectedExceptions = NoSuchBeanDefinitionException.class)
	public void getSpringBean_NoSuchBeanDefinitionException() {
		Object unExists = spring.getBean("unExists");
		want.object(unExists).isNull();
	}
}
