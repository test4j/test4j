package org.test4j.module.spec.internal;

/**
 * story测试上下文期望
 *
 * @author darui.wu
 * @create 2019-10-15
 */
public class SpecContext {
    /**
     * 测试各步骤中的共享数据
     */
    private static ThreadLocal<Object> sharedData = new ThreadLocal<>();
    /**
     * when步骤执行返回值
     */
    private static ThreadLocal<Object> whenResult = new ThreadLocal<>();
    /**
     * when步骤中的异常
     */
    private static ThreadLocal<Throwable> expectedException = new ThreadLocal<>();

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
    public static void setExpectedException(Throwable e) {
        expectedException.set(e);
    }

    /**
     * 返回期望异常
     *
     * @return
     */
    public static Throwable getExpectedException() {
        return expectedException.get();
    }

    public static void setWhenResult(Object result) {
        whenResult.set(result);
    }

    public static Object getWhenResult() {
        return whenResult.get();
    }

    public static void clean() {
        expectedException.remove();
        sharedData.remove();
        whenResult.remove();
    }
}
