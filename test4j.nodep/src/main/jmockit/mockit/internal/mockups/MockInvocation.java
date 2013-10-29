/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import java.lang.reflect.*;
import java.util.concurrent.*;

import mockit.*;

/**
 * An invocation to a {@code @Mock} method.
 */
public final class MockInvocation extends Invocation implements Runnable, Callable<Method>
{
   private final MockState mockState;
   private boolean proceedIntoConstructor;

   public MockInvocation(Object invokedInstance, Object[] invokedArguments, MockState mockState)
   {
      super(
         invokedInstance, invokedArguments,
         mockState.getTimesInvoked(), mockState.getMinInvocations(), mockState.getMaxInvocations());
      this.mockState = mockState;
   }

   /**
    * To be called if and when the min/max number of invocations is set by user code.
    */
   public void run()
   {
      mockState.minExpectedInvocations = getMinInvocations();
      mockState.maxExpectedInvocations = getMaxInvocations();
   }

   /**
    * Returns the {@code Method} object corresponding to the mocked method, or {@code null} if it's a mocked
    * constructor.
    */
   public Method call()
   {
      if (mockState.mockMethod.isForConstructor()) {
         proceedIntoConstructor = true;
         return null;
      }

      return mockState.getRealMethod().method;
   }

   public boolean shouldProceedIntoConstructor() { return proceedIntoConstructor; }
}
