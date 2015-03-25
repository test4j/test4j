package org.test4j.module.core;

import java.lang.reflect.Method;
import java.util.List;

import org.test4j.module.core.ClazzAroundObject.ClazzAfterObject;
import org.test4j.module.core.utility.ConfigurationLoader;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.core.utility.ModulesLoader;
import org.test4j.module.core.utility.ModulesManager;
import org.test4j.module.jmockit.utility.JMockitModuleHelper;
import org.test4j.tools.commons.ConfigHelper;

/**
 * test4j的核心类，所有事件监听器的总入口<br>
 */
public class CoreModule {
    static {
        JMockitModuleHelper.getJMockitJavaagentHit();
    }
    private static CoreModule instance;

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

    /**
     * 初始化test4j,要保证这个方法在使用test4j功能之前被调用<br>
     * <br>
     * Initializes the singleton instance to the default value, loading the
     * configuration using the {@link ConfigurationLoader}
     */
    static boolean hasInitial = false;

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
     * Returns the single instance of {@link TestListener}. This instance
     * provides hook callback methods that enable intervening during the
     * execution of unit tests.
     * 
     * @return The single {@link TestListener}
     */
    public static TestListener getTestListener() {
        return getInstance().testListener;
    }

    /**
     * Implementation of {@link TestListener} that ensures that at every point
     * during the run of a test, every {@link Module} gets the chance of
     * performing some behavior, by calling the {@link TestListener} of each
     * module in turn. Also makes sure that the state of the instance of
     * {@link TestContext} returned by {@link CoreModule#getTestContext()} is
     * correctly set to the current test class, test object and test method.
     */
    private class CoreModuleListener extends TestListener {
        @SuppressWarnings("rawtypes")
        @Override
        public void beforeClass(Class testClazz) {
            MessageHelper.resetLog4jLevel();
            TestContext.setContext(testClazz);
            List<TestListener> listeners = ModulesManager.getTestListeners();
            for (TestListener listener : listeners) {
                try {
                    listener.beforeClass(testClazz);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        @Override
        public void beforeMethod(Object testObject, Method testMethod) {
            TestContext.setContext(testObject, testMethod);

            List<TestListener> listeners = ModulesManager.getTestListeners();
            for (TestListener listener : listeners) {
                try {
                    listener.beforeMethod(testObject, testMethod);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        @Override
        public void beforeRunning(Object testObject, Method testMethod) {
            TestContext.setContext(testObject, testMethod);

            List<TestListener> listeners = ModulesManager.getTestListeners();
            for (TestListener listener : listeners) {
                try {
                    listener.beforeRunning(testObject, testMethod);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            // TracerManager.startTracer();
        }

        @Override
        public void afterRunned(Object testObject, Method testMethod, Throwable throwable) {
            // TracerManager.endTracer();

            TestContext.setContext(testObject, testMethod);
            List<TestListener> listeners = ModulesManager.getTestListeners_Reverse();
            for (TestListener listener : listeners) {
                try {
                    listener.afterRunned(testObject, testMethod, throwable);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        @Override
        public void afterMethod(Object testObject, Method testMethod) {
            TestContext.setContext(testObject, testMethod);

            List<TestListener> listeners = ModulesManager.getTestListeners_Reverse();
            for (TestListener listener : listeners) {
                try {
                    listener.afterMethod(testObject, testMethod);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        @Override
        public void afterClass(Object testObject) {
            List<TestListener> listeners = ModulesManager.getTestListeners_Reverse();
            for (TestListener listener : listeners) {
                try {
                    listener.afterClass(testObject);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            TestContext.setContext(new ClazzAfterObject(testObject.getClass()), null);
        }

        @Override
        protected String getName() {
            return "CoreModuleListener";
        }
    }
}
