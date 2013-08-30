/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;

import mockit.internal.*;
import mockit.internal.expectations.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class MockedBridge extends MockingBridge
{
   @SuppressWarnings("UnusedDeclaration")
   public static final InvocationHandler MB = new MockedBridge();

   public static void preventEventualClassLoadingConflicts()
   {
      // Pre-load certain JMockit classes to avoid NoClassDefFoundError's or re-entrancy loops during class loading
      // when certain JRE classes are mocked, such as ArrayList or Thread.
      try {
         Class.forName("mockit.Capturing");
         Class.forName("mockit.Delegate");
         Class.forName("mockit.internal.expectations.invocation.InvocationResults");
         Class.forName("mockit.internal.expectations.invocation.MockedTypeCascade");
         Class.forName("mockit.internal.expectations.mocking.BaseTypeRedefinition$MockedClass");
         Class.forName("mockit.internal.expectations.mocking.SharedFieldTypeRedefinitions");
         Class.forName("mockit.internal.expectations.mocking.TestedClasses");
         Class.forName("mockit.internal.expectations.argumentMatching.EqualityMatcher");
      }
      catch (ClassNotFoundException ignore) {}

      wasCalledDuringClassLoading();
      DefaultValues.computeForReturnType("()J");
   }

   public Object invoke(Object mocked, Method method, Object[] args) throws Throwable
   {
      String mockedClassDesc = (String) args[1];

      if (notToBeMocked(mocked, mockedClassDesc)) {
         return Void.class;
      }

      String mockName = (String) args[2];
      String mockDesc = (String) args[3];
      String mockNameAndDesc = mockName + mockDesc;
      Object[] mockArgs = extractMockArguments(args);
      int executionMode = (Integer) args[6];
      boolean lockHeldByCurrentThread = RecordAndReplayExecution.RECORD_OR_REPLAY_LOCK.isHeldByCurrentThread();

      if (lockHeldByCurrentThread && mocked != null && executionMode == 0) {
         Object rv = Utilities.evaluateObjectOverride(mocked, mockNameAndDesc, args);

         if (rv != null) {
            return rv;
         }
      }

      if (TestRun.isInsideNoMockingZone()) {
         return Void.class;
      }

      String genericSignature = (String) args[4];

      if (lockHeldByCurrentThread && executionMode == 0) {
         return
            RecordAndReplayExecution.defaultReturnValue(
               mocked, mockedClassDesc, mockNameAndDesc, genericSignature, 1, mockArgs);
      }

      TestRun.enterNoMockingZone();

      try {
         int mockAccess = (Integer) args[0];
         String exceptions = (String) args[5];

         return
            RecordAndReplayExecution.recordOrReplay(
               mocked, mockAccess, mockedClassDesc, mockNameAndDesc, genericSignature, exceptions,
               executionMode, mockArgs);
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }
}
