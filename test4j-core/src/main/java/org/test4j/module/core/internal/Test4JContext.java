package org.test4j.module.core.internal;

import lombok.Setter;
import org.test4j.exception.MultipleException;
import org.test4j.module.core.utility.ClazzAroundObject;
import org.test4j.tools.commons.ExceptionWrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试类（对象）上下文
 *
 * @author darui.wudr 2013-1-10 下午10:53:01
 */
@SuppressWarnings("rawtypes")
public class Test4JContext {
    private final static Test4JContext context = new Test4JContext();

    private Object springTestContextManager;

    /**
     * 当前正在运行的测试类
     */
    private Class testedClazz;

    /**
     * 当前正在运行的测试类实例
     */
    @Setter
    private Object testedObject;

    /**
     * 当前正在运行的测试方法
     */
    @Setter
    private Method testedMethod;

    /**
     * 异常收集
     */
    private List<Throwable> errors;

    private Test4JContext() {
    }

    public final static Test4JContext context() {
        return context;
    }

    /**
     * 设置测试上下文的测试类<br>
     * 测试实例和测试方法置空
     *
     * @param claz
     */
    public final static void setContext(Class claz) {
        context.testedClazz = claz;
        // context.testedObject = null;
        context.testedMethod = null;
    }

    /**
     * 设置测试上下文信息
     *
     * @param testedObject
     * @param testedMethod
     */
    public final static void setContext(Object testedObject, Method testedMethod) {
        if (testedObject == null) {
            throw new RuntimeException("tested object can't be null.");
        }
        if (testedObject instanceof ClazzAroundObject) {
            context.testedClazz = ((ClazzAroundObject) testedObject).getClazz();
        } else {
            context.testedClazz = testedObject.getClass();
        }
        context.testedObject = testedObject;
        context.testedMethod = testedMethod;
        context.errors = new ArrayList<Throwable>();
    }

    /**
     * 封装多个异常，如果当前测试没有记录的历史异常，则直接返回cause<br>
     * 否则返回一个MutipleException
     *
     * @param cause
     * @return
     */
    public static RuntimeException getMultipleException(Throwable cause) {
        if (context.errors == null || context.errors.size() == 0) {
            return ExceptionWrapper.wrapWithRuntimeException(cause);
        }
        MultipleException exception = new MultipleException(cause);
        for (Throwable e : context.errors) {
            exception.addException(e);
        }
        return exception;
    }

    public static Method currTestedMethod() {
        return context.testedMethod;
    }

    /**
     * 当前测试类的类名称
     *
     * @return
     */
    public static Class currTestedClazz() {
        if (context.testedClazz == null) {
            throw new RuntimeException("tested class can't be null.");
        } else {
            return context.testedClazz;
        }
    }

    /**
     * 当前测试类
     *
     * @return
     */
    public static Object currTestedObject() {
        if (context.testedObject == null) {
            throw new RuntimeException("tested object can't be null.");
        } else {
            return context.testedObject;
        }
    }

    public static Object getSpringTestContextManager() {
        return context.springTestContextManager;
    }

    public static void setSpringTestContextManager(Object springTestContextManager) {
        context.springTestContextManager = springTestContextManager;
    }
}
