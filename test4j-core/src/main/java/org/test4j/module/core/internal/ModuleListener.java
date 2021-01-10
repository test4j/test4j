package org.test4j.module.core.internal;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public abstract class ModuleListener {

    /**
     * 在@BeforeClass前执行
     *
     * @param testClazz The test class, not null
     */
    public void beforeAll(Class testClazz) {
        // empty
    }

    /**
     * 在 @AfterClass之前执行
     *
     * @param testClass
     * @throws Exception
     */
    public void afterAll(Class testClass) {
        // empty
    }

    /**
     * 在测试方法执行前，setup方法后
     *
     * @param testObject The test instance, not null
     * @param testMethod
     */
    public void beforeExecute(Object testObject, Method testMethod) {
        // empty
    }

    /**
     * @After之前执行
     */
    public void afterExecute(Object testObject, Method testMethod, Throwable testThrowable) {
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