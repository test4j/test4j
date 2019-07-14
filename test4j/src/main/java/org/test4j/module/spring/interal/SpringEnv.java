package org.test4j.module.spring.interal;

import org.test4j.module.core.internal.Test4JTestContext;
import org.test4j.tools.commons.AnnotationHelper;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringEnv {
    private static Map<Class, Boolean> classIsSpring = new ConcurrentHashMap<>();

    private static String[] springTestAnnotations = {
            "org.springframework.boot.test.context.SpringBootTest",
            "org.springframework.test.context.ContextConfiguration"
    };

    public static boolean isSpringEnv() {
        Class klass = Test4JTestContext.currTestedClazz();
        return isSpringEnv(klass);
    }

    public static void setSpringEnv(Class<?> klass) {
        classIsSpring.put(klass, isSpringTest(klass));
    }

    public static boolean isSpringEnv(Class klass) {
        return classIsSpring.get(klass) == null ? false : classIsSpring.get(klass);
    }

    private static boolean isSpringTest(Class klass) {
        for (String annotation : springTestAnnotations) {
            boolean hasAnnotation = hasAnnotation(klass, annotation);
            if (hasAnnotation) {
                return true;
            }
        }
        return false;
    }

    static Map<String, Class> HasAnnotation = new HashMap<>(5);

    private static boolean hasAnnotation(Class objectClass, String annotation) {
        Class annotationClass = getAnnotationClass(annotation);
        if (annotationClass == null) {
            return false;
        } else {
            Annotation instance = AnnotationHelper.getClassLevelAnnotation(annotationClass, objectClass);
            return instance != null;
        }
    }

    private static Class<?> getAnnotationClass(String annotation) {
        if (!HasAnnotation.containsKey(annotation)) {
            try {
                Class klass = Class.forName(annotation);
                HasAnnotation.put(annotation, klass);
                return klass;
            } catch (ClassNotFoundException e) {
                HasAnnotation.put(annotation, null);
                return null;
            }
        }
        return HasAnnotation.get(annotation);
    }
}
