package org.test4j.tools.reflector;

import lombok.Getter;
import org.test4j.exception.NoSuchMethodRuntimeException;
import org.test4j.exception.ReflectionException;
import org.test4j.exception.Test4JException;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.Reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * 方法访问器
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MethodAccessor {
    @Getter
    private final Method method;
    @Getter
    private final Class declaringClass;

    private MethodAccessor(Class declaringClass, String methodName, Class... parametersType) {
        this.declaringClass = declaringClass;
        this.method = Reflector.getMethod(declaringClass, methodName, parametersType);
    }

    private MethodAccessor(Object target, String methodName, Class... parametersType) {
        Object _target = ClazzHelper.getProxiedObject(target);
        this.declaringClass = _target.getClass();
        this.method = Reflector.getMethod(declaringClass, methodName, parametersType);
    }

    private MethodAccessor(Method method) {
        this.method = method;
        this.declaringClass = method.getDeclaringClass();
    }

    /**
     * 执行方法
     *
     * @param target
     * @param args   方法参数
     * @return
     */
    public <T> T invoke(Object target, Object... args) {
        boolean isAccessible = this.method.isAccessible();
        try {
            this.method.setAccessible(true);
            Object _target = ClazzHelper.getProxiedObject(target);
            return (T) method.invoke(_target, args);
        } catch (InvocationTargetException e) {
            Throwable te = e.getTargetException();
            if (te instanceof RuntimeException) {
                throw (RuntimeException) te;
            } else {
                throw new Test4JException(te);
            }
        } catch (IllegalAccessException ie) {
            throw new Test4JException(ie);
        } finally {
            this.method.setAccessible(isAccessible);
        }
    }


    /**
     * 执行静态方法
     *
     * @param args 方法参数
     * @return
     */
    public <T> T invokeStatic(Object... args) {
        if (Modifier.isStatic(method.getModifiers()) == false) {
            String methodDesc = method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")";
            throw new NoSuchMethodRuntimeException("No such static method: " + methodDesc + " in class["
                    + this.declaringClass + "]");
        } else {
            return (T) invoke(null, args);
        }
    }

    /**
     * 构造方法访问器
     *
     * @param declaringClass
     * @param methodName
     * @param parametersType
     * @return
     */
    public static MethodAccessor method(Class declaringClass, String methodName, Class... parametersType) {
        return new MethodAccessor(declaringClass, methodName, parametersType);
    }

    /**
     * 构造方法访问器
     *
     * @param target
     * @param methodName
     * @param parametersType
     * @return
     */
    public static MethodAccessor method(Object target, String methodName, Class... parametersType) {
        return new MethodAccessor(target, methodName, parametersType);
    }

    /**
     * 构造方法访问器
     *
     * @param method
     * @return
     */
    public static MethodAccessor method(Method method) {
        return new MethodAccessor(method);
    }

    /**
     * 根据方法的名称和参数个数查找方法访问器，如果有多于1个同名且参数个数一样的方法，那么抛出异常
     *
     * @param targetClazz
     * @param methodName
     * @param args
     * @return
     */
    public static MethodAccessor method(Class targetClazz, String methodName, int args) {
        List<Method> methods = Reflector.getMethod(targetClazz, methodName, args);
        if (methods.size() == 0) {
            throw new ReflectionException("No such method: " + methodName + ",parameter count:" + args);
        }
        if (methods.size() > 1) {
            throw new ReflectionException("More then one method: " + methodName + ",parameter count:" + args);
        }
        Method method = methods.get(0);
        return MethodAccessor.method(method);
    }


    /**
     * 根据方法的名称和参数个数查找方法访问器，如果有多于1个同名且参数个数一样的方法，那么抛出异常
     *
     * @param target
     * @param methodName
     * @param args
     * @return
     */
    public static MethodAccessor method(Object target, String methodName, int args) {
        Object _target = ClazzHelper.getProxiedObject(target);
        return method(_target.getClass(), methodName, args);
    }

    /**
     * 执行方法
     *
     * @param target
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T invoke(Object target, String methodName, Object... args) {
        Object _target = ClazzHelper.getProxiedObject(target);
        Class[] types = Reflector.getTypes(args);
        return method(_target, methodName, types).invoke(_target, args);
    }

    /**
     * 执行方法
     *
     * @param klass
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T invoke(Class klass, String methodName, Object... args) {
        Class[] types = Reflector.getTypes(args);
        return method(klass, methodName, types).invokeStatic(args);
    }
}
