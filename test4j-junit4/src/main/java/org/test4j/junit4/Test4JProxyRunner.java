package org.test4j.junit4;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.test4j.module.spring.interal.SpringEnv;
import org.test4j.tools.reflector.FieldAccessor;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.test4j.tools.reflector.MethodAccessor.invoke;


public class Test4JProxyRunner extends BlockJUnit4ClassRunner {
    private ITest4Runner proxy;

    public Test4JProxyRunner(Class<?> testClass) throws Exception {
        super(testClass);
    }

    @Override
    protected TestClass createTestClass(Class<?> testClass) {
        this.createRunnerProxy(testClass);
        return this.proxy.createTestClass(testClass);
    }

    @Override
    public Object createTest() throws Exception {
        return this.proxy.createTest();
    }

    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return this.proxy.childrenInvoker(notifier);
    }

    @Override
    public Statement methodInvoker(FrameworkMethod method, Object test) {
        return this.proxy.methodInvoker(method, test);
    }

    @Override
    public List<FrameworkMethod> computeTestMethods() {
        return this.proxy.computeTestMethods();
    }

    @Override
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        this.proxy.validatePublicVoidNoArgMethods(annotation, isStatic, errors);
    }

    private void createRunnerProxy(Class<?> testClass) {
        try {
            SpringEnv.setSpringEnv(testClass);
            this.proxy = SpringEnv.isSpringEnv(testClass) ? new Test4JSpringRunner(testClass) : new Test4JNormalRunner(testClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        new MockUp<ParentRunner>() {
            private final Object childrenLock = new Object();

            @Mock
            public void filter(Invocation it, Filter filter) throws NoTestsRemainException {
                ParentRunner runner = it.getInvokedInstance();
                synchronized (childrenLock) {
                    List children = new ArrayList((Collection) invoke(runner, "getFilteredChildren"));
                    for (Iterator iter = children.iterator(); iter.hasNext(); ) {
                        Object each = iter.next();
                        if (this.shouldRun(runner, filter, each)) {
                            try {
                                filter.apply(each);
                            } catch (NoTestsRemainException e) {
                                iter.remove();
                            }
                        } else {
                            iter.remove();
                        }
                    }
                    Collection filteredChildren = Collections.unmodifiableCollection(children);
                    FieldAccessor.setValue(runner, "filteredChildren", filteredChildren);
                    if (filteredChildren.isEmpty()) {
                        throw new NoTestsRemainException();
                    }
                }
            }

            private boolean shouldRun(ParentRunner runner, Filter filter, Object each) {
                String desc = filter.describe();
                if (each instanceof FrameworkMethodWithParameters) {
                    int index = each.toString().indexOf("[");
                    String name = each.toString().substring(0, index);
                    return desc.contains(name + "(");
                } else {
                    return invoke(runner, "shouldRun", filter, each);
                }
            }
        };
        super.filter(filter);
    }
}