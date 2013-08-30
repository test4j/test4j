package org.jtester.module.spring.strategy.injector;

import static org.jtester.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import org.jtester.module.JTesterException;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.strategy.JTesterBeanFactory;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
class SpringBeanInjectorByName extends SpringBeanInjector {
	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * 根据@SpringBeanByName注入spring bean<br>
	 * <br>
	 * Gets the spring bean with the given name. The given test instance, by
	 * using {@link SpringContext}, determines the application
	 * context in which to look for the bean.
	 * <p/>
	 * A JTesterException is thrown when the no bean could be found for the
	 * given name.
	 */
	public void injectBy(JTesterBeanFactory beanFactory, Object testedObject, Class<? extends Annotation> annotation) {
		Class testedClazz = testedObject.getClass();
		Set<Field> fields = getFieldsAnnotatedWith(testedClazz, SpringBeanByName.class);
		for (Field field : fields) {
			try {
				SpringBeanByName byName = field.getAnnotation(SpringBeanByName.class);
				String beanName = byName.value();
				if (StringHelper.isBlankOrNull(byName.value())) {
					beanName = field.getName();
				}
				Object bean = beanFactory.getSpringBean(beanName);
				FieldHelper.setFieldValue(testedObject, field, bean);
			} catch (Throwable e) {
				String error = String.format("inject @SpringBeanByName field[%s] in class[%s] error.", field.getName(),
						testedClazz.getName());
				throw new JTesterException(error, e);
			}
		}
	}
}
