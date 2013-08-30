package org.test4j.testng.spring;

import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext
@AutoBeanInject
@Test(groups = "for-test")
public class SpringBeanRegisterTest_Constructor extends Test4J {

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
