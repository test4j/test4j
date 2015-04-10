package org.test4j.junit.suitetest.suite;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.junit.runners.model.RunnerBuilder;
import org.test4j.junit.annotations.TestPath;
import org.test4j.junit.annotations.RunGroup;
import org.test4j.module.core.utility.MessageHelper;
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
    public static TestPath findClazFinder(Class suiteClazz, RunnerBuilder builder) {
        TestPath annotation = findAnnotation(suiteClazz, builder, TestPath.class);
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
        RunGroup annotation = findAnnotation(suiteClazz, builder, RunGroup.class);
        return annotation;
    }

    private static <T extends Annotation> T findAnnotation(Class suiteClazz, RunnerBuilder builder,
                                                           Class<T> annotationClaz) {
        try {
            T annotation = (T) suiteClazz.getAnnotation(annotationClaz);
            if (annotation != null) {
                return annotation;
            }
            Set<Class> parents = (Set<Class>) FieldAccessor.getFieldValue(builder, "parents");
            if (parents == null || parents.size() == 0) {
                return null;
            }
            for (Class parent : parents) {
                annotation = (T) parent.getAnnotation(annotationClaz);
                if (annotation != null) {
                    return annotation;
                }
            }
            return null;
        } catch (RuntimeException e) {
            MessageHelper.error("find annotation[" + annotationClaz.getName() + "] from suite error:" + e.getMessage(),
                    e);
            throw e;
        }
    }
}
