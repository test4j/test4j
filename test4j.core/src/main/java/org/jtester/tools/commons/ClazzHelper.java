package org.jtester.tools.commons;

import static ext.jtester.apache.commons.io.IOUtils.closeQuietly;
import static java.lang.reflect.Modifier.isStatic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mockit.Mockit;

import org.jtester.module.JTesterException;
import org.jtester.tools.datagen.ConstructorArgsGenerator;
import org.jtester.tools.exception.NewInstanceException;
import org.jtester.tools.reflector.MethodAccessor;

import ext.jtester.apache.commons.io.IOUtils;
import ext.jtester.objenesis.ObjenesisHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
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
     * Gets the class for the given name. An JTesterException is thrown when the
     * class could not be loaded.
     * 
     * @param className The name of the class, not null
     * @return The class, not null
     */
    public static <T> Class<T> getClazz(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (Throwable t) {
            throw new JTesterException("Could not load class with name " + className, t);
        }
    }

    /**
     * @param className The name of the class to check, not null
     * @return True if the classfile exists in the classpath
     */
    public static boolean classFileExistsInClasspath(String className) {
        String classFileName = className.replace('.', '/') + ".class";
        return ClazzHelper.class.getClassLoader().getResource(classFileName) != null;
    }

    /**
     * 类路径中是否有 org.hibernate.tool.hbm2ddl.SchemaExport class
     * 
     * @return
     */
    public final static boolean doesImportSchemaExport() {
        try {
            Class.forName("org.hibernate.tool.hbm2ddl.SchemaExport");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
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
     * @param clazzName
     * @return
     */
    public final static String getPathFromPath(Class clazz) {
        if (clazz == null) {
            return "";
        } else {
            return getPathFromPath(clazz.getName());
        }
    }

    public final static void generateClazz(String clazzName, byte[] r) throws IOException {
        // debug，调试生成的字节码
        FileOutputStream file = new FileOutputStream("d:/" + clazzName.substring(clazzName.lastIndexOf('.') + 1)
                + ".class");
        file.write(r);
        file.flush();
        file.close();
    }

    /**
     * package名称有效字符表达式
     */
    public final static String VALID_PACK_REGULAR = "[^\\.]*";

    /**
     * 将带有星号的package名称转换为正则表达式
     * 
     * @param regexPackage 带有星号的package名称，比如 com.**.service.*Impl
     * @return
     */
    public static String getPackageRegex(String regexPackage) {
        String _interfaceKey = regexPackage.replace('*', '#');// 先把*替换成不会被使用的字符#，避免后面的正则表达式替换错误
        String regex = _interfaceKey.replace(".", "\\.");
        regex = regex.replace("\\.##\\.", "(\\.|\\..*\\.)");
        regex = regex.replace("##\\.", ".*\\.");
        regex = regex.replace("#", VALID_PACK_REGULAR);
        return regex;
    }

    /**
     * 判断类型是否是接口或者抽象类
     * 
     * @param type
     * @return
     */
    public static boolean isInterfaceOrAbstract(Class type) {
        if (type == null) {
            throw new RuntimeException("type can't be null.");
        }
        if (type.isInterface()) {
            return true;
        } else if (Modifier.isAbstract(type.getModifiers())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从void setBeanName(Type instance)方法中解析出beanName
     * 
     * @param method
     * @return
     */
    public static String exactBeanName(Method method) {
        String methodname = method.getName();
        if (methodname.equalsIgnoreCase("set") || methodname.startsWith("set") == false) {
            return null;
        }
        String beanName = methodname.substring(3);
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        return beanName;
    }

    /**
     * 创建className的实例<br>
     * Creates an instance of the class with the given name. The class's no
     * argument constructor is used to create an instance.
     * 
     * @param className The name of the class, not null
     * @return An instance of this class
     * @throws JTesterException if the class could not be found or no instance
     *             could be created
     */
    public static <T> T createInstanceOfType(String className) {
        try {
            Class type = Class.forName(className);
            return (T) newInstance(type);
        } catch (ClassCastException e) {
            throw new JTesterException("Class " + className + " is not of expected type.", e);
        } catch (NoClassDefFoundError e) {
            throw new JTesterException("Unable to load class " + className, e);
        } catch (ClassNotFoundException e) {
            throw new JTesterException("Class " + className + " not found", e);
        } catch (JTesterException e) {
            throw e;
        } catch (Throwable e) {
            throw new JTesterException("Error while instantiating class " + className, e);
        }
    }

    /**
     * Checks whether the given fromType is assignable to the given toType, also
     * taking into account possible auto-boxing.<br>
     * Check if the right-hand side type may be assigned to the left-hand side
     * type following the Java generics rules.
     * 
     * @param fromType The from type, not null
     * @param toType The to type, not null
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
     * @param enumClass The enum class, not null
     * @param enumValueName The name of the enum value, not null
     * @return The actual enum value, not null
     * @throws JTesterException if no value could be found with the given name
     */
    public static <T extends Enum<?>> T getEnumValue(Class<T> enumClass, String enumValueName) {
        T[] enumValues = enumClass.getEnumConstants();
        for (T enumValue : enumValues) {
            if (enumValueName.equalsIgnoreCase(enumValue.name())) {

                return enumValue;
            }
        }
        throw new JTesterException("Unable to find a enum value in enum: " + enumClass + ", with value name: "
                + enumValueName);
    }

    /**
     * 获得泛型字段的参数类类型<br>
     * Gets the T from a Class<T> field declaration. An exception is raised if
     * the field type is not generic or has more than 1 generic type
     * 
     * @param field The field to get the type from, not null
     * @return The declared generic type
     */
    public static Type getGenericType(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            Type[] argumentTypes = ((ParameterizedType) type).getActualTypeArguments();
            if (argumentTypes.length == 1) {
                return argumentTypes[0];
            }
            throw new JTesterException("Unable to determine unique generic type for field: " + field
                    + ". The field type declares more than one generic type: " + type);
        }
        throw new JTesterException("Unable to determine unique generic type for field: " + field
                + ". Field type is not a generic type: " + type);
    }

    /**
     * Gets the class instance for the given type instance.
     * 
     * @param type The type to get a class instance for, not null
     * @return The class instance, not null
     */
    public static <T> Class<T> getClassForType(Type type) {
        if (type instanceof Class) {
            return (Class<T>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        }
        throw new JTesterException("Unable to convert Type instance " + type + " to a Class instance.");
    }

    /**
     * 读取class的字节码内容byte[]
     * 
     * @param clazz
     * @return
     */
    public static byte[] getBytes(Class clazz) {
        String name = clazz.getName().replace('.', '/') + ".class";
        InputStream iStream = clazz.getClassLoader().getResourceAsStream(name);
        try {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();

            IOUtils.copy(iStream, oStream);
            return oStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(iStream);
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
            return Mockit.newEmptyProxy(claz);
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
            return (T) ObjenesisHelper.newInstance(claz);
        }
    }

    /**
     * 返回可以class中定义的字段(包含父类定义的)<br>
     * 
     * @param clazz
     * @param filters 需要过滤掉的字段
     * @param includeStatic 是否包含static修饰的字段
     * @param includeFinal 是否包含final修饰的字段
     * @param includeTransient 是否包含transient修饰的字段
     * @return
     */
    public static final List<Field> getAllFields(Class clazz, Collection<String> filters, boolean includeStatic,
                                                 boolean includeFinal, boolean includeTransient) {
        List<Field> jsonFields = new ArrayList<Field>();

        List<Field> fields = FieldHelper.getAllFields(clazz);
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
     * @param type
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

    private static MethodAccessor springModuleHelper;

    private static MethodAccessor getSpringModuleHelper() throws ClassNotFoundException {
        if (springModuleHelper == null) {
            Class claz = Class.forName("org.jtester.module.spring.utility.SpringModuleHelper");
            springModuleHelper = new MethodAccessor(claz, "getAdvisedObject", Object.class);
        }
        return springModuleHelper;
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
                Object o = getSpringModuleHelper().invokeStatic(new Object[] { target });
                return o;
            } else {
                return target;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return target;
        }
    }

    /**
     * 是否是class文件
     * 
     * @param classFileName
     * @return
     */
    public static boolean isClassFile(String classFileName) {
        return classFileName.endsWith(".class");
    }

    /**
     * 是否是内部类
     * 
     * @param className
     * @return
     */
    public static boolean isInnerClass(String className) {
        return className.contains("$");
    }

    /**
     * 将class的全路径替换为带.的class名称<br>
     * ex: convert /a/b.class to a.b
     * 
     * @param clazzPath
     * @return
     */
    public static String replaceFileSeparators(String clazzPath) {
        String clazzName = clazzPath.replace(File.separatorChar, '.');
        if (File.separatorChar != '/') {
            // In Jar-Files it's always '/'
            clazzName = clazzName.replace('/', '.');
        }
        if (clazzName.startsWith(".")) {
            return clazzName.substring(1);
        } else {
            return clazzName;
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
