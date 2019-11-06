package org.test4j.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.util.Assert;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.spring.SpringModule;

/**
 *
 * @author darui.wu
 * @create 2019/11/5 4:16 下午
 */
public class JUnit5Helper {
    /**
     * {@link ExtensionContext.Namespace} in which {@code TestContextManagers} are stored,
     * keyed by test class.
     */
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(Test4JExtension.class);


    public static Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(NAMESPACE);
    }

    public static void doSpringInitial(Object testInstance, ExtensionContext context) throws Exception {
        SpringModule.invokeSpringInitMethod(testInstance);
        TestContextManager testContextManager = getTestContextManager(context);
        Test4JContext.setSpringTestContextManager(testContextManager);
        testContextManager.prepareTestInstance(testInstance);
        ApplicationContext applicationContext = getApplicationContext(testContextManager);
        SpringModule.setSpringContext(applicationContext);
    }

    /**
     * Get the {@link TestContextManager} associated with the supplied {@code ExtensionContext}.
     *
     * @return the {@code TestContextManager} (never {@code null})
     */
    public static TestContextManager getTestContextManager(ExtensionContext context) {
        Assert.notNull(context, "ExtensionContext must not be null");
        Class<?> testClass = context.getRequiredTestClass();
        Store store = getStore(context);
        return store.getOrComputeIfAbsent(testClass, TestContextManager::new, TestContextManager.class);
    }

    /**
     * Get the {@link ApplicationContext} associated with the supplied {@code ExtensionContext}.
     *
     * @param context the current {@code ExtensionContext} (never {@code null})
     * @return the application context
     * @throws IllegalStateException if an error occurs while retrieving the application context
     * @see org.springframework.test.context.TestContext#getApplicationContext()
     */
    public static ApplicationContext getApplicationContext(ExtensionContext context) {
        return getTestContextManager(context).getTestContext().getApplicationContext();
    }

    /**
     * 有些版本getTestContext禁止访问，所以这里反射调用
     *
     * @param contextManager
     * @return
     */
    public static ApplicationContext getApplicationContext(TestContextManager contextManager) {
        return contextManager.getTestContext().getApplicationContext();
    }
}
