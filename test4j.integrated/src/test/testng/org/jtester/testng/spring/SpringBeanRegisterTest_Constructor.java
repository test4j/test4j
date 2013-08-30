package org.jtester.testng.spring;

import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext
@AutoBeanInject
@Test(groups = "for-test")
public class SpringBeanRegisterTest_Constructor extends JTester {

	@SpringBeanByName
	BeanClass bean;

	/**
	 * 自动注册的Clazz没有默认的构造函数
	 */
	public void test_AutoInject_NoDefaultConstructor() {

	}

	public static class BeanClass {
		public BeanClass(String value) {

		}
	}
}
