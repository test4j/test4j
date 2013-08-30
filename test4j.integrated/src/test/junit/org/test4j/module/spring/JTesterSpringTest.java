package org.test4j.module.spring;

import mockit.Mocked;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.SpringTestedContext;
import org.test4j.module.spring.annotations.SpringBeanByName;

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

	@Test
	public void getSpringBean() {
		Object bean = spring.getBean("userService");
		want.object(bean).same(userService);
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void getSpringBean_NoSuchBeanDefinitionException() {
		Object unExists = spring.getBean("unExists");
		want.object(unExists).isNull();
	}
}
