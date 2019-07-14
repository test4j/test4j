package org.test4j.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.test4j.module.core.CoreModule;
import org.test4j.module.core.internal.Test4JTestContext;
import org.test4j.module.spring.SpringModule;
import org.test4j.tools.reflector.MethodAccessor;

import java.lang.annotation.Annotation;
import java.util.List;

public class Test4JSpringRunner extends SpringJUnit4ClassRunner implements ITest4Runner {
    static {
        CoreModule.initSingletonInstance();
    }

    private JUnitTestContext context;

    public Test4JSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public TestClass createTestClass(Class<?> testClass) {
        this.context = new JUnitTestContext(testClass);
        return new TestClass(testClass);
    }

    @Override
    public Object createTest() throws Exception {
        Object testInstance = getTestClass().getOnlyConstructor().newInstance();
        this.context.setTestedObject(testInstance);
        SpringModule.invokeSpringInitMethod(testInstance);
        TestContextManager contextManager = this.getTestContextManager();
        {
            Test4JTestContext.setSpringTestContextManager(contextManager);
            contextManager.prepareTestInstance(testInstance);
            ApplicationContext applicationContext = this.getApplicationContext(contextManager);
            SpringModule.setSpringContext(applicationContext);
        }
        return testInstance;
    }

    /**
     * 有些版本getTestContext禁止访问，所以这里反射调用
     *
     * @param contextManager
     * @return
     */
    private ApplicationContext getApplicationContext(TestContextManager contextManager) {
        try {
            TestContext testContext = MethodAccessor.invokeMethod(contextManager, "getTestContext");
            return testContext.getApplicationContext();
        } catch (Exception e) {
            throw new RuntimeException("get Spring Application Context error:" + e.getMessage(), e);
        }
    }

    @Override
    public Statement childrenInvoker(RunNotifier notifier) {
        return this.context.childrenInvoker(() -> super.childrenInvoker(notifier));
    }

    @Override
    public Statement methodInvoker(FrameworkMethod method, Object testedObject) {
        return this.context.methodInvoker(method, () -> super.methodInvoker(method, testedObject));
    }

    @Override
    public Statement methodBlock(FrameworkMethod method) {
        Statement statement = super.methodBlock(method);
        if (!isIgnored(method)) {
            this.context.runBeforeSetup(method);
        }
        return statement;
    }

    @Override
    public List<FrameworkMethod> computeTestMethods() {
        return this.context.computeTestMethods(() -> super.computeTestMethods());
    }

    @Override
    public void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        this.validateMethod(annotation, isStatic, errors);
    }
}
