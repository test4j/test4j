package org.jtester.module.core.utility;

import static ext.jtester.apache.commons.lang.ClassUtils.getShortClassName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jtester.module.JTesterException;
import org.jtester.module.core.Module;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.ConfigHelper;

@SuppressWarnings("rawtypes")
public class ModuleHelper {

    /**
     * The default name of the default enum value.
     */
    public static final String DEFAULT_ENUM_VALUE_NAME = "DEFAULT";

    /**
     * Returns an object that represents the default values for the properties
     * of the given annotations and the given module.
     * 
     * @param moduleClass The class of the module for which we want the default
     *            annotation property values
     * @param configuration The jtester configuration
     * @param annotationClasses The annotations for which we want the default
     *            values
     * @return An object that returns the annotation property default values
     */
    public static Map<Class<? extends Annotation>, Map<String, String>> getAnnotationPropertyDefaults(Class<? extends Module> moduleClass,
                                                                                                      Class<? extends Annotation>... annotationClasses) {
        Map<Class<? extends Annotation>, Map<String, String>> result = new HashMap<Class<? extends Annotation>, Map<String, String>>();

        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            Method[] methods = annotationClass.getDeclaredMethods();
            for (Method method : methods) {
                String defaultValue = getAnnotationPropertyDefault(moduleClass, annotationClass, method.getName());

                Map<String, String> defaultValueMap = result.get(annotationClass);
                if (defaultValueMap == null) {
                    defaultValueMap = new HashMap<String, String>();
                    result.put(annotationClass, defaultValueMap);
                }
                defaultValueMap.put(method.getName(), defaultValue);
            }
        }
        return result;
    }

    /**
     * Returns the default for annotation property of the given annotation with
     * the given name for the given moduleclass.
     * 
     * @param moduleClass The module class, not null
     * @param annotationClass The annotation class, not null
     * @param name The property suffix, not null
     * @param configuration The jtester config, not null
     * @return the default value
     */
    private static String getAnnotationPropertyDefault(Class<? extends Module> moduleClass,
                                                       Class<? extends Annotation> annotationClass, String name) {
        String propertyName = getShortClassName(moduleClass) + "." + getShortClassName(annotationClass) + "." + name
                + ".default";
        if (!ConfigHelper.hasProperty(propertyName)) {
            return null;
        }
        return ConfigHelper.getString(propertyName);
    }

    /**
     * Replaces default enum value with the given default enum value. If
     * enumValue contains the value {@link #DEFAULT_ENUM_VALUE_NAME} the
     * defaultValue will be returned otherwise the enumValue itself will be
     * returned.
     * 
     * @param annotation the annotation, not null
     * @param annotationPropertyName the name of the annotation property
     * @param enumValue the value to check, not null
     * @param allDefaultValues the map with values to return in case of a
     *            default, not null
     * @return the enumValue or the defaultValue in case of a default
     */
    public static <T extends Enum<?>> T getEnumValueReplaceDefault(Class<? extends Annotation> annotation,
                                                                   String annotationPropertyName,
                                                                   T enumValue,
                                                                   Map<Class<? extends Annotation>, Map<String, String>> allDefaultValues) {
        return getEnumValueReplaceDefault(annotation, annotationPropertyName, enumValue, allDefaultValues,
                DEFAULT_ENUM_VALUE_NAME);
    }

    /**
     * Replaces default enum value with the given default enum value. If
     * enumValue contains the value {@link #DEFAULT_ENUM_VALUE_NAME} the
     * defaultValue will be returned otherwise the enumValue itself will be
     * returned.
     * 
     * @param annotation the annotation, not null
     * @param annotationPropertyName the name of the annotation property
     * @param enumValue the value to check, not null
     * @param allDefaultValues the map with values to return in case of a
     *            default, not null
     * @param defaultValueName the name of the default value
     * @return the enumValue or the defaultValue in case of a default
     */
    @SuppressWarnings({ "unchecked" })
    private static <T extends Enum<?>> T getEnumValueReplaceDefault(Class<? extends Annotation> annotation,
                                                                    String annotationPropertyName,
                                                                    T enumValue,
                                                                    Map<Class<? extends Annotation>, Map<String, String>> allDefaultValues,
                                                                    String defaultValueName) {
        String valueAsString = getValueAsStringReplaceDefault(annotation, annotationPropertyName, enumValue.name(),
                allDefaultValues, defaultValueName);
        return (T) ClazzHelper.getEnumValue(enumValue.getClass(), valueAsString);
    }

    /**
     * Replaces default enum value with the given default enum value. If
     * enumValue contains the value {@link #DEFAULT_ENUM_VALUE_NAME} the
     * defaultValue will be returned otherwise the enumValue itself will be
     * returned.
     * 
     * @param annotation the annotation, not null
     * @param annotationPropertyName the name of the annotation property
     * @param value the value to check, not null
     * @param allDefaultValues the map with values to return in case of a
     *            default, not null
     * @param defaultValueClass the name of the default value
     * @return the enumValue or the defaultValue in case of a default
     */
    public static Class getClassValueReplaceDefault(Class<? extends Annotation> annotation,
                                                    String annotationPropertyName,
                                                    Class value,
                                                    Map<Class<? extends Annotation>, Map<String, String>> allDefaultValues,
                                                    Class defaultValueClass) {

        String valueAsString = getValueAsStringReplaceDefault(annotation, annotationPropertyName, value.getName(),
                allDefaultValues, defaultValueClass.getName());
        return ClazzHelper.getClazz(valueAsString);
    }

    /**
     * Replaces default enum values with the given default enum value. If
     * enumValue contains the value defaultValueName, the defaultValue will be
     * returned otherwise the enumValue itself will be returned.
     * 
     * @param annotation the annotation, not null
     * @param annotationProperty the annotation property for which the value
     *            must be replaced, not null
     * @param valueAsString the value to check, not null
     * @param allDefaultValues the map with values to return in case of a
     *            default, not null
     * @param defaultValueName the name of the default value, eg DEFAULT, not
     *            null @return the enumValue or the defaultValue in case of a
     *            default
     * @return The default value as a string
     */
    private static String getValueAsStringReplaceDefault(Class<? extends Annotation> annotation,
                                                         String annotationProperty,
                                                         String valueAsString,
                                                         Map<Class<? extends Annotation>, Map<String, String>> allDefaultValues,
                                                         String defaultValueName) {
        if (!defaultValueName.equalsIgnoreCase(valueAsString)) {
            // no replace needed
            return valueAsString;
        }

        Map<String, String> defaultValues = allDefaultValues.get(annotation);
        if (defaultValues != null) {
            String defaultValueAsString = defaultValues.get(annotationProperty);
            if (defaultValueAsString != null) {
                return defaultValueAsString;
            }
        }
        // nothing found, raise exception
        throw new JTesterException("Could not replace default value. No default value found for annotation: "
                + annotation + ", property " + annotationProperty + ", defaultValues: " + allDefaultValues);
    }
}
