package org.jtester.module.spring.strategy.register;

import java.util.LinkedList;
import java.util.Queue;

import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.exception.FindBeanImplClassException;
import org.jtester.module.spring.strategy.ImplementorFinder;
import org.jtester.module.spring.strategy.register.types.PropertiesRegister;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.StringHelper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 动态配置spring context的bean @AutoBeanInject<br>
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class RegisterDynamicBean {

	Queue<Class> beanFields = new LinkedList<Class>();

	/**
	 * 动态注册@SpringBeanByName,@SpringBeanByType和@SpringBeanFor等注解定义的bean
	 * 
	 * @param beanFactory
	 * @param testedClazz
	 */
	public static void dynamicRegisterBeanDefinition(final DefaultListableBeanFactory beanFactory,
			final Class testedClazz) {
		RegisterDynamicBean dynamicBean = new RegisterDynamicBean(beanFactory, testedClazz);
		try {
			dynamicBean.registerSpringBean();
		} catch (FindBeanImplClassException e) {
			throw new RuntimeException(e);
		}
	}

	private final DefaultListableBeanFactory beanFactory;
	private final Class testedClazz;
	private final RegisterBeanDefinition definitionRegister;

	RegisterDynamicBean(final DefaultListableBeanFactory beanFactory, final Class testedClazz) {
		if (testedClazz == null) {
			throw new RuntimeException("Current thread hasn't registered tested class!");
		}
		this.beanFactory = beanFactory;
		this.testedClazz = testedClazz;
		AutoBeanInject autoBeanInject = AnnotationHelper.getClassLevelAnnotation(AutoBeanInject.class, testedClazz);
		this.definitionRegister = new RegisterBeanDefinition(this.beanFactory, autoBeanInject);
	}

	static final SpringBeanRegister byNameRegister = new SpringBeanByNameRegister();

	static final SpringBeanRegister byTypeRegister = new SpringBeanByTypeRegister();

	/**
	 * 注册@SpringBeanByName、@SpringBean和@SpringBeanFor 定义的spring bean对象
	 * 
	 * @param beanFactory
	 * @param testedClazz
	 * @throws FindBeanImplClassException
	 */
	protected final void registerSpringBean() throws FindBeanImplClassException {
		// o1 注册@SpringBeanByName注释的spring bean
		byNameRegister.register(testedClazz, beanFields, definitionRegister);
		// o2 注册@SpringBeanByType注释的spring bean
		byTypeRegister.register(testedClazz, beanFields, definitionRegister);

		if (this.definitionRegister.allowAutoInject() == false) {
			return;
		}
		/**
		 * 属性的注入放到所有的@SpringBean和@SpringBeanByName注入完毕后开始
		 */
		Class clazz = beanFields.poll();
		while (clazz != null) {
			if (clazz != null) {
				PropertiesRegister.registerPropertiesBean(clazz, definitionRegister, this.beanFields);
			}
			clazz = beanFields.poll();
		}
	}

	/**
	 * 返回普通方式定义的BeanDefinition
	 * 
	 * @param beanName
	 * @param implClazz
	 * @param initMethod
	 * @param isLazy
	 * @return
	 */
	public static RootBeanDefinition getRootBeanDefinition(String beanName, Class implClazz, String initMethod,
			boolean isLazy) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClassName(implClazz.getName());
		beanDefinition.setScope("singleton");
		beanDefinition.setAutowireCandidate(true);
		beanDefinition.setLazyInit(isLazy);

		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

		String init_method = initMethod;
		if (StringHelper.isBlankOrNull(initMethod)) {
			init_method = ImplementorFinder.findInitMethodName(implClazz);
		}
		if (StringHelper.isBlankOrNull(init_method) == false) {
			beanDefinition.setInitMethodName(init_method);
		}

		return beanDefinition;
	}
}
