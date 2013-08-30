package org.jtester.module.spring.strategy.register.types;

import java.lang.reflect.Method;
import java.util.Queue;

import org.jtester.module.spring.strategy.register.RegisterBeanDefinition;
import org.jtester.tools.commons.ClazzHelper;

@SuppressWarnings("rawtypes")
public class MethodPropertiesRegister extends PropertiesRegister {

	protected MethodPropertiesRegister(Class ownerClazz, RegisterBeanDefinition definitionRegister) {
		super(ownerClazz, definitionRegister);
	}

	public void registerProperties(final Queue<Class> registedBeanClazz) {
		Method[] allmethods = ownerClazz.getMethods();
		for (Method method : allmethods) {
			if (method.getParameterTypes().length != 1) {
				continue;
			}
			Class propClazz = method.getParameterTypes()[0];
			String beanName = ClazzHelper.exactBeanName(method);
			boolean isExclude = this.definitionRegister.isExcludeProperty(beanName, propClazz);

			if (isExclude == false) {
				this.registerBean(beanName, propClazz, registedBeanClazz);
			}
		}
	}

	// private void registerBean(final String beanName, final Class propClazz,
	// final Queue<Class> registedBeanClazz) {
	// try {
	// boolean doesRegisted = definitionRegister.doesHaveRegisted(beanName);
	// if (doesRegisted) {
	// return;
	// }
	// Class impl = definitionRegister.findImplementClass(ownerClazz, beanName,
	// propClazz);
	// if (impl == null) {
	// return;
	// }
	// RootBeanDefinition beanDefinition =
	// SpringBeanRegister.getRootBeanDefinition(beanName, impl, true);
	// definitionRegister.register(beanName, beanDefinition);
	//
	// registedBeanClazz.offer(impl);
	// } catch (FindBeanImplClassException e) {
	// definitionRegister.ignoreNotFoundException(e);
	// }
	// }
}
