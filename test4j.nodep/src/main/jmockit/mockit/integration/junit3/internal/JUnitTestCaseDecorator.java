/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit3.internal;

import java.lang.reflect.*;

import junit.framework.*;

import mockit.integration.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

final class JUnitTestCaseDecorator extends TestRunnerDecorator
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

   void runBare(TestCase testCase) throws Throwable
   {
      updateTestClassState(testCase, testCase.getClass());

      prepareForNextTest();
      TestRun.setRunningIndividualTest(testCase);

      try {
         originalRunBare(testCase);
      }
      catch (Throwable t) {
         StackTrace.filterStackTrace(t);
         throw t;
      }
      finally {
         TestRun.setRunningIndividualTest(null);
      }
   }

   private void originalRunBare(TestCase testCase) throws Throwable
   {
      setUpMethod.invoke(testCase);

      Throwable exception = null;

      try {
         Method testMethod = findTestMethod(testCase);
         executeTestMethod(testCase, testMethod);
      }
      catch (Throwable running) {
         exception = running;
      }
      finally {
         TestRun.finishCurrentTestExecution(true);
         exception = performTearDown(testCase, exception);
      }

      if (exception != null) {
         throw exception;
      }
   }

   private Method findTestMethod(TestCase testCase) throws IllegalAccessException
   {
      String testMethodName = (String) fName.get(testCase);

      for (Method publicMethod : testCase.getClass().getMethods()) {
         if (publicMethod.getName().equals(testMethodName)) {
            return publicMethod;
         }
      }

      return runTestMethod;
   }

   private void executeTestMethod(TestCase testCase, Method testMethod) throws Throwable
   {
      SavePoint savePoint = new SavePoint();
      TestRun.setSavePointForTestMethod(savePoint);

      Throwable testFailure = null;

      try {
         Object[] mockParameters = createInstancesForMockParameters(testCase, testMethod, null, savePoint);
         createInstancesForTestedFields(testCase);

         if (mockParameters == null) {
            runTestMethod.invoke(testCase);
         }
         else {
            testMethod.invoke(testCase, mockParameters);
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

   private Throwable performTearDown(TestCase testCase, Throwable thrownByTestMethod)
   {
      try {
         tearDownMethod.invoke(testCase);
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
