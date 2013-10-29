/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import mockit.internal.expectations.invocation.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

final class Expectation
{
   final RecordPhase recordPhase;
   final ExpectedInvocation invocation;
   final InvocationConstraints constraints;
   private InvocationHandlerResult handler;
   private InvocationResults results;
   boolean executedRealImplementation;

   Expectation(RecordPhase recordPhase, ExpectedInvocation invocation, boolean nonStrict)
   {
      this.recordPhase = recordPhase;
      this.invocation = invocation;
      constraints = new InvocationConstraints(nonStrict);
   }

   void setHandler(Object handler) { this.handler = new InvocationHandlerResult(handler); }

   InvocationResults getResults()
   {
      if (results == null) {
         results = new InvocationResults(invocation, constraints);
      }

      return results;
   }

   Object produceResult(Object invokedObject, Object[] invocationArgs) throws Throwable
   {
      if (handler != null) {
         handler.produceResult(invokedObject, invocation, constraints, invocationArgs);
      }

      if (results == null) {
         return invocation.getDefaultValueForReturnType(null);
      }

      return results.produceResult(invokedObject, invocationArgs);
   }

   void addReturnValueOrValues(Object value)
   {
      new ReturnTypeConversion(this, getReturnType(), value).addConvertedValueOrValues();
   }

   Class<?> getReturnType()
   {
      return TypeDescriptor.getReturnType(invocation.getSignatureWithResolvedReturnType());
   }

   void substituteCascadedMockToBeReturnedIfNeeded(Object valueToBeReturned)
   {
      if (valueToBeReturned != null) {
         Object cascadedMock = invocation.getCascadedMock();

         if (cascadedMock != null) {
            TestRun.getExecutingTest().discardCascadedMockWhenInjectable(cascadedMock);
            recordPhase.setNextInstanceToMatch(null);
         }
      }
   }

   void addSequenceOfReturnValues(Object firstValue, Object[] remainingValues)
   {
      InvocationResults sequence = getResults();

      if (remainingValues == null) {
         sequence.addReturnValue(firstValue);
      }
      else if (!new SequenceOfReturnValues(this, firstValue, remainingValues).addResultWithSequenceOfValues()) {
         sequence.addReturnValue(firstValue);
         sequence.addReturnValues(remainingValues);
      }
   }

   @SuppressWarnings("UnnecessaryFullyQualifiedName")
   void addResult(Object value)
   {
      if (value == null) {
         getResults().addReturnValueResult(value);
      }
      else if (isReplacementInstance(value)) {
         invocation.replacementInstance = value;
      }
      else if (value instanceof Throwable) {
         getResults().addThrowable((Throwable) value);
      }
      else if (value instanceof mockit.Delegate) {
         getResults().addDelegatedResult((mockit.Delegate<?>) value);
      }
      else {
         Class<?> rt = getReturnType();

         if (rt.isInstance(value)) {
            substituteCascadedMockToBeReturnedIfNeeded(value);
            getResults().addReturnValueResult(value);
         }
         else {
            new ReturnTypeConversion(this, rt, value).addConvertedValue();
         }
      }
   }

   private boolean isReplacementInstance(Object value)
   {
      return invocation.isConstructor() && value.getClass().isInstance(invocation.instance);
   }

   void setCustomErrorMessage(CharSequence message) { invocation.customErrorMessage = message; }

   Error verifyConstraints(
      ExpectedInvocation replayInvocation, Object[] replayArgs, int minInvocations, int maxInvocations)
   {
      Error error = constraints.verifyLowerLimit(invocation, minInvocations);

      if (error != null) {
         return error;
      }

      return constraints.verifyUpperLimit(replayInvocation, replayArgs, maxInvocations);
   }

   Object executeRealImplementation(Object replacementInstance, Object[] args) throws Throwable
   {
      return getResults().executeRealImplementation(replacementInstance, args);
   }
}
