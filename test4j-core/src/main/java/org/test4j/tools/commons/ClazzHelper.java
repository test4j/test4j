package org.test4j.tools.commons;

import ext.test4j.apache.commons.io.IOUtils;
import org.objenesis.ObjenesisHelper;
import org.test4j.exception.NewInstanceException;
import org.test4j.exception.Test4JException;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.spring.interal.SpringModuleHelper;
import org.test4j.tools.datagen.ConstructorArgsGenerator;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.reflect.Modifier.isStatic;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ClazzHelper {
    private static final Map<String, Boolean> clazAvailableCache = new HashMap<String, Boolean>();

    /**
     * Utility method that verifies whether the class with the given fully
     * qualified classname is available in the classpath.
     *
     * @param className The name of the class
     * @return True if the class with the given name is available
     */
    public static boolean isClassAvailable(String className) {
        Boolean isAvailable = clazAvailableCache.get(className);
        if (isAvailable != null) {
            return isAvailable;
        }
        try {
            Thread.currentThread().getContextClassLoader().loadClass(className);
            clazAvailableCache.put(className, true);
            return true;
        } catch (ClassNotFoundException e) {
            clazAvailableCache.put(className, false);
            return false;
        }
    }

    /**
     * Gets the class for the given name. An Test4JException is thrown when the
     * class could not be loaded.
     *
     * @param className The name of the class, not null
     * @return The class, not null
     */
    public static <T> Class<T> getClazz(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (Throwable t) {
            throw new Test4JException("Could not load class with name " + className, t);
        }
    }

    public final static String getPackFromClassName(String clazzName) {
        int index = clazzName.lastIndexOf(".");
        String pack = "";
        if (index > 0) {
            pack = clazzName.substring(0, index);
        }
        return pack;
    }

    /**
     * 根据类名获得package的路径<br>
     * a.b.c.ImplClazz 返回 a/b/c
     *
     * @param clazzName
     * @return
     */
    public final static String getPathFromPath(String clazzName) {
        String pack = getPackFromClassName(clazzName);
        return pack.replace(".", "/");
    }

    /**
     * 根据类名获得package的路径<br>
     * a.b.c.ImplClazz 返回 "a/b/c"<br>
     * if clazz==null, 返回 ""
     *
     * @param clazz
     * @return
     */
    public final static String getPathFromPath(Class clazz) {
        if (clazz == null) {
            return "";
        } else {
            return getPathFromPath(clazz.getName());
        }
    }

    /**
     * 创建className的实例<br>
     * Creates an instance of the class with the given name. The class's no
     * argument constructor is used to create an instance.
     *
     * @param className The name of the class, not null
     * @return An instance of this class
     * @throws Test4JException if the class could not be found or no instance
     *                         could be created
     */
    public static <T> T createInstanceOfType(String className) {
        try {
            Class type = Class.forName(className);
            return (T) newInstance(type);
        } catch (ClassCastException e) {
            throw new Test4JException("Class " + className + " is not of expected type.", e);
        } catch (NoClassDefFoundError e) {
            throw new Test4JException("Unable to load class " + className, e);
        } catch (ClassNotFoundException e) {
            throw new Test4JException("Class " + className + " not found", e);
        } catch (Test4JException e) {
            throw e;
        } catch (Throwable e) {
            throw new Test4JException("Error while instantiating class " + className, e);
        }
    }

    /**
     * Checks whether the given fromType is assignable to the given toType, also
     * taking into account possible auto-boxing.<br>
     * Check if the right-hand side type may be assigned to the left-hand side
     * type following the Java generics rules.
     *
     * @param fromType The from type, not null
     * @param toType   The to type, not null
     * @return True if assignable
     */
    public static boolean isAssignable(Type fromType, Type toType) {
        if (fromType instanceof Class && toType instanceof Class) {
            Class fromClass = (Class) fromType;
            Class toClass = (Class) toType;

            // handle auto boxing types
            if (boolean.class.equals(fromClass) && Boolean.class.isAssignableFrom(toClass)
                    || boolean.class.equals(toClass) && Boolean.class.isAssignableFrom(fromClass)) {
                return true;
            }
            if (char.class.equals(fromClass) && Character.class.isAssignableFrom(toClass) || char.class.equals(toClass)
                    && Character.class.isAssignableFrom(fromClass)) {
                return true;
            }
            if (int.class.equals(fromClass) && Integer.class.isAssignableFrom(toClass) || int.class.equals(toClass)
                    && Integer.class.isAssignableFrom(fromClass)) {
                return true;
            }
            if (long.class.equals(fromClass) && Long.class.isAssignableFrom(toClass) || long.class.equals(toClass)
                    && Long.class.isAssignableFrom(fromClass)) {
                return true;
            }
            if (float.class.equals(fromClass) && Float.class.isAssignableFrom(toClass) || float.class.equals(toClass)
                    && Float.class.isAssignableFrom(fromClass)) {
                return true;
            }
            if (double.class.equals(fromClass) && Double.class.isAssignableFrom(toClass)
                    || double.class.equals(toClass) && Double.class.isAssignableFrom(fromClass)) {
                return true;
            }
            return toClass.isAssignableFrom(fromClass);
        } else {
            if (toType.equals(fromType)) {
                return true;
            }
            if (toType instanceof Class && fromType instanceof Class) {
                return ((Class) toType).isAssignableFrom((Class) fromType);
            }
            if (toType instanceof ParameterizedType && fromType instanceof ParameterizedType) {
                return isAssignable((ParameterizedType) toType, (ParameterizedType) fromType);
            }
            if (toType instanceof WildcardType) {
                return isAssignable((WildcardType) toType, fromType);
            }
            return false;
        }
    }

    private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
        if (lhsType.equals(rhsType)) {
            return true;
        }
        Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
        Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
        if (lhsTypeArguments.length != rhsTypeArguments.length) {
            return false;
        }
        for (int size = lhsTypeArguments.length, i = 0; i < size; ++i) {
            Type lhsArg = lhsTypeArguments[i];
            Type rhsArg = rhsTypeArguments[i];
            if (!lhsArg.equals(rhsArg)
                    && !(lhsArg instanceof WildcardType && isAssignable((WildcardType) lhsArg, rhsArg))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignable(WildcardType lhsType, Type rhsType) {
        Type[] upperBounds = lhsType.getUpperBounds();
        Type[] lowerBounds = lhsType.getLowerBounds();
        for (int size = upperBounds.length, i = 0; i < size; ++i) {
            if (!isAssignable(upperBounds[i], rhsType)) {
                return false;
            }
        }
        for (int size = lowerBounds.length, i = 0; i < size; ++i) {
            if (!isAssignable(rhsType, lowerBounds[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据字符串获取枚举值<br>
     * Gets the enum value that has the given name.
     *
     * @param enumClass     The enum class, not null
     * @param enumValueName The name of the enum value, not null
     * @return The actual enum value, not null
     * @throws Test4JException if no value could be found with the given name
     */
    public static <T extends Enum<?>> T getEnumValue(Class<T> enumClass, String enumValueName) {
        T[] enumValues = enumClass.getEnumConstants();
        for (T enumValue : enumValues) {
            if (enumValueName.equalsIgnoreCase(enumValue.name())) {

                return enumValue;
            }
        }
        throw new Test4JException("Unable to find a enum value in enum: " + enumClass + ", with value name: "
                + enumValueName);
    }

    /**
     * 读取class的字节码内容byte[]
     *
     * @param clazz
     * @return
     */
    public static byte[] getBytes(Class clazz) {
        String name = clazz.getName().replace('.', '/') + ".class";
        try (InputStream iStream = clazz.getClassLoader().getResourceAsStream(name)) {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();

            IOUtils.copy(iStream, oStream);
            return oStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建claz对象实例<br>
     * 不是通过 new Construction()形式
     *
     * @param <T>
     * @param claz
     * @return
     */
    public static <T> T newInstance(Class<T> claz) {
        if (claz.isMemberClass() && !isStatic(claz.getModifiers())) {
            throw new NewInstanceException(
                    "Creation of an instance of a non-static innerclass is not possible using reflection. The type "
                            + claz.getSimpleName()
                            + " is only known in the context of an instance of the enclosing class "
                            + claz.getEnclosingClass().getSimpleName()
                            + ". Declare the innerclass as static to make construction possible.");
        }
        try {
            return innerNewInstance(claz);
        } catch (NewInstanceException ne) {
            throw ne;
        } catch (Throwable e) {
            throw new NewInstanceException(e);
        }
    }

    private static <T> T innerNewInstance(Class<T> claz) {
        if (claz.isInterface()) {
            throw new RuntimeException("create a fake implementation class for interface:" + claz.getName());
        }
        int modifiers = claz.getModifiers();
        if (Modifier.isAbstract(modifiers)) {
            throw new NewInstanceException("unsupport abstract class!");
        }

        try {
            Constructor constructor = claz.getDeclaredConstructor();
            if (constructor != null) {
                boolean isAccessor = constructor.isAccessible();
                constructor.setAccessible(true);
                Object o = constructor.newInstance();
                constructor.setAccessible(isAccessor);
                return (T) o;
            }
        } catch (Throwable e) {
            MessageHelper.warn("new instance[" + claz.getName() + "] error", e);
            return (T) ObjenesisHelper.newInstance(claz);
        }
        return (T) ObjenesisHelper.newInstance(claz);
    }

    public static <T> T newInstance(Class<T> claz, ConstructorArgsGenerator argGenerator) {
        Constructor constructor = null;
        try {
            constructor = claz.getDeclaredConstructor();
        } catch (Exception e) {
            constructor = claz.getDeclaredConstructors()[0];
        }
        if (constructor == null) {
            constructor = claz.getConstructors()[0];
        }

        try {
            Object[] args = argGenerator.generate(constructor);
            boolean isAccessor = constructor.isAccessible();
            constructor.setAccessible(true);
            Object o = constructor.newInstance(args);
            constructor.setAccessible(isAccessor);
            return (T) o;
        } catch (Exception e) {
            MessageHelper.warn("new instance[" + claz.getName() + "] error.", e);
            return (T) ObjenesisHelper.newInstance(claz);
        }
    }

    /**
     * 返回可以class中定义的字段(包含父类定义的)<br>
     *
     * @param clazz
     * @param filters          需要过滤掉的字段
     * @param includeStatic    是否包含static修饰的字段
     * @param includeFinal     是否包含final修饰的字段
     * @param includeTransient 是否包含transient修饰的字段
     * @return
     */
    public static final List<Field> getAllFields(Class clazz, Collection<String> filters, boolean includeStatic,
                                                 boolean includeFinal, boolean includeTransient) {
        List<Field> jsonFields = new ArrayList<Field>();

        List<Field> fields = Reflector.getAllFields(clazz);
        for (Field field : fields) {
            String fieldname = field.getName();
            if (filters != null && filters.contains(fieldname)) {
                continue;
            }
            int modifier = field.getModifiers();
            if (!includeStatic && Modifier.isStatic(modifier)) {
                continue;
            }
            if (!includeFinal && Modifier.isFinal(modifier)) {
                continue;
            }
            if (!includeTransient && Modifier.isTransient(modifier)) {
                continue;
            }
            if (jsonFields.contains(field)) {
                continue;
            }
            jsonFields.add(field);
        }
        return jsonFields;
    }

    private final static String Proxy_Type_Pattern = ".*\\$[Pp]roxy\\d+.*";

    /**
     * 返回非代理类的类型
     *
     * @param clazz
     * @return
     */
    public static Class getUnProxyType(Class clazz) {
        if (Proxy.isProxyClass(clazz)) {
            return Object.class;
        }
        Class type = clazz;
        while (type.isAnonymousClass() || type.getName().matches(Proxy_Type_Pattern)) {
            type = type.getSuperclass();
        }

        return type;
    }

    /**
     * 如果是spring代理对象，获得被代理的目标对象
     *
     * @param target
     * @return
     */
    public static Object getProxiedObject(Object target) {
        try {
            if (ClazzHelper.isClassAvailable("org.springframework.aop.framework.Advised")) {
                Object o = SpringModuleHelper.getAdvisedObject(target);
                return o;
            } else {
                return target;
            }
        } catch (Exception e) {
            MessageHelper.warn("get proxy object error.", e);
            return target;
        }
    }

    /**
     * 是否是抽象类且非primitive类型
     *
     * @param clazz
     * @return
     */
    public static boolean isAbstract(Class clazz) {
        boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
        boolean isPrimitive = PrimitiveHelper.isPrimitiveType(clazz);
        return isAbstract && !isPrimitive;
    }
}
