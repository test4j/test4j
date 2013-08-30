/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit3.internal;

import java.lang.reflect.*;

import junit.framework.*;

import mockit.*;
import mockit.integration.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * Provides an startup mock that modifies the JUnit 3.8 test runner so that it calls back to JMockit for each test
 * execution.
 * When that happens, JMockit will assert any expectations set during the test, including expectations specified through
 * {@link Mock} as well as in {@link Expectations} subclasses.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
@MockClass(realClass = TestCase.class, instantiation = Instantiation.PerMockedInstance)
public final class JUnitTestCaseDecorator extends TestRunnerDecorator
{
   private static final Method setUpMethod;
   private static final Method tearDownMethod;
   private static final Method runTestMethod;
   private static final Field fName;

   static
   {
      try {
         setUpMethod = TestCase.class.getDeclaredMethod("setUp");
         tearDownMethod = TestCase.class.getDeclaredMethod("tearDown");
         runTestMethod = TestCase.class.getDeclaredMethod("runTest");
         fName = TestCase.class.getDeclaredField("fName");
      }
      catch (NoSuchMethodException e) {
         // OK, won't happen.
         throw new RuntimeException(e);
      }
      catch (NoSuchFieldException e) {
         // OK, won't happen.
         throw new RuntimeException(e);
      }

      setUpMethod.setAccessible(true);
      tearDownMethod.setAccessible(true);
      runTestMethod.setAccessible(true);
      fName.setAccessible(true);
   }

   public TestCase it;

   @Mock
   public void runBare() throws Throwable
   {
      updateTestClassState(it, it.getClass());

      prepareForNextTest();
      TestRun.setRunningIndividualTest(it);

      try {
         originalRunBare();
      }
      catch (Throwable t) {
         StackTrace.filterStackTrace(t);
         throw t;
      }
      finally {
         TestRun.setRunningIndividualTest(null);
      }
   }

   private void originalRunBare() throws Throwable
   {
      setUpMethod.invoke(it);

      Throwable exception = null;

      try {
         Method testMethod = findTestMethod();
         executeTestMethod(testMethod);
      }
      catch (Throwable running) {
         exception = running;
      }
      finally {
         TestRun.finishCurrentTestExecution(true);
         exception = performTearDown(exception);
      }

      if (exception != null) {
         throw exception;
      }
   }

   private Method findTestMethod() throws IllegalAccessException
   {
      String testMethodName = (String) fName.get(it);

      for (Method publicMethod : it.getClass().getMethods()) {
         if (publicMethod.getName().equals(testMethodName)) {
            return publicMethod;
         }
      }

      return runTestMethod;
   }

   private void executeTestMethod(Method testMethod) throws Throwable
   {
      SavePoint savePoint = new SavePoint();
      TestRun.setSavePointForTestMethod(savePoint);

      Throwable testFailure = null;

      try {
         Object[] mockParameters = createInstancesForMockParameters(it, testMethod, null, savePoint);
         createInstancesForTestedFields(it);

         if (mockParameters == null) {
            runTestMethod.invoke(it);
         }
         else {
            testMethod.invoke(it, mockParameters);
         }
      }
      catch (InvocationTargetException e) {
         e.fillInStackTrace();
         testFailure = e.getTargetException();
      }
      catch (IllegalAccessException e) {
         e.fillInStackTrace();
         testFailure = e;
      }
      catch (Throwable thrownByTest) {
         testFailure = thrownByTest;
      }
      finally {
         concludeTestMethodExecution(savePoint, testFailure, false);
      }
   }

   private Throwable performTearDown(Throwable thrownByTestMethod)
   {
      try {
         tearDownMethod.invoke(it);
         return thrownByTestMethod;
      }
      catch (Throwable tearingDown) {
         return thrownByTestMethod == null ? tearingDown : thrownByTestMethod;
      }
      finally {
         TestRun.getExecutingTest().setRecordAndReplay(null);
      }
   }
}
