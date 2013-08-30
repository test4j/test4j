package org.jtester.module.spring.strategy.register;

import java.lang.reflect.Field;
import java.util.Set;

import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
class SpringBeanByNameRegister extends SpringBeanRegister {

	@Override
	protected Set<Field> getRegisterField(Class testedClazz) {
		Set<Field> fields = AnnotationHelper.getFieldsAnnotatedWith(testedClazz, SpringBeanByName.class);
		return fields;
	}

	@Override
	protected void initSpringBean(final Field field, final BeanMeta beanMeta) {
		SpringBeanByName byName = field.getAnnotation(SpringBeanByName.class);

		beanMeta.beanName = field.getName();
		if (StringHelper.isBlankOrNull(byName.value()) == false) {
			beanMeta.beanName = byName.value();
		}
		beanMeta.initMethod = byName.init();
		beanMeta.beanClaz = byName.claz();
		beanMeta.properties = byName.properties();
	}
}
