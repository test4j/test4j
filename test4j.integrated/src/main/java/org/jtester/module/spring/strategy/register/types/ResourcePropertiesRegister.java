package org.jtester.module.spring.strategy.register.types;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Resource;

import org.jtester.module.spring.strategy.register.RegisterBeanDefinition;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ResourcePropertiesRegister extends PropertiesRegister {

	protected ResourcePropertiesRegister(Class ownerClazz, RegisterBeanDefinition definitionRegister) {
		super(ownerClazz, definitionRegister);
	}

	@Override
	public void registerProperties(Queue<Class> registedBeanClazz) {
		Set<Field> fields = AnnotationHelper.getFieldsAnnotatedWith(ownerClazz, Resource.class);
		for (Field field : fields) {
			Resource resource = field.getAnnotation(Resource.class);
			String beanName = resource.name();
			if (StringHelper.isBlankOrNull(beanName)) {
				beanName = field.getName();
			}
			Class propClazz = field.getType();
			boolean isExclude = this.definitionRegister.isExcludeProperty(beanName, propClazz);
			if (isExclude == false) {
				this.registerBean(beanName, propClazz, registedBeanClazz);
			}
		}
	}
}
