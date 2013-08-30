/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4.internal;

import java.lang.reflect.Method;
import java.util.List;

import mockit.Expectations;
import mockit.Instantiation;
import mockit.Mock;
import mockit.MockClass;
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

/**
 * Startup mock that modifies the JUnit 4.5+ test runner so that it calls back
 * to JMockit immediately after every test executes. When that happens, JMockit
 * will assert any expectations set during the test, including expectations
 * specified through {@link Mock} as well as in {@link Expectations} subclasses.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will
 * automatically load it at startup.
 */
@MockClass(realClass = FrameworkMethod.class, instantiation = Instantiation.PerMockedInstance)
public final class JUnit4TestRunnerDecorator extends TestRunnerDecorator {
	public FrameworkMethod it;
	private static volatile boolean shouldPrepareForNextTest = true;

	@Mock(reentrant = true)
	public Object invokeExplosively(Object target, Object... params) throws Throwable {
		Method method = it.getMethod();
		Class<?> testClass = target == null ? method.getDeclaringClass() : target.getClass();

		handleMockingOutsideTestMethods(target, testClass);

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
			executeTestMethod(target, params);
			return null; // it's a test method, therefore has void return type
		} catch (Throwable t) {
			StackTrace.filterStackTrace(t);
			throw t;
		} finally {
			/** modified by davey.wu **/
			// TestRun.finishCurrentTestExecution(true);
			TestRun.finishCurrentTestExecution(false);
			/** end modified by davey.wu **/
		}
	}

	private void handleMockingOutsideTestMethods(Object target, Class<?> testClass) {
		TestRun.enterNoMockingZone();

		try {
			if (target == null) {
				Class<?> currentTestClass = TestRun.getCurrentTestClass();

				if (currentTestClass != null && testClass.isAssignableFrom(currentTestClass)) {
					if (it.getAnnotation(AfterClass.class) != null) {
						cleanUpMocksFromPreviousTestClass();
					}
				} else if (it.getAnnotation(BeforeClass.class) != null) {
					updateTestClassState(null, testClass);
				}
			} else if (testClass.isAnnotationPresent(SuiteClasses.class)) {
				setUpClassLevelMocksAndStubs(testClass);
			} else {
				updateTestClassState(target, testClass);
			}
		} finally {
			TestRun.exitNoMockingZone();
		}
	}

	private void executeTestMethod(Object target, Object... parameters) throws Throwable {
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
			boolean isJTesterFrameworkMethod = frameworkMethodName.startsWith("org.jtester.junit.")
					&& frameworkMethodName.endsWith(".FrameworkMethodWithParameters");
			if (!isJTesterFrameworkMethod) {
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

	@Mock(reentrant = true)
	public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors) {
		if (!isStatic && it.getMethod().getParameterTypes().length > 0) {
			it.validatePublicVoid(false, errors);
			return;
		}

		it.validatePublicVoidNoArg(isStatic, errors);
	}
}
