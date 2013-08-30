/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import mockit.internal.expectations.invocation.*;
import mockit.internal.state.*;
import static mockit.internal.util.Utilities.*;

public final class RecordPhase extends TestOnlyPhase
{
   private final boolean nonStrict;

   RecordPhase(RecordAndReplayExecution recordAndReplay, boolean nonStrict)
   {
      super(recordAndReplay);
      this.nonStrict = nonStrict;
   }

   public void addResult(Object result)
   {
      getCurrentExpectation().addResult(result);
   }

   public void addReturnValueOrValues(Object value)
   {
      getCurrentExpectation().addReturnValueOrValues(value);
   }

   public void addSequenceOfReturnValues(Object firstValue, Object[] remainingValues)
   {
      getCurrentExpectation().addSequenceOfReturnValues(firstValue, remainingValues);
   }

   public void setNotStrict()
   {
      recordAndReplay.executionState.makeNonStrict(currentExpectation);
   }

   @Override
   Object handleInvocation(
      Object mock, int access, String classDesc, String mockNameAndDesc, String genericSignature, String exceptions,
      boolean withRealImpl, Object[] args) throws Throwable
   {
      //noinspection AssignmentToMethodParameter
      mock = configureMatchingOnMockInstanceIfSpecified(mock);
      ExpectedInvocation invocation =
         new ExpectedInvocation(mock, access, classDesc, mockNameAndDesc, matchInstance, genericSignature, args);
      ExecutingTest executingTest = TestRun.getExecutingTest();
      boolean nonStrictInvocation = nonStrict || executingTest.isNonStrictInvocation(mock, classDesc, mockNameAndDesc);

      if (!nonStrictInvocation) {
         String mockClassDesc = matchInstance ? null : classDesc;
         executingTest.addStrictMock(mock, mockClassDesc);
      }

      currentExpectation = new Expectation(this, invocation, nonStrictInvocation);

      if (argMatchers != null) {
         invocation.arguments.setMatchers(argMatchers);
         argMatchers = null;
      }

      recordAndReplay.executionState.addExpectation(currentExpectation, nonStrictInvocation);

      return invocation.getDefaultValueForReturnType(this);
   }

   private Object configureMatchingOnMockInstanceIfSpecified(Object mock)
   {
      matchInstance = false;

      if (mock == null || nextInstanceToMatch == null) {
         return mock;
      }

      Object specified = nextInstanceToMatch;

      if (mock != specified) {
         Class<?> mockedClass = getMockedClass(mock);

         if (!mockedClass.isInstance(specified)) {
            return mock;
         }
      }

      nextInstanceToMatch = null;
      matchInstance = true;
      return specified;
   }

   @Override
   public void handleInvocationCountConstraint(int minInvocations, int maxInvocations)
   {
      int lowerLimit = minInvocations;
      int upperLimit = maxInvocations;

      if (numberOfIterations > 1 && nonStrict) {
         lowerLimit *= numberOfIterations;
         upperLimit *= numberOfIterations;
      }

      getCurrentExpectation().constraints.setLimits(lowerLimit, upperLimit);
   }

   @Override
   public void setCustomErrorMessage(CharSequence customMessage)
   {
      getCurrentExpectation().setCustomErrorMessage(customMessage);
   }

   @Override
   public void applyHandlerForEachInvocation(Object invocationHandler)
   {
      getCurrentExpectation().setHandler(invocationHandler);
   }
}
