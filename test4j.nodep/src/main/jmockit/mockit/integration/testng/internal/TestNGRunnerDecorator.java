/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng.internal;

import java.lang.reflect.*;

import static mockit.internal.util.StackTrace.*;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.internal.Parameters;

import mockit.*;
import mockit.integration.internal.*;
import mockit.internal.state.*;

/**
 * Provides callbacks to be called by the TestNG 5.14+ test runner for each test execution.
 * JMockit will then assert any expectations set during the test, including those specified through {@link mockit.Mock}
 * and those recorded in {@link mockit.Expectations} subclasses.
 * <p/>
 * This class is not supposed to be accessed from user code. It will be automatically loaded at startup.
 */
public final class TestNGRunnerDecorator extends TestRunnerDecorator
   implements IInvokedMethodListener, IExecutionListener
{
   public static final class MockParameters extends MockUp<Parameters>
   {
      @Mock
      public static void checkParameterTypes(
         String methodName, Class<?>[] parameterTypes, String methodAnnotation, String[] parameterNames) {}

      @Mock
      public static Object getInjectedParameter(
         Invocation invocation, Class<?> c, Method method, ITestContext context, ITestResult testResult)
      {
         Object value = invocation.proceed();

         if (value != null) {
            return value;
         }

         if (method == null) {
            // Test execution didn't reach a test method yet.
            return null;
         }

         if (method.getParameterTypes().length == 0) {
            // A test method was reached, but it has no parameters.
            return null;
         }

         if (isMethodWithParametersProvidedByTestNG(method)) {
            // The test method has parameters, but they are to be provided by TestNG, not JMockit.
            return null;
         }

         // It's a mock parameter in a test method, to be provided by JMockit.
         return "";
      }
   }

   private static boolean isMethodWithParametersProvidedByTestNG(Method method)
   {
      if (method.isAnnotationPresent(org.testng.annotations.Parameters.class)) {
         return true;
      }

      Test testMetadata = method.getAnnotation(Test.class);

      return testMetadata != null && testMetadata.dataProvider().length() > 0;
   }

   private final ThreadLocal<SavePoint> savePoint;
   private boolean shouldPrepareForNextTest;

   public static void registerWithTestNG(TestNG testNG)
   {
      Object runnerDecorator = new TestNGRunnerDecorator();
      testNG.addListener(runnerDecorator);
   }

   public TestNGRunnerDecorator()
   {
      savePoint = new ThreadLocal<SavePoint>();
      new MockParameters();
      shouldPrepareForNextTest = true;
   }

   public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult testResult)
   {
      if (!invokedMethod.isTestMethod()) {
         beforeConfigurationMethod(testResult);
         return;
      }

      TestRun.enterNoMockingZone();
      Object testInstance = testResult.getInstance();
      SavePoint testMethodSavePoint;

      try {
         Class<?> testClass = testResult.getTestClass().getRealClass();
         updateTestClassState(testInstance, testClass);

         testMethodSavePoint = new SavePoint();
         savePoint.set(testMethodSavePoint);

         if (shouldPrepareForNextTest) {
            TestRun.prepareForNextTest();
            shouldPrepareForNextTest = false;
         }

         Method method = testResult.getMethod().getConstructorOrMethod().getMethod();

         if (!isMethodWithParametersProvidedByTestNG(method)) {
            Object[] parameters = testResult.getParameters();
            Object[] mockParameters =
               createInstancesForMockParameters(testInstance, method, parameters, testMethodSavePoint);

            if (mockParameters != null) {
               System.arraycopy(mockParameters, 0, parameters, 0, parameters.length);
            }
         }

         createInstancesForTestedFields(testInstance);
      }
      finally {
         TestRun.exitNoMockingZone();
      }

      TestRun.setRunningIndividualTest(testInstance);
      TestRun.setSavePointForTestMethod(testMethodSavePoint);
   }

   private void beforeConfigurationMethod(ITestResult testResult)
   {
      TestRun.enterNoMockingZone();

      try {
         Class<?> testClass = testResult.getTestClass().getRealClass();
         updateTestClassState(null, testClass);

         ITestNGMethod method = testResult.getMethod();

         if (method.isBeforeMethodConfiguration()) {
            if (shouldPrepareForNextTest) {
               discardTestLevelMockedTypes();
            }

            Object testInstance = method.getInstance();
            updateTestClassState(testInstance, testClass);

            if (shouldPrepareForNextTest) {
               prepareForNextTest();
               shouldPrepareForNextTest = false;
            }

            TestRun.setRunningIndividualTest(testInstance);
            TestRun.setSavePointForTestMethod(null);
         }
         else if (!method.isAfterMethodConfiguration()) {
            TestRun.getExecutingTest().setRecordAndReplay(null);
            cleanUpMocksFromPreviousTestClass();
            TestRun.setRunningIndividualTest(null);
            TestRun.setSavePointForTestMethod(null);
            TestRun.setCurrentTestClass(null);
         }
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }

   public void afterInvocation(IInvokedMethod invokedMethod, ITestResult testResult)
   {
      if (!invokedMethod.isTestMethod()) {
         afterConfigurationMethod(testResult);
         return;
      }

      TestRun.enterNoMockingZone();
      shouldPrepareForNextTest = true;
      SavePoint testMethodSavePoint = savePoint.get();
      savePoint.set(null);

      Throwable thrownByTest = testResult.getThrowable();

      try {
         if (thrownByTest == null) {
            concludeTestExecutionWithNothingThrown(testMethodSavePoint, testResult);
         }
         else if (thrownByTest instanceof TestException) {
            concludeTestExecutionWithExpectedExceptionNotThrown(invokedMethod, testMethodSavePoint, testResult);
         }
         else if (testResult.isSuccess()) {
            concludeTestExecutionWithExpectedExceptionThrown(testMethodSavePoint, testResult, thrownByTest);
         }
         else {
            concludeTestExecutionWithUnexpectedExceptionThrown(testMethodSavePoint, thrownByTest);
         }
      }
      finally {
         TestRun.finishCurrentTestExecution(false);
         TestRun.exitNoMockingZone();
      }
   }

   private void afterConfigurationMethod(ITestResult testResult)
   {
      TestRun.enterNoMockingZone();

      try {
         ITestNGMethod method = testResult.getMethod();

         if (method.isAfterMethodConfiguration()) {
            Throwable thrownAfterTest = testResult.getThrowable();

            if (thrownAfterTest != null) {
               filterStackTrace(thrownAfterTest);
            }
         }
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }

   private void concludeTestExecutionWithNothingThrown(SavePoint testMethodSavePoint, ITestResult testResult)
   {
      try {
         concludeTestMethodExecution(testMethodSavePoint, null, false);
      }
      catch (Throwable t) {
         filterStackTrace(t);
         testResult.setThrowable(t);
         testResult.setStatus(ITestResult.FAILURE);
      }
   }

   private void concludeTestExecutionWithExpectedExceptionNotThrown(
      IInvokedMethod invokedMethod, SavePoint testMethodSavePoint, ITestResult testResult)
   {
      try {
         concludeTestMethodExecution(testMethodSavePoint, null, false);
      }
      catch (Throwable t) {
         filterStackTrace(t);

         if (isExpectedException(invokedMethod, t)) {
            testResult.setThrowable(null);
            testResult.setStatus(ITestResult.SUCCESS);
         }
         else {
            filterStackTrace(testResult.getThrowable());
         }
      }
   }

   private void concludeTestExecutionWithExpectedExceptionThrown(
      SavePoint testMethodSavePoint, ITestResult testResult, Throwable thrownByTest)
   {
      filterStackTrace(thrownByTest);

      try {
         concludeTestMethodExecution(testMethodSavePoint, thrownByTest, true);
      }
      catch (Throwable t) {
         if (t != thrownByTest) {
            filterStackTrace(t);
            testResult.setThrowable(t);
            testResult.setStatus(ITestResult.FAILURE);
         }
      }
   }

   private void concludeTestExecutionWithUnexpectedExceptionThrown(
      SavePoint testMethodSavePoint, Throwable thrownByTest)
   {
      filterStackTrace(thrownByTest);

      try {
         concludeTestMethodExecution(testMethodSavePoint, thrownByTest, false);
      }
      catch (Throwable ignored) {}
   }

   private boolean isExpectedException(IInvokedMethod invokedMethod, Throwable thrownByTest)
   {
      Method testMethod = invokedMethod.getTestMethod().getConstructorOrMethod().getMethod();
      Class<?>[] expectedExceptions = testMethod.getAnnotation(Test.class).expectedExceptions();
      Class<? extends Throwable> thrownExceptionType = thrownByTest.getClass();

      for (Class<?> expectedException : expectedExceptions) {
         if (expectedException.isAssignableFrom(thrownExceptionType)) {
            return true;
         }
      }

      return false;
   }

   public void onExecutionStart() {}

   public void onExecutionFinish()
   {
      TestRun.enterNoMockingZone();

      try {
         TestRunnerDecorator.cleanUpMocksFromPreviousTestClass();
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }
}
