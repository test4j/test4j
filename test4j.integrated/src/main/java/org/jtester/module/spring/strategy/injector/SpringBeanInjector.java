package org.jtester.module.spring.strategy.injector;

import java.lang.annotation.Annotation;

import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.module.spring.strategy.JTesterBeanFactory;

/**
 * 查找spring策略接口，以及策略工厂类
 * 
 * @author darui.wudr
 * 
 */
public abstract class SpringBeanInjector {

	private final static SpringBeanInjector byName = new SpringBeanInjectorByName();

	private final static SpringBeanInjector byType = new SpringBeanInjectorByType();

	/**
	 * 往测试类实例中注入spring bean
	 * 
	 * @param testedObject
	 */
	public static void injectSpringBeans(Object beanFactory, Object testedObject) {
		if (beanFactory instanceof JTesterBeanFactory) {
			byName.injectBy((JTesterBeanFactory) beanFactory, testedObject, SpringBeanByName.class);
			byType.injectBy((JTesterBeanFactory) beanFactory, testedObject, SpringBeanByType.class);
		} else {
			throw new RuntimeException(String.format(
					"the type error, object[%s] isn't an instance of JTesterBeanFactory.", beanFactory == null ? null
							: beanFactory.getClass().getName()));
		}
	}

	/**
	 * 按Annotation注释注入spring bean到测试实例中
	 * 
	 * @param context
	 *            spring容器
	 * @param testedObject
	 *            测试类
	 * @param annotation
	 *            字段声明的Annotation
	 * @return
	 */
	public abstract void injectBy(JTesterBeanFactory beanFactory, Object testedObject,
			Class<? extends Annotation> annotation);
}
