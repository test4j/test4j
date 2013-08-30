package org.jtester.module.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jtester.tools.commons.ExceptionWrapper;
import org.jtester.tools.exception.MultipleException;

/**
 * 测试类（对象）上下文
 * 
 * @author darui.wudr 2013-1-10 下午10:53:01
 */
@SuppressWarnings("rawtypes")
public class TestContext {
    private final static TestContext context = new TestContext();

    public final static TestContext context() {
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
     * 当前正在运行的测试类
     */
    private Class           testedClazz;

    /**
     * 当前正在运行的测试类实例
     */
    private Object          testedObject;

    /**
     * 当前正在运行的测试方法
     */
    private Method          testedMethod;

    /**
     * 异常收集
     */
    private List<Throwable> errors;

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
     * 用来收集测试过程中可能被其它异常吞噬的异常
     * 
     * @param e
     */
    public static final void addThrowable(Throwable e) {
        if (context.errors == null) {
            context.errors = new ArrayList<Throwable>();
        }
        context.errors.add(e);
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

    private TestContext() {
    }

    /**
     * 当前测试类名称
     * 
     * @return
     */
    public static String currTestedClazzName() {
        if (context.testedClazz == null) {
            throw new RuntimeException("tested class can't be null.");
        } else {
            return context.testedClazz.getName();
        }
    }

    public static String currTestedMethodName() {
        if (context.testedClazz == null) {
            throw new RuntimeException("tested class can't be null.");
        } else {
            return context.testedClazz.getName() + "."
                    + (context.testedMethod == null ? "<init>" : context.testedMethod.getName());
        }
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
}
