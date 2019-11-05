package org.test4j.module.core.internal;

import java.lang.reflect.Method;

import org.test4j.module.core.CoreModule;
import org.test4j.module.core.utility.MessageHelper;

/**
 * test4j Listener执行器
 * 
 * @author darui.wudr
 */
public class ListenerExecutor {
    private final static String TEST_CLAZZ_INFO  = "%s executing test class[%s] in thread[%d].";

    private final static String TEST_METHOD_INFO = "%s executing test method[%s . %s ()] in thread[%d].";

    /**
     * 执行setup class的事件
     * 
     * @param testClazz 测试对象
     * @return 事件异常
     */
    @SuppressWarnings("rawtypes")
    public static Throwable executeBeforeClassEvents(Class testClazz) {
        String hits = String.format(TEST_CLAZZ_INFO, "Begin", testClazz.getName(), Thread.currentThread().getId());
        MessageHelper.info("\n\n\n" + hits);

        try {
            getTestListener().beforeClass(testClazz);
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 执行测试方法开始的事件
     * 
     * @param testedObject 测试对象
     * @param testedMethod 测试方法
     * @return 事件异常
     */
    public static Throwable executeBeforeMethodEvents(Object testedObject, Method testedMethod) {
        String hits = String.format(TEST_METHOD_INFO, "Begin", testedObject.getClass().getName(),
                testedMethod.getName(), Thread.currentThread().getId());
        MessageHelper.info("\n" + hits);

        try {
            getTestListener().beforeMethod(testedObject, testedMethod);
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 执行测试方法完毕后的事件
     * 
     * @param testedMethod 测试方法
     * @param error 测试方法异常
     * @return 返回事件异常
     */
    public static Throwable executeAfterRunnedEvents(Object testedObject, Method testedMethod, Throwable error) {
        try {
            getTestListener().afterMethod(testedObject, testedMethod, error);
            return null;
        } catch (Throwable e) {
            return e;
        }
    }

    /**
     * 执行teardownClass的事件
     * 
     * @param testedObject 测试对象
     * @return 事件异常
     */
    public static Throwable executeAfterClassEvents(Object testedObject) {
        String clazName = testedObject.getClass().getName();
        String hits = String.format(TEST_CLAZZ_INFO, "End", clazName, Thread.currentThread().getId());
        try {
            getTestListener().afterClass(testedObject);
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return e;
        } finally {
            MessageHelper.info(hits + "\n");
        }
    }

    /**
     * @return The test4j test listener
     */
    private static TestListener getTestListener() {
        return CoreModule.getTestListener();
    }
}
