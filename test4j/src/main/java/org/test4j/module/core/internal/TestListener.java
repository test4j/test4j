package org.test4j.module.core.internal;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public abstract class TestListener {

    /**
     * 在@BeforeClass前执行
     *
     * @param testClazz The test class, not null
     */
    public void beforeClass(Class testClazz) {
        // empty
    }


    /**
     * 在测试方法执行前，setup方法后
     *
     * @param testObject The test instance, not null
     * @param testMethod The test method, not null
     */
    public void beforeMethod(Object testObject, Method testMethod) {
        // empty
    }

    /**
     * 在测试方法之后 @After之前执行
     *
     * @param testObject    The test instance, not null
     * @param testMethod    The test method, not null
     * @param testThrowable The throwable thrown during the test or
     *                      beforeMethodRunning, null if none was thrown
     */
    public void afterMethod(Object testObject, Method testMethod, Throwable testThrowable) {
        // empty
    }

    /**
     * 在 @AfterClass之前执行
     *
     * @param testObject
     * @throws Exception
     */
    public void afterClass(Object testObject) {
        // empty
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * 监听器名称
     *
     * @return
     */
    protected abstract String getName();
}
