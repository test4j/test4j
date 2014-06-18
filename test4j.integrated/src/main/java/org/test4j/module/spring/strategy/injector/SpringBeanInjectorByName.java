package org.test4j.module.spring.strategy.injector;

import static org.test4j.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

import java.lang.reflect.Field;
import java.util.Set;

import org.test4j.module.Test4JException;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.strategy.Test4JBeanFactory;
import org.test4j.tools.commons.FieldHelper;
import org.test4j.tools.commons.StringHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
class SpringBeanInjectorByName extends SpringBeanInjector {
    /**
     * {@inheritDoc}<br>
     * <br>
     * 根据@SpringBeanByName注入spring bean<br>
     * <br>
     * Gets the spring bean with the given name. The given test instance, by
     * using {@link SpringContext}, determines the application context in which
     * to look for the bean.
     * <p/>
     * A Test4JException is thrown when the no bean could be found for the given
     * name.
     */
    @Override
    public void injectBy(Test4JBeanFactory beanFactory, Object testedObject) {
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
                throw new Test4JException(error, e);
            }
        }
    }
}
