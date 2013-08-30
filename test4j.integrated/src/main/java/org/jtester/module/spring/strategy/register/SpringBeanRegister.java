package org.jtester.module.spring.strategy.register;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.Set;

import org.jtester.module.core.utility.ModulesManager;
import org.jtester.module.jmockit.JMockitModule;
import org.jtester.module.jmockit.utility.JMockitModuleHelper;
import org.jtester.module.spring.annotations.Property;
import org.jtester.module.spring.exception.FindBeanImplClassException;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.StringHelper;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;

@SuppressWarnings("rawtypes")
public abstract class SpringBeanRegister {

	public void register(final Class testedClazz, final Queue<Class> queue, final RegisterBeanDefinition definitions)
			throws FindBeanImplClassException {
		// o1 注册@SpringBeanByName注释的spring bean
		Set<Field> fields = this.getRegisterField(testedClazz);
		for (Field field : fields) {
			if (ModulesManager.isModuleEnabled(JMockitModule.class)) {
				JMockitModuleHelper.doesSpringBeanFieldIllegal(field);
			}

			BeanMeta beanMeta = new BeanMeta(queue, definitions);
			this.initSpringBean(field, beanMeta);
			if (beanMeta.isConcreteClazz()) {
				beanMeta.registerBeanDefinition();
			} else if (definitions.allowAutoInject(beanMeta.beanName)) {
				Class impl = definitions.findImplementClass(testedClazz, beanMeta.beanName, field.getType());
				if (impl != null) {
					beanMeta.beanClaz = impl;
					beanMeta.registerBeanDefinition();
				}
			}
		}
	}

	/**
	 * 返回所有@SpringBeanByName @SpringBeanByType 定义的字段<br>
	 * 
	 * @param testedClazz
	 * @return
	 */
	protected abstract Set<Field> getRegisterField(Class testedClazz);

	/**
	 * 返回字段Meta定义
	 * 
	 * @param field
	 * @return
	 */
	protected abstract void initSpringBean(final Field field, final BeanMeta beanMeta);

}

@SuppressWarnings("rawtypes")
class BeanMeta {
	String initMethod;

	String beanName;

	Class beanClaz;

	Property[] properties;

	private final Queue<Class> queue;

	private final RegisterBeanDefinition definitions;

	public BeanMeta(final Queue<Class> queue, final RegisterBeanDefinition definitions) {
		this.queue = queue;
		this.definitions = definitions;
	}

	/**
	 * 是否是可实例化的类
	 * 
	 * @return
	 */
	boolean isConcreteClazz() {
		if (beanClaz.isAnnotation()) {
			return false;
		}
		if (beanClaz.isEnum()) {
			return false;
		}

		if (ClazzHelper.isInterfaceOrAbstract(beanClaz)) {
			return false;
		} else {
			return true;
		}
	}

	void registerBeanDefinition() {
		RootBeanDefinition beanDefinition = RegisterDynamicBean.getRootBeanDefinition(beanName, beanClaz, initMethod,
				false);

		for (Property property : properties) {
			this.checkPropertyDefine(property);
			if (StringHelper.isBlankOrNull(property.value())) {
				this.registerRefBean(property.ref(), property.clazz());
				RuntimeBeanReference reference = new RuntimeBeanReference(property.ref());
				PropertyValue pv = new PropertyValue(property.name(), reference);
				beanDefinition.getPropertyValues().addPropertyValue(pv);
			} else {
				PropertyValue pv = new PropertyValue(property.name(), property.value());
				beanDefinition.getPropertyValues().addPropertyValue(pv);
			}
		}

		definitions.register(beanName, beanDefinition);
		queue.offer(beanClaz);
	}

	/**
	 * 添加reference bean的spring定义
	 * 
	 * @param refname
	 * @param clazz
	 */
	private void registerRefBean(String refname, Class clazz) {
		if (clazz == Property.class) {
			return;
		}
		if (ClazzHelper.isInterfaceOrAbstract(clazz)) {
			throw new RuntimeException("the reference bean[" + refname
					+ "] class should be an instancable class, but actual is " + clazz.getName() + ".");
		}
		RootBeanDefinition beanDefinition = RegisterDynamicBean.getRootBeanDefinition(refname, clazz, null, true);
		definitions.register(refname, beanDefinition);
		queue.add(clazz);
	}

	/**
	 * 检查BeanProperty定义的合法性
	 * 
	 * @param property
	 */
	private void checkPropertyDefine(final Property property) {
		if (StringHelper.isBlankOrNull(property.name())) {
			throw new RuntimeException("the value of @BeanProperty name() can't be empty.");
		}
		if (StringHelper.isBlankOrNull(property.value()) && StringHelper.isBlankOrNull(property.ref())) {
			throw new RuntimeException("the values of @BeanProperty value() and ref() can't be all empty.");
		}
	}
}
