package org.test4j.module.inject.inject.imposteriser;

import org.test4j.module.inject.inject.Inject;
import org.test4j.module.inject.inject.Injected;
import org.test4j.tools.Logger;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.reflector.Reflector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.test4j.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

public class InjectHelper {

    /**
     * 初始化测试对象的Injected
     *
     * @param testedObject 被测试对象
     */
    public static void injectIntoTestedObject(Object testedObject) {
        Set<Field> injects = getFieldsAnnotatedWith(testedObject.getClass(), Inject.class);
        for (Field field : injects) {
            Object value = getProxyValue(testedObject, field);
            Inject inject = field.getAnnotation(Inject.class);
            List<Object> targets = targetField(testedObject, inject);
            String[] properties = properties(field, inject);
            for (Object target : targets) {
                injectFieldIntoTarget(target, value, properties);
            }
        }
    }

    /**
     * 先构造代理对象，如果无法构造，直接返回当前值
     *
     * @param testedObject
     * @param field
     * @return
     */
    private static Object getProxyValue(Object testedObject, Field field) {
        try {
            return Test4JProxy.proxy(testedObject.getClass(), field);
        } catch (Exception e) {
            return Reflector.getFieldValue(testedObject, field);
        }
    }

    private static void injectFieldIntoTarget(Object target, Object fieldValue, String[] properties) {
        for (String property : properties) {
            try {
                Reflector.setFieldValue(target, property, fieldValue);
            } catch (RuntimeException e) {
                Logger.warn(e.getMessage());
            }
        }
    }

    private static List<Object> targetField(Object testedObject, Inject inject) {
        List<Object> injected = new ArrayList<>();
        if (inject.targets().length == 0) {
            Set<Field> targets = AnnotationHelper.getFieldsAnnotatedWith(testedObject.getClass(), Injected.class);
            for (Field target : targets) {
                injected.add(Reflector.getFieldValue(testedObject, target));
            }
        } else {
            for (String target : inject.targets()) {
                injected.add(Reflector.getFieldValue(testedObject, target));
            }
        }
        return injected;
    }

    private static String[] properties(Field field, Inject inject) {
        if (inject.properties().length == 0) {
            return new String[]{field.getName()};
        } else {
            return inject.properties();
        }
    }
}