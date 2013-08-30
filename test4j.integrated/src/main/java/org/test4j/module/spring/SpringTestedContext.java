package org.test4j.module.spring;

import java.util.HashMap;
import java.util.Map;

import org.test4j.module.core.TestContext;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.spring.strategy.Test4JBeanFactory;
import org.test4j.module.spring.strategy.Test4JSpringContext;
import org.test4j.module.spring.strategy.cleaner.SpringBeanCleaner;
import org.test4j.module.spring.utility.SpringModuleHelper;

/**
 * 测试spring管理上下文
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public class SpringTestedContext {
	private final static SpringTestedContext context = new SpringTestedContext();

	public final static SpringTestedContext context() {
		return context;
	}

	private SpringTestedContext() {
	}

	/**
	 * AbstractApplicationContext类<br>
	 * <br>
	 * <br>
	 * 第一个Object是测试类实例<br>
	 * 第二个Object是AbstractApplicationContext实例，这里定义为Object类型是为了兼容没有使用spring容器的测试
	 */
	private static Map<Class, Test4JSpringContext> springBeanFactories = new HashMap<Class, Test4JSpringContext>();

	/**
	 * 存放当前测试实例下的spring application context实例
	 * 
	 * @param springContext
	 */
	public static void setSpringContext(Test4JSpringContext springContext) {
		if (springContext == null) {
			MessageHelper.info("no spring application context for test:" + TestContext.currTestedClazzName());
			return;
		}
		if (TestContext.currTestedClazz() == null) {
			throw new RuntimeException("the tested object can't be null.");
		} else {
			springBeanFactories.put(TestContext.currTestedClazz(), springContext);
		}
	}

	/**
	 * 获取当前测试实例的spring application context实例
	 * 
	 * @return
	 */
	public static Test4JBeanFactory getSpringBeanFactory() {
		if (TestContext.currTestedClazz() == null) {
			throw new RuntimeException("the tested object can't be null.");
		} else {
			Test4JSpringContext springContext = springBeanFactories.get(TestContext.currTestedClazz());
			return springContext == null ? null : springContext.getTest4JBeanFactory();
		}
	}

	public static Test4JSpringContext getSpringContext() {
		if (TestContext.currTestedClazz() == null) {
			throw new RuntimeException("the tested object can't be null.");
		} else {
			Test4JSpringContext springContext = springBeanFactories.get(TestContext.currTestedClazz());
			return springContext;
		}
	}

	/**
	 * 释放spring context，清空测试类中的spring bean实例
	 */
	public static void removeSpringContext() {
		if (TestContext.currTestedClazz() == null) {
			throw new RuntimeException("the tested object can't be null.");
		} else {
			SpringBeanCleaner.cleanSpringBeans(TestContext.currTestedObject());
			Object springContext = springBeanFactories.remove(TestContext.currTestedClazz());
			if (springContext != null) {
				SpringModuleHelper.closeSpringContext(springContext);
			}
		}
	}
}
