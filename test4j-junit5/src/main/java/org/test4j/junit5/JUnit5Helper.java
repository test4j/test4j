package org.test4j.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.springframework.test.context.TestContextManager;
import org.springframework.util.Assert;

/**
 * @author darui.wu
 * @date 2019/11/5 4:16 下午
 */
public class JUnit5Helper {
    /**
     * {@link Namespace} in which {@code TestContextManagers} are stored,
     * keyed by test class.
     */
    private static final Namespace NAMESPACE = Namespace.create(Test4JExtension.class);

    public static Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(NAMESPACE);
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

    public static void beforeTestClass(ExtensionContext context) throws Exception {
        getTestContextManager(context).beforeTestClass();
    }

    public static void afterTestClass(ExtensionContext context) throws Exception {
        getTestContextManager(context).afterTestClass();
    }
}
