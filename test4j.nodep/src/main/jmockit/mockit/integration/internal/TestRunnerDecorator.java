/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.internal;

import java.lang.reflect.*;

import mockit.internal.*;
import mockit.internal.expectations.*;
import mockit.internal.expectations.injection.*;
import mockit.internal.expectations.mocking.*;
import mockit.internal.state.*;
import mockit.*;
import mockit.internal.util.*;

/**
 * Base class for "test runner decorators", which provide integration between JMockit and specific
 * test runners from JUnit and TestNG.
 */
public class TestRunnerDecorator
{
   private static SavePoint savePointForTest;

   protected final void updateTestClassState(Object target, Class<?> testClass)
   {
      try {
         handleSwitchToNewTestClassIfApplicable(testClass);

         if (target != null) {
            handleMockFieldsForWholeTestClass(target);
         }
      }
      catch (Error e) {
         try {
            SavePoint.rollbackForTestClass();
         }
         catch (Error err) {
            StackTrace.filterStackTrace(err);
            throw err;
         }

         throw e;
      }
      catch (RuntimeException e) {
         SavePoint.rollbackForTestClass();
         StackTrace.filterStackTrace(e);
         throw e;
      }
   }

   private void handleSwitchToNewTestClassIfApplicable(Class<?> testClass)
   {
      Class<?> currentTestClass = TestRun.getCurrentTestClass();

      if (testClass != currentTestClass) {
         if (currentTestClass == null) {
            SavePoint.registerNewActiveSavePoint();
         }
         else if (!currentTestClass.isAssignableFrom(testClass)) {
            cleanUpMocksFromPreviousTestClass();
            SavePoint.registerNewActiveSavePoint();
         }

         setUpClassLevelMocksAndStubs(testClass);
         TestRun.setCurrentTestClass(testClass);
      }
   }

   public static void cleanUpMocksFromPreviousTestClass()
   {
      discardTestLevelMockedTypes();
      SavePoint.rollbackForTestClass();

      SharedFieldTypeRedefinitions redefinitions = TestRun.getSharedFieldTypeRedefinitions();

      if (redefinitions != null) {
         redefinitions.cleanUp();
         TestRun.setSharedFieldTypeRedefinitions(null);
      }
   }

   protected final void setUpClassLevelMocksAndStubs(Class<?> testClass)
   {
      UsingMocksAndStubs mocksAndStubs = testClass.getAnnotation(UsingMocksAndStubs.class);

      if (mocksAndStubs != null) {
         Mockit.setUpMocksAndStubs(mocksAndStubs.value());
      }
   }

   protected final void prepareForNextTest()
   {
      discardTestLevelMockedTypes();
      savePointForTest = new SavePoint();
      TestRun.prepareForNextTest();
   }

   protected static void discardTestLevelMockedTypes()
   {
      if (savePointForTest != null) {
         savePointForTest.rollback();
         savePointForTest = null;
      }
   }

   private void handleMockFieldsForWholeTestClass(Object target)
   {
      SharedFieldTypeRedefinitions sharedRedefinitions = TestRun.getSharedFieldTypeRedefinitions();

      if (sharedRedefinitions == null) {
         sharedRedefinitions = new SharedFieldTypeRedefinitions(target);
         sharedRedefinitions.redefineTypesForTestClass();
         TestRun.setSharedFieldTypeRedefinitions(sharedRedefinitions);
      }

      if (target != TestRun.getCurrentTestInstance()) {
         sharedRedefinitions.assignNewInstancesToMockFields(target);
      }
   }

   protected final void createInstancesForTestedFields(Object target)
   {
      SharedFieldTypeRedefinitions sharedRedefinitions = TestRun.getSharedFieldTypeRedefinitions();

      if (sharedRedefinitions != null) {
         TestedClassInstantiations testedClasses = sharedRedefinitions.getTestedClassInstantiations();

         if (testedClasses != null) {
            TestRun.enterNoMockingZone();

            try {
               testedClasses.assignNewInstancesToTestedFields(target);
            }
            finally {
               TestRun.exitNoMockingZone();
            }
         }
      }
   }

   protected final Object[] createInstancesForMockParameters(
      Object target, Method testMethod, Object[] parameterValues, SavePoint savePoint)
   {
      if (testMethod.getParameterTypes().length == 0) {
         return null;
      }

      TestRun.enterNoMockingZone();

      try {
         ParameterTypeRedefinitions redefinitions = new ParameterTypeRedefinitions(target, testMethod, parameterValues);
         TestRun.getExecutingTest().setParameterTypeRedefinitions(redefinitions);
         savePoint.addRollbackAction(redefinitions.getCaptureOfNewInstances());

         return redefinitions.getParameterValues();
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }

   protected final void concludeTestMethodExecution(
      SavePoint savePoint, Throwable thrownByTest, boolean thrownAsExpected)
      throws Throwable
   {
      TestRun.enterNoMockingZone();
      Error expectationsFailure = RecordAndReplayExecution.endCurrentReplayIfAny();

      try {
         if (expectationsFailure == null && (thrownByTest == null || thrownAsExpected)) {
            TestRun.verifyExpectationsOnAnnotatedMocks();
         }
      }
      finally {
         TestRun.resetExpectationsOnAnnotatedMocks();
         savePoint.rollback();
         TestRun.exitNoMockingZone();
      }

      if (thrownByTest != null) {
         if (expectationsFailure == null || !thrownAsExpected || isUnexpectedOrMissingInvocation(thrownByTest)) {
            throw thrownByTest;
         }

         Throwable expectationsFailureCause = expectationsFailure.getCause();

         if (expectationsFailureCause != null) {
            expectationsFailureCause.initCause(thrownByTest);
         }
      }

      if (expectationsFailure != null) {
         throw expectationsFailure;
      }
   }

   private boolean isUnexpectedOrMissingInvocation(Throwable error)
   {
      Class<?> errorType = error.getClass();
      return errorType == UnexpectedInvocation.class || errorType == MissingInvocation.class;
   }
}
