package org.test4j.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.test4j.module.core.CoreModule;

import java.lang.annotation.Annotation;
import java.util.List;

public class Test4JNormalRunner extends BlockJUnit4ClassRunner implements ITest4Runner {
    static {
        CoreModule.initSingletonInstance();
    }

    private JUnitTestContext context;

    public Test4JNormalRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public TestClass createTestClass(Class<?> testClass) {
        this.context = new JUnitTestContext(testClass);
        return new TestClass(testClass);
    }

    @Override
    public Object createTest() throws Exception {
        Object testedObject = super.createTest();
        this.context.setTestedObject(testedObject);
        return testedObject;
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
    protected Statement methodBlock(FrameworkMethod method) {
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
