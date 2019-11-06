package org.test4j.junit4;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.test4j.module.core.CoreModule;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.spring.interal.SpringModuleHelper;

import java.lang.annotation.Annotation;
import java.util.List;

public class Test4JSpringRunner extends SpringJUnit4ClassRunner implements ITest4Runner {
    static {
        CoreModule.initSingletonInstance();
    }


    public Test4JSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public TestClass createTestClass(Class<?> testClass) {
        Test4JContext.setContext(testClass);
        return new TestClass(testClass);
    }

    @Override
    public Object createTest() throws Exception {
        Object testInstance = getTestClass().getOnlyConstructor().newInstance();
        Test4JContext.setContext(testInstance, null);
        TestContextManager contextManager = this.getTestContextManager();
        SpringModuleHelper.doSpringInitial(testInstance, contextManager);
        return testInstance;
    }

    @Override
    public Statement childrenInvoker(RunNotifier notifier) {
        return JUnit4Helper.childrenInvoker(() -> super.childrenInvoker(notifier));
    }

    @Override
    public Statement methodInvoker(FrameworkMethod method, Object testedObject) {
        return JUnit4Helper.methodInvoker(method, () -> super.methodInvoker(method, testedObject));
    }

    @Override
    public List<FrameworkMethod> computeTestMethods() {
        return JUnit4Helper.computeTestMethods(() -> super.computeTestMethods());
    }

    @Override
    public void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        this.validateMethod(annotation, isStatic, errors);
    }
}
