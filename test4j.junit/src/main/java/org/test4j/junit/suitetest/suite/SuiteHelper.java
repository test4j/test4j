package org.test4j.junit.suitetest.suite;

import java.util.Set;

import org.junit.runners.model.RunnerBuilder;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.annotations.RunGroup;
import org.test4j.tools.reflector.FieldAccessor;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SuiteHelper {
    /**
     * 在suite类上查找 @ClazFinder 注解
     * 
     * @param suiteClazz
     * @param parents
     * @return
     */
    public static ClazFinder findClazFinder(Class suiteClazz, RunnerBuilder builder) {
        ClazFinder annotation = (ClazFinder) findAnnotation(suiteClazz, builder, RunGroup.class);
        return annotation;
    }

    /**
     * 在suite类上查找 @RunGroup 注解
     * 
     * @param suiteClazz
     * @param builder
     * @return
     */
    public static RunGroup findRunGroup(Class suiteClazz, RunnerBuilder builder) {
        RunGroup annotation = (RunGroup) findAnnotation(suiteClazz, builder, RunGroup.class);
        return annotation;
    }

    private static Object findAnnotation(Class suiteClazz, RunnerBuilder builder, Class annotationClaz) {
        Object annotation = suiteClazz.getAnnotation(annotationClaz);
        if (annotation != null) {
            return annotation;
        }
        Set<Class> parents = (Set<Class>) FieldAccessor.getFieldValue(builder, "parents");
        if (parents == null || parents.size() == 0) {
            return null;
        }
        for (Class parent : parents) {
            annotation = parent.getAnnotation(annotationClaz);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}
