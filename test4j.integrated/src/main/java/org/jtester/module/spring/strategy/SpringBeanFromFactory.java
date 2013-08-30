package org.jtester.module.spring.strategy;

import static org.jtester.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import org.jtester.module.core.TestContext;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.exception.FindBeanImplClassException;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.reflector.imposteriser.JTesterProxy;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SpringBeanFromFactory implements FactoryBean {// , BeanFactoryAware

	public String fieldName;

	private Class type = null;

	public SpringBeanFromFactory() {
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getObject() throws Exception {
		Class testClazz = TestContext.currTestedClazz();
		Object o = JTesterProxy.proxy(testClazz, fieldName);
		return o;
	}

	public Class getObjectType() {
		// JTesterBeanFactory beanFactory =
		// TestedContext.getSpringBeanFactory();
		if (type == null) {// && beanFactory != null
			Class testClazz = TestContext.currTestedClazz();

			Field field = FieldHelper.getField(testClazz, this.fieldName);
			this.type = field.getType();
		}
		return type;
	}

	public boolean isSingleton() {
		return true;
	}

	/**
	 * 注册tested object下所有的@SpringBeanFrom对象<br>
	 * 
	 * @param testedObject
	 */
	public static void registerSpringBeanFromField(final DefaultListableBeanFactory beanFactory, Class testedClazz) {
		Set<Field> beanFields = getFieldsAnnotatedWith(testedClazz, SpringBeanFrom.class);

		for (Field beanField : beanFields) {
			SpringBeanFrom beanFor = beanField.getAnnotation(SpringBeanFrom.class);
			String fieldName = beanField.getName();
			String beanName = beanFor.value();
			if (StringHelper.isBlankOrNull(beanName)) {
				beanName = fieldName;
			}
			registerProxyBeanDefinition(beanFactory, beanName, fieldName);
		}
	}

	/**
	 * 定义SpringBeanFor的代理proxy bean
	 * 
	 * @param beanFactory
	 * @param beanName
	 * @param byName
	 * @throws FindBeanImplClassException
	 */
	private static void registerProxyBeanDefinition(final DefaultListableBeanFactory beanFactory,
			final String beanName, final String fieldName) {
		// SpringBeanFrom采用的是覆盖原有的bean定义
		if (beanFactory.containsBeanDefinition(beanName)) {
			MessageHelper
					.info(String
							.format("SpringBeanFrom BeanName[%s] has been defined in application context, so override bean definition.",
									beanName));
		}

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClassName(SpringBeanFromFactory.class.getName());
		beanDefinition.setScope("singleton");
		beanDefinition.setAutowireCandidate(true);
		beanDefinition.setLazyInit(true);

		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

		MutablePropertyValues properties = new MutablePropertyValues(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				this.put("fieldName", fieldName);
			}
		});
		beanDefinition.setPropertyValues(properties);
		beanFactory.registerBeanDefinition(beanName, beanDefinition);
	}
}
