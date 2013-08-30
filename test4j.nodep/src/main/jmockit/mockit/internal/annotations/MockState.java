/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.annotations;

import java.lang.reflect.*;

import mockit.internal.*;
import mockit.internal.util.*;

final class MockState
{
   final AnnotatedMockMethods.MockMethod mockMethod;
   private RealMethod realMethod;
   private Method actualMockMethod;

   // Expectations on the number of invocations of the mock as specified by the @Mock annotation,
   // initialized with the default values as specified in @Mock annotation definition:
   int expectedInvocations = -1;
   int minExpectedInvocations;
   int maxExpectedInvocations = -1;

   // Current mock invocation state:
   private int invocationCount;
   private ThreadLocal<Boolean> onReentrantCall;

   // Helper field just for synchronization:
   private final Object invocationCountLock = new Object();

   MockState(AnnotatedMockMethods.MockMethod mockMethod) { this.mockMethod = mockMethod; }

   Class<?> getRealClass() { return mockMethod.getRealClass(); }

   boolean isReentrant() { return onReentrantCall != null; }

   void makeReentrant()
   {
      onReentrantCall = new ThreadLocal<Boolean>() {
         @Override
         protected Boolean initialValue() { return false; }
      };
   }

   boolean isWithExpectations()
   {
      return
         expectedInvocations >= 0 || minExpectedInvocations > 0 || maxExpectedInvocations >= 0 ||
         mockMethod.hasInvocationParameter;
   }

   void update()
   {
      synchronized (invocationCountLock) {
         invocationCount++;
      }

      if (onReentrantCall != null) {
         onReentrantCall.set(true);
      }
   }

   boolean isOnReentrantCall() { return onReentrantCall != null && onReentrantCall.get(); }
   void exitReentrantCall() { onReentrantCall.set(false); }

   void verifyExpectations()
   {
      int timesInvoked = getTimesInvoked();

      if (expectedInvocations >= 0 && timesInvoked != expectedInvocations) {
         String message = mockMethod.errorMessage("exactly", expectedInvocations, timesInvoked);
         throw timesInvoked < expectedInvocations ?
            new MissingInvocation(message) : new UnexpectedInvocation(message);
      }
      else if (timesInvoked < minExpectedInvocations) {
         throw new MissingInvocation(mockMethod.errorMessage("at least", minExpectedInvocations, timesInvoked));
      }
      else if (maxExpectedInvocations >= 0 && timesInvoked > maxExpectedInvocations) {
         throw new UnexpectedInvocation(mockMethod.errorMessage("at most", maxExpectedInvocations, timesInvoked));
      }
   }

   int getMinInvocations() { return expectedInvocations >= 0 ? expectedInvocations : minExpectedInvocations; }
   int getMaxInvocations() { return expectedInvocations >= 0 ? expectedInvocations : maxExpectedInvocations; }

   int getTimesInvoked()
   {
      synchronized (invocationCountLock) {
         return invocationCount;
      }
   }

   void reset()
   {
      synchronized (invocationCountLock) {
         invocationCount = 0;
      }
   }

   RealMethod getRealMethod()
   {
      if (realMethod == null) {
         realMethod = new RealMethod(getRealClass(), mockMethod.name, mockMethod.mockedMethodDesc);
      }

      return realMethod;
   }

   Method getMockMethod(Class<?> mockClass, Class<?>[] paramTypes)
   {
      if (actualMockMethod == null) {
         actualMockMethod = Utilities.findCompatibleMethod(mockClass, mockMethod.name, paramTypes);
      }

      return actualMockMethod;
   }

   @Override
   public boolean equals(Object other) { return mockMethod.equals(((MockState) other).mockMethod); }

   @Override
   public int hashCode() { return mockMethod.hashCode(); }
}
