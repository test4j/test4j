package org.test4j.module.core;

import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.core.internal.TestListener;
import org.test4j.module.core.utility.ClazzAroundObject.ClazzAfterObject;
import org.test4j.module.core.utility.*;
import org.test4j.module.spec.internal.SpecContext;
import org.test4j.tools.commons.ConfigHelper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * test4j的核心类，所有事件监听器的总入口<br>
 */
public class CoreModule {
    /**
     * 初始化test4j,要保证这个方法在使用test4j功能之前被调用<br>
     * <br>
     * Initializes the singleton instance to the default value, loading the
     * configuration using the {@link ConfigurationLoader}
     */
    static boolean hasInitial = false;

    static {
        JMockitHelper.getJMockitJavaagentHit();
    }

    private static CoreModule instance;

    /* Listener that observes the execution of tests */
    private final TestListener testListener;

    /**
     * Creates a new instance.
     */
    public CoreModule() {
        List<Module> modules = ModulesLoader.loading();
        this.testListener = new CoreModuleListener();
        for (Module module : modules) {
            module.afterInit();
        }
    }

    /**
     * Returns the singleton instance
     *
     * @return the singleton instance, not null
     */
    public static synchronized CoreModule getInstance() {
        if (instance == null) {
            initSingletonInstance();
        }
        return instance;
    }

    public static void initSingletonInstance() {
        if (hasInitial) {
            return;
        }
        try {
            hasInitial = true;
            ConfigurationLoader.loading();
            MessageHelper.level = ConfigHelper.logLevel();
            instance = new CoreModule();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * Returns the single instance of {@link TestListener}. This instance
     * provides hook callback methods that enable intervening during the
     * execution of unit tests.
     *
     * @return The single {@link TestListener}
     */
    public static TestListener getTestListener() {
        return getInstance().testListener;
    }

    private class CoreModuleListener extends TestListener {
        @SuppressWarnings("rawtypes")
        @Override
        public void beforeClass(Class testClazz) {
            MessageHelper.resetLog4jLevel();
            Test4JContext.setContext(testClazz);
            ModulesManager.getTestListeners()
                    .forEach(listener -> listener.beforeClass(testClazz));
        }

        @Override
        public void beforeMethod(Object testObject, Method testMethod) {
            Test4JContext.setContext(testObject, testMethod);
            ModulesManager.getTestListeners()
                    .forEach(listener -> listener.beforeMethod(testObject, testMethod));
        }

        @Override
        public void afterMethod(Object testObject, Method testMethod, Throwable throwable) {
            Test4JContext.setContext(testObject, testMethod);
            ModulesManager.getTestListeners_Reverse()
                    .forEach(listener -> listener.afterMethod(testObject, testMethod, throwable));
        }

        @Override
        public void afterClass(Class testClass) {
            ModulesManager.getTestListeners_Reverse()
                    .forEach(listener -> listener.afterClass(testClass));
            Test4JContext.setContext(testClass);
            SpecContext.clean();
        }

        @Override
        protected String getName() {
            return "CoreModuleListener";
        }
    }
}
