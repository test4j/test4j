package org.test4j.module.spring.strategy.injector;

import static org.test4j.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.test4j.module.Test4JException;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.strategy.Test4JBeanFactory;
import org.test4j.tools.commons.FieldHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
class SpringBeanInjectorByType extends SpringBeanInjector {
    /**
     * {@inheritDoc}<br>
     * <br>
     * 根据@SpringBeanByType注入spring bean<br>
     */
    @Override
    public void injectBy(Test4JBeanFactory beanFactory, Object testedObject, Class<? extends Annotation> annotation) {
        Class testedClazz = testedObject.getClass();
        Set<Field> fields = getFieldsAnnotatedWith(testedClazz, SpringBeanByType.class);
        for (Field field : fields) {
            try {
                Object bean = getSpringBeanByType(beanFactory, field.getType());
                FieldHelper.setFieldValue(testedObject, field, bean);
            } catch (Throwable e) {
                String error = String.format("inject @SpringBeanByType field[%s] in class[%s] error.", field.getName(),
                        testedClazz.getName());
                throw new Test4JException(error, e);
            }
        }
    }

    /**
     * Gets the spring bean with the given type. The given test instance, by
     * using {@link SpringContext}, determines the application context in which
     * to look for the bean. If more there is not exactly 1 possible bean
     * assignment, an Test4JException will be thrown.
     * 
     * @param testObject The test instance, not null
     * @param type The type, not null
     * @return The bean, not null
     */
    private Object getSpringBeanByType(Test4JBeanFactory beanFactory, Class type) {
        Map<String, ?> beans = beanFactory.getBeansOfType(type);
        if (beans == null || beans.size() == 0) {
            throw new Test4JException("Unable to get Spring bean by type. No Spring bean found for type "
                    + type.getSimpleName());
        }
        if (beans.size() > 1) {
            throw new Test4JException("Unable to get Spring bean by type. More than one possible Spring bean for type "
                    + type.getSimpleName() + ". Possible beans; " + beans);
        }
        return beans.values().iterator().next();
    }
}
