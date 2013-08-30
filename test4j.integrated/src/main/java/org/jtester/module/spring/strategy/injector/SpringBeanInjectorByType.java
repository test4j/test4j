package org.jtester.module.spring.strategy.injector;

import static org.jtester.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.jtester.module.JTesterException;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.module.spring.strategy.JTesterBeanFactory;
import org.jtester.tools.commons.FieldHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
class SpringBeanInjectorByType extends SpringBeanInjector {
	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * 根据@SpringBeanByType注入spring bean<br>
	 */
	public void injectBy(JTesterBeanFactory beanFactory, Object testedObject, Class<? extends Annotation> annotation) {
		Class testedClazz = testedObject.getClass();
		Set<Field> fields = getFieldsAnnotatedWith(testedClazz, SpringBeanByType.class);
		for (Field field : fields) {
			try {
				Object bean = getSpringBeanByType(beanFactory, field.getType());
				FieldHelper.setFieldValue(testedObject, field, bean);
			} catch (Throwable e) {
				String error = String.format("inject @SpringBeanByType field[%s] in class[%s] error.", field.getName(),
						testedClazz.getName());
				throw new JTesterException(error, e);
			}
		}
	}

	/**
	 * Gets the spring bean with the given type. The given test instance, by
	 * using {@link SpringContext}, determines the application
	 * context in which to look for the bean. If more there is not exactly 1
	 * possible bean assignment, an JTesterException will be thrown.
	 * 
	 * @param testObject
	 *            The test instance, not null
	 * @param type
	 *            The type, not null
	 * @return The bean, not null
	 */
	private Object getSpringBeanByType(JTesterBeanFactory beanFactory, Class type) {
		Map<String, ?> beans = beanFactory.getBeansOfType(type);
		if (beans == null || beans.size() == 0) {
			throw new JTesterException("Unable to get Spring bean by type. No Spring bean found for type "
					+ type.getSimpleName());
		}
		if (beans.size() > 1) {
			throw new JTesterException(
					"Unable to get Spring bean by type. More than one possible Spring bean for type "
							+ type.getSimpleName() + ". Possible beans; " + beans);
		}
		return beans.values().iterator().next();
	}
}
