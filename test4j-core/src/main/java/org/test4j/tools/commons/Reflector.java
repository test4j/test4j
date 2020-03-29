package org.test4j.tools.commons;

import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.exception.NoSuchMethodRuntimeException;
import org.test4j.json.JSON;
import org.test4j.tools.datagen.ConstructorArgsGenerator;
import org.test4j.tools.reflector.FieldAccessor;
import org.test4j.tools.reflector.MethodAccessor;
import org.test4j.tools.reflector.imposteriser.Test4JProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Reflector {
    public final static Reflector instance = new Reflector();

    private Reflector() {
    }

    /**
     * 获取方法
     *
     * @param cls
     * @param name
     * @return
     */
    public static Field getField(Class cls, String name) {
        while (cls != Object.class) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldRuntimeException("No such field: " + name);
    }


    /**
     * 返回类所有的字段（包括父类的）<br>
     * Gets all fields of the given class and all its super-classes.
     *
     * @param clazz The class
     * @return The fields, not null
     */
    public static List<Field> getAllFields(final Class clazz) {
        List<Field> result = new ArrayList<Field>();
        if (clazz == null || clazz.equals(Object.class)) {
            return result;
        }

        Class type = clazz;
        while (type != Object.class && type != null) {
            // add all fields of this class
            Field[] declaredFields = type.getDeclaredFields();
            result.addAll(asList(declaredFields));

            type = type.getSuperclass();
        }
        return result;
    }

    /**
     * Returns all declared fields of the given class that are assignable from
     * the given type.
     *
     * @param clazz The class to get fields from, not null
     * @param type  The type, not null
     * @return A list of Fields, empty list if none found
     */
    public static Set<Field> getFieldsAssignableFrom(Class clazz, Type type) {
        Set<Field> fieldsOfType = new HashSet<Field>();
        List<Field> allFields = getAllFields(clazz);
        for (Field field : allFields) {
            boolean isAssignFrom = ClazzHelper.isAssignable(type, field.getGenericType());
            if (isAssignFrom) {
                fieldsOfType.add(field);
            }
        }
        return fieldsOfType;
    }

    /**
     * Returns the fields in the given class that have the exact given type. The
     * class's superclasses are also investigated.
     *
     * @param clazz The class to get the field from, not null
     * @param type  The type, not null
     * @return The fields with the given type
     */
    public static Set<Field> getFieldsOfType(Class clazz, Type type) {
        Set<Field> fields = new HashSet<Field>();
        List<Field> allFields = getAllFields(clazz);
        for (Field field : allFields) {
            boolean isTypeEquals = field.getType().equals(type);
            if (isTypeEquals) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static void setFieldValue(Object target, String fieldName, Object fieldValue) {
        FieldAccessor.field(target, fieldName).set(target, fieldValue);
    }

    public static void setFieldValue(Class klass, String fieldName, Object fieldValue) {
        FieldAccessor.field(klass, fieldName).setStatic(fieldValue);
    }

    public static Object getFieldValue(Object target, String fieldName) {
        return FieldAccessor.field(target, fieldName).get(target);
    }

    public static Object getFieldValue(Class klass, String fieldName) {
        return FieldAccessor.field(klass, fieldName).getStatic();
    }

    public static Object getFieldValue(Object target, Field field) {
        return FieldAccessor.field(field).get(target);
    }


    /**
     * 创建target对象field字段的代理实例<br>
     * 用于运行时转移代理操作到字段对象上
     *
     * @param <T>
     * @param klass
     * @param fieldName
     * @return
     */
    public <T> T newProxy(Class klass, String fieldName) {
        if (klass == null) {
            throw new RuntimeException("can't get a field from null object.");
        }
        Field field = Reflector.getField(klass, fieldName);
        Object proxy = Test4JProxy.proxy(klass, field);
        return (T) proxy;
    }

    /**
     * 创建claz对象实例<br>
     * 不是通过 new Construction()形式
     *
     * @param <T>
     * @param claz
     * @return
     */
    public <T> T newInstance(Class<T> claz) {
        Object o = ClazzHelper.newInstance(claz);
        return (T) o;
    }

    /**
     * 根据构造函数对象
     *
     * @param claz
     * @param argGenerator
     * @return
     */
    public <T> T newInstance(Class<T> claz, ConstructorArgsGenerator argGenerator) {
        Object o = ClazzHelper.newInstance(claz, argGenerator);
        return (T) o;
    }

    /**
     * 从json字符串创建对象
     *
     * @param <T>
     * @param json
     * @return
     */
    public <T> T newInstance(String json) {
        Object o = JSON.toObject(json);
        return (T) o;
    }

    /**
     * 从json字符串创建对象
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public <T> T newInstance(String json, Class<T> clazz) {
        Object o = JSON.toObject(json, clazz);
        return (T) o;
    }


    /**
     * 返回class中名称为name,参数类型为 parametersType的方法
     *
     * @param cls
     * @param name
     * @param parametersType
     * @return
     */
    public static final Method getMethod(Class cls, String name, Class... parametersType) {
        while (cls != Object.class) {
            try {
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(name) == false) {
                        continue;
                    }
                    if (matchParasType(parametersType, method.getParameterTypes())) {
                        method.setAccessible(true);
                        return method;
                    }
                }
                throw new NoSuchMethodRuntimeException();
            } catch (NoSuchMethodRuntimeException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodRuntimeException("No such method: " + name + "(" + Arrays.toString(parametersType) + ")");
    }

    /**
     * 查找名为name，参数个数为args的方法列表
     *
     * @param cls
     * @param name
     * @param args
     * @return
     */
    public static final List<Method> getMethod(Class cls, String name, int args) {
        List<Method> methods = new ArrayList<Method>();
        while (cls != Object.class) {
            try {
                Method[] declares = cls.getDeclaredMethods();
                for (Method method : declares) {
                    if (method.getName().equals(name) == false) {
                        continue;
                    }
                    if (method.getParameterTypes().length == args) {
                        methods.add(method);
                    }
                }
                throw new NoSuchMethodRuntimeException();
            } catch (NoSuchMethodRuntimeException e) {
                cls = cls.getSuperclass();
            }
        }
        return methods;
    }

    /**
     * Returns all declared setter methods of fields of the given class that are
     * assignable from the given type.
     *
     * @param clazz    The class to get setters from, not null
     * @param type     The type, not null
     * @param isStatic True if static setters are to be returned, false for
     *                 non-static
     * @return A list of Methods, empty list if none found
     */
    public static Set<Method> getSettersAssignableFrom(Class clazz, Type type, boolean isStatic) {
        Set<Method> settersAssignableFrom = new HashSet<Method>();

        Set<Method> allMethods = getAllMethods(clazz);
        for (Method method : allMethods) {
            if (isSetterMethod(method) && ClazzHelper.isAssignable(type, method.getGenericParameterTypes()[0])
                    && (isStatic == isStatic(method.getModifiers()))) {
                settersAssignableFrom.add(method);
            }
        }
        return settersAssignableFrom;
    }

    /**
     * Gets all methods of the given class and all its super-classes.
     *
     * @param clazz The class
     * @return The methods, not null
     */
    public static Set<Method> getAllMethods(Class clazz) {
        Set<Method> result = new HashSet<Method>();
        if (clazz == null || clazz.equals(Object.class)) {
            return result;
        }

        // add all methods of this class
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isSynthetic() || declaredMethod.isBridge()) {
                // skip methods that were added by the compiler
                continue;
            }
            result.add(declaredMethod);
        }
        // add all methods of the super-classes
        result.addAll(getAllMethods(clazz.getSuperclass()));
        return result;
    }

    /**
     * Returns the setter methods in the given class that have an argument with
     * the exact given type. The class's superclasses are also investigated.
     *
     * @param clazz The class to get the setter from, not null
     * @param type  The type, not null
     * @return All setters for an object of the given type
     */
    public static Set<Method> getSettersOfType(Class clazz, Type type) {
        Set<Method> settersOfType = new HashSet<Method>();
        Set<Method> allMethods = getAllMethods(clazz);
        for (Method method : allMethods) {
            if (isSetterMethod(method) && method.getGenericParameterTypes()[0].equals(type)) {
                settersOfType.add(method);
            }
        }
        return settersOfType;
    }

    static boolean matchParasType(Class[] expecteds, Class[] actuals) {
        if (expecteds == null && actuals == null) {
            return true;
        } else if (expecteds == null || actuals == null) {
            return false;
        } else if (expecteds.length != actuals.length) {
            return false;
        }
        for (int index = 0; index < expecteds.length; index++) {
            Class expected = expecteds[index];
            Class actual = actuals[index];
            if (expected == null || expected == actual) {
                continue;
            } else if (actual != null && actual.isAssignableFrom(expected)) {
                continue;
            } else if (PrimitiveHelper.isPrimitiveTypeEquals(expected, actual)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * @param method The method to check, not null
     * @return True if the given method is the {@link Object#equals} method
     */
    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && 1 == method.getParameterTypes().length
                && Object.class.equals(method.getParameterTypes()[0]);
    }

    /**
     * @param method The method to check, not null
     * @return True if the given method is the {@link Object#hashCode} method
     */
    public static boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && 0 == method.getParameterTypes().length;
    }

    /**
     * @param method The method to check, not null
     * @return True if the given method is the {@link Object#toString} method
     */
    public static boolean isToStringMethod(Method method) {
        return "toString".equals(method.getName()) && 0 == method.getParameterTypes().length;
    }

    /**
     * @param method The method to check, not null
     * @return True if the given method is the {@link Object#clone} method
     */
    public static boolean isCloneMethod(Method method) {
        return "clone".equals(method.getName()) && 0 == method.getParameterTypes().length;
    }

    public static boolean isFinalizeMethod(Method method) {
        return "finalize".equals(method.getName()) && 0 == method.getParameterTypes().length;
    }

    /**
     * 判断方法是否是setter方法<br>
     * For each method, check if it can be a setter for an object of the given
     * type. A setter is a method with the following properties:
     * <ul>
     * <li>Method name is > 3 characters long and starts with set</li>
     * <li>The fourth character is in uppercase</li>
     * <li>The method has one parameter, with the type of the property to set</li>
     * </ul>
     *
     * @param method The method to check, not null
     * @return True if the given method is a setter, false otherwise
     */
    public static boolean isSetterMethod(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("set") == false || method.getParameterTypes().length != 1 || methodName.length() < 4) {
            return false;
        }

        String fourthLetter = methodName.substring(3, 4);
        if (fourthLetter.toUpperCase().equals(fourthLetter)) {
            return true;
        } else {
            return false;
        }
    }

    public static Class[] getTypes(Object... args) {
        if (args == null) {
            return new Class[0];
        }

        List<Class> clazes = new ArrayList<Class>();
        for (Object para : args) {
            clazes.add(para == null ? null : para.getClass());
        }
        return clazes.toArray(new Class[0]);
    }

    /**
     * 测试类是否是 public static void且是无参形式的?
     *
     * @param method
     * @return
     */
    public static boolean isPublicStaticVoid(Method method) {
        return method.getReturnType() == void.class && method.getParameterTypes().length == 0
                && (method.getModifiers() & Modifier.STATIC) != 0 && (method.getModifiers() & Modifier.PUBLIC) != 0;
    }

    public Object invoke(Object target, String method, Object... args) {
        return MethodAccessor.invoke(target, method, args);
    }

    public Object invoke(Class klass, String method, Object... args) {
        return MethodAccessor.invoke(klass, method, args);
    }
}
