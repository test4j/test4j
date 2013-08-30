package org.jtester.datafilling.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.jtester.datafilling.annotations.FillExclude;
import org.jtester.module.core.utility.MessageHelper;

/**
 * This class wraps fields and setters information about a given class
 * <p>
 * The purpose of this class is to work as a sort of cache which stores the list
 * of declared fields and setter methods of a given class. These information
 * will then be analysed to compose the list of setters which can be invoked to
 * create the state of a given POJO.
 * </p>
 */
@SuppressWarnings({ "serial" })
public class ClassFieldInfo implements Serializable {

    /** The Set of fields belonging to this class */
    private final Set<String> classFields;

    /** The Set of setters belonging to this class */
    private final Set<Method> classSetters;

    /**
     * Full constructor
     * 
     * @param className The class name
     * @param classFields The set of fields belonging to this class
     * @param classSetters The set of setters belonging to this class
     */
    public ClassFieldInfo(Set<String> classFields, Set<Method> classSetters) {
        this.classFields = classFields;
        this.classSetters = classSetters;
    }

    /**
     * @return the classSetters
     */
    public Set<Method> getClassSetters() {
        return classSetters;
    }

    public Set<String> getClassFields() {
        return classFields;
    }

    /**
     * It returns a {@link ClassFieldInfo} object for the given class
     * 
     * @param clazz The class to retrieve info from
     * @return a {@link ClassFieldInfo} object for the given class
     */
    public static ClassFieldInfo getClassInfo(Class<?> clazz) {
        Set<String> classFields = getDeclaredInstanceFields(clazz);
        Set<Method> classSetters = getPojoSetters(clazz, classFields);
        return new ClassFieldInfo(classFields, classSetters);
    }

    /**
     * Given a class, it returns a Set of its declared instance field names.
     * 
     * @param clazz The class to analyse to retrieve declared fields
     * @return Set of a class declared field names.
     */
    public static Set<String> getDeclaredInstanceFields(Class<?> clazz) {
        Set<String> classFields = new HashSet<String>();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                // If users wanted to skip this field, we grant their wishes
                if (field.getAnnotation(FillExclude.class) != null) {
                    continue;
                }
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers)) {

                    classFields.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return classFields;
    }

    /**
     * Given a class and a set of class declared fields it returns a Set of
     * setters matching the declared fields
     * <p>
     * If present, a setter method is considered if and only if the
     * {@code classFields} argument contains an attribute whose name matches the
     * setter, according to JavaBean standards.
     * </p>
     * 
     * @param clazz The class to analyse for setters
     * @param classFields A Set of field names for which setters are to be found
     * @return A Set of setters matching the class declared field names
     */
    public static Set<Method> getPojoSetters(Class<?> clazz, Set<String> classFields) {
        Set<Method> classSetters = new HashSet<Method>();

        while (clazz != null) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            String candidateField = null;
            for (Method method : declaredMethods) {
                if (!method.getName().startsWith("set")) {
                    continue;
                }
                //if (!method.getReturnType().equals(void.class)) {
                if (method.getParameterTypes().length != 1) {
                    continue;
                }
                candidateField = extractFieldNameFromSetterMethod(method);
                if (!classFields.contains(candidateField)) {
                    continue;
                }
                classSetters.add(method);
            }
            clazz = clazz.getSuperclass();
        }
        return classSetters;
    }

    /**
     * Given a setter {@link Method}, it extracts the field name, according to
     * JavaBean standards
     * <p>
     * This method, given a setter method, it returns the corresponding
     * attribute name. For example: given setIntField the method would return
     * intField. The correctness of the return value depends on the adherence to
     * JavaBean standards.
     * </p>
     * 
     * @param method The setter method from which the field name is required
     * @return The field name corresponding to the setter
     */
    public static String extractFieldNameFromSetterMethod(Method method) {
        String candidateField = null;
        candidateField = method.getName().substring(3);
        if (!candidateField.equals("")) {
            candidateField = Character.toLowerCase(candidateField.charAt(0)) + candidateField.substring(1);
        } else {
            MessageHelper.warn("Encountered method set. This will be ignored.");
        }
        return candidateField;
    }
}
