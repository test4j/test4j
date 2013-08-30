package org.jtester.junit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jtester.junit.annotations.DataFrom;
import org.jtester.junit.parametermethod.FrameworkMethodWithParameters;
import org.jtester.junit.parametermethod.ParameterDataFromHelper;
import org.jtester.junit.statement.MethodAroundStatement;
import org.jtester.junit.statement.TestAroundStatement;
import org.jtester.module.core.ClazzAroundObject.ClazzBeforeObject;
import org.jtester.module.core.CoreModule;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.TestListener;
import org.jtester.tools.commons.MethodHelper;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

@SuppressWarnings({ "rawtypes" })
public class JTesterRunner extends BlockJUnit4ClassRunner {
    static {
        CoreModule.initSingletonInstance();
    }

    private Object           testedObject = null;

    private final Class      testClazz;

    private RuntimeException error;

    /**
     * Creates a test runner that runs all test methods in the given class.
     * 
     * @param testClass the class, not null
     * @throws org.junit.runners.model.InitializationError
     * @throws InitializationError
     */
    public JTesterRunner(Class testClass) throws InitializationError {
        super(testClass);
        this.testClazz = testClass;
        TestContext.setContext(new ClazzBeforeObject(testClazz), null);
    }

    @Override
    protected Object createTest() throws Exception {
        if (this.testedObject == null) {
            this.testedObject = this.getTestedObject();
        }
        return this.testedObject;
    }

    private Object getTestedObject() throws Exception {
        this.error = null;
        try {
            Object tested = super.createTest();
            return tested;
        } catch (Error e) {
            this.error = new RuntimeException(e);
            throw e;
        } catch (Exception e) {
            this.error = new RuntimeException(e);
            throw e;
        }
    }

    private void initTestedObject() {
        try {
            this.testedObject = super.createTest();
            TestContext.setContext(this.testedObject, null);
        } catch (Throwable e) {
            this.error = new RuntimeException(e);
        }
    }

    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() {
                initTestedObject();
                JTesterRunner.this.getTestListener().beforeClass(JTesterRunner.this.testClazz);
                MethodHelper.invokeUnThrow(JTesterRunner.this, "runChildren", notifier);
                JTesterRunner.this.getTestListener().afterClass(JTesterRunner.this.testedObject);
            }
        };
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Statement statement = super.methodBlock(method);
        if (this.error != null) {
            throw error;
        }
        statement = new TestAroundStatement(statement, getTestListener(), testedObject, method.getMethod());
        return statement;
    }

    @Override
    public Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement statement = super.methodInvoker(method, test);
        statement = new MethodAroundStatement(statement, getTestListener(), test, method.getMethod());
        return statement;
    }

    @Override
    protected String testName(FrameworkMethod method) {
        if (method instanceof FrameworkMethodWithParameters) {
            return method.toString();
        } else {
            return super.testName(method);
        }
    }

    private List<FrameworkMethod> testMethods;

    /**
     * {@inheritDoc}<br>
     * 构造有参和无参的测试方法列表
     */
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        Class testclazz = getTestClass().getJavaClass();
        if (this.testMethods == null) {
            List<FrameworkMethod> initTestMethods = super.computeTestMethods();
            testMethods = new ArrayList<FrameworkMethod>();
            for (FrameworkMethod frameworkMethod : initTestMethods) {
                Method testMethod = frameworkMethod.getMethod();
                testMethod.getDeclaringClass();
                DataFrom dataFrom = testMethod.getAnnotation(DataFrom.class);
                if (dataFrom == null) {
                    testMethods.add(frameworkMethod);
                } else {
                    List<FrameworkMethodWithParameters> parameterizedTestMethods = ParameterDataFromHelper
                            .computeParameterizedTestMethods(testclazz, frameworkMethod.getMethod(), dataFrom);
                    testMethods.addAll(parameterizedTestMethods);
                }
            }
        }
        return this.testMethods;
    }

    /**
     * @return The test listener
     */
    protected TestListener getTestListener() {
        return CoreModule.getTestListener();
    }
}
