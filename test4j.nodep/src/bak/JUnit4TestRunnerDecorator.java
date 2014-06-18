/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4.internal;

import java.lang.reflect.Method;

import mockit.integration.internal.TestRunnerDecorator;
import mockit.internal.expectations.RecordAndReplayExecution;
import mockit.internal.state.SavePoint;
import mockit.internal.state.TestRun;
import mockit.internal.util.StackTrace;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.FrameworkMethod;

final class JUnit4TestRunnerDecorator extends TestRunnerDecorator {
    /**
     * A "volatile boolean" is as good as a
     * java.util.concurrent.atomic.AtomicBoolean here, since we only need the
     * basic get/set operations.
     */
    private volatile boolean shouldPrepareForNextTest = true;

    Object invokeExplosively(FrameworkMethod it, Object target, Object... params) throws Throwable {
        Method method = it.getMethod();
        Class<?> testClass = target == null ? method.getDeclaringClass() : target.getClass();

        handleMockingOutsideTestMethods(it, target, testClass);

        // In case it isn't a test method, but a before/after method:
        if (it.getAnnotation(Test.class) == null) {
            if (shouldPrepareForNextTest && it.getAnnotation(Before.class) != null) {
                prepareForNextTest();
                shouldPrepareForNextTest = false;
            }

            TestRun.setRunningIndividualTest(target);
            TestRun.setSavePointForTestMethod(null);

            try {
                return it.invokeExplosively(target, params);
            } catch (Throwable t) {
                RecordAndReplayExecution.endCurrentReplayIfAny();
                StackTrace.filterStackTrace(t);
                throw t;
            } finally {
                if (it.getAnnotation(After.class) != null) {
                    shouldPrepareForNextTest = true;
                }
            }
        }

        if (shouldPrepareForNextTest) {
            prepareForNextTest();
        }

        shouldPrepareForNextTest = true;

        try {
            executeTestMethod(it, target, params);
            return null; // it's a test method, therefore has void return type
        } catch (Throwable t) {
            StackTrace.filterStackTrace(t);
            throw t;
        } finally {
            /** modified by davey.wu **/
            TestRun.finishCurrentTestExecution(true);
            //TestRun.finishCurrentTestExecution(false);
            /** end modified by davey.wu **/
        }
    }

    private void handleMockingOutsideTestMethods(FrameworkMethod it, Object target, Class<?> testClass) {
        TestRun.enterNoMockingZone();

        try {
            if (target == null) {
                Class<?> currentTestClass = TestRun.getCurrentTestClass();

                if (currentTestClass != null && testClass.isAssignableFrom(currentTestClass)) {
                    if (it.getAnnotation(AfterClass.class) != null) {
                        cleanUpMocksFromPreviousTestClass();
                    }
                } else if (testClass.isAnnotationPresent(SuiteClasses.class)) {
                    setUpClassLevelMocksAndStubs(testClass);
                } else if (it.getAnnotation(BeforeClass.class) != null) {
                    updateTestClassState(null, testClass);
                }
            } else {
                updateTestClassState(target, testClass);
            }
        } finally {
            TestRun.exitNoMockingZone();
        }
    }

    private void executeTestMethod(FrameworkMethod it, Object target, Object... parameters) throws Throwable {
        SavePoint savePoint = new SavePoint();
        TestRun.setSavePointForTestMethod(savePoint);

        Method testMethod = it.getMethod();
        Throwable testFailure = null;
        boolean testFailureExpected = false;

        try {
            Object[] mockParameters = null;
            /** modified by davey.wu **/
            String frameworkMethodName = it.getClass().getName();
            /** 不判断整个类名，是尽量避免代码package重构带来的影响 **/
            boolean isTest4JFrameworkMethod = frameworkMethodName.startsWith("org.test4j.junit.")
                    && frameworkMethodName.endsWith(".FrameworkMethodWithParameters");
            if (!isTest4JFrameworkMethod) {
                mockParameters = createInstancesForMockParameters(target, testMethod, parameters, savePoint);
            }
            /** end modified by davey.wu **/
            createInstancesForTestedFields(target);

            TestRun.setRunningIndividualTest(target);
            it.invokeExplosively(target, mockParameters == null ? parameters : mockParameters);
        } catch (Throwable thrownByTest) {
            testFailure = thrownByTest;
            Class<? extends Throwable> expectedType = testMethod.getAnnotation(Test.class).expected();
            testFailureExpected = expectedType.isAssignableFrom(thrownByTest.getClass());
        } finally {
            concludeTestMethodExecution(savePoint, testFailure, testFailureExpected);
        }
    }
}
