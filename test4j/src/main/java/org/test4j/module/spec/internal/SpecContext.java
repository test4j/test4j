package org.test4j.module.spec.internal;

/**
 * story测试上下文期望
 *
 * @author darui.wu
 * @create 2019-10-15
 */
public class SpecContext {
    private static ThreadLocal<Object> sharedData = new ThreadLocal<>();

    private static ThreadLocal<Throwable> exceptedException = new ThreadLocal<>();

    /**
     * 设置story测试步骤共享数据
     *
     * @param data
     */
    public static void setSharedData(Object data) {
        sharedData.set(data);
    }

    /**
     * 返回共享数据
     *
     * @return
     */
    public static Object getSharedData() {
        return sharedData.get();
    }

    /**
     * 设置期望异常
     *
     * @param e
     */
    public static void setExceptedException(Throwable e) {
        exceptedException.set(e);
    }

    /**
     * 返回期望异常
     *
     * @return
     */
    public static Throwable getExceptedException() {
        return exceptedException.get();
    }

    public static void clean() {
        exceptedException.remove();
        sharedData.remove();
    }
}
