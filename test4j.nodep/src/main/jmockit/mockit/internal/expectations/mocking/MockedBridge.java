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
   public static final MockingBridge MB = new MockedBridge();

   private MockedBridge() { super(MockedBridge.class); }

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
         Object rv = ObjectMethods.evaluateOverride(mocked, mockNameAndDesc, args);

         if (rv != null) {
            return rv;
         }
      }

      if (TestRun.isInsideNoMockingZone()) {
         return Void.class;
      }

      String genericSignature = (String) args[4];

      if (lockHeldByCurrentThread && executionMode == 0) {
         return RecordAndReplayExecution.defaultReturnValue(
            mocked, mockedClassDesc, mockNameAndDesc, genericSignature, 1, mockArgs);
      }

      int mockAccess = (Integer) args[0];
      String exceptions = (String) args[5];

      TestRun.enterNoMockingZone();

      try {
         return RecordAndReplayExecution.recordOrReplay(
            mocked, mockAccess, mockedClassDesc, mockNameAndDesc, genericSignature, exceptions,
            executionMode, mockArgs);
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }
}
