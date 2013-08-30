/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import mockit.internal.expectations.invocation.*;

final class UnorderedVerificationPhase extends BaseVerificationPhase
{
   final List<VerifiedExpectation> verifiedExpectations;
   private Expectation aggregate;

   UnorderedVerificationPhase(
      RecordAndReplayExecution recordAndReplay,
      List<Expectation> expectationsInReplayOrder, List<Object[]> invocationArgumentsInReplayOrder)
   {
      super(recordAndReplay, expectationsInReplayOrder, invocationArgumentsInReplayOrder);
      verifiedExpectations = new ArrayList<VerifiedExpectation>();
   }

   @Override
   protected void findNonStrictExpectation(Object mock, String mockClassDesc, String mockNameAndDesc, Object[] args)
   {
      aggregate = null;

      if (recordAndReplay.executionState.isToBeMatchedOnInstance(mock, mockNameAndDesc)) {
         matchInstance = true;
      }

      for (Expectation expectation : recordAndReplay.executionState.nonStrictExpectations) {
         if (matches(mock, mockClassDesc, mockNameAndDesc, args, expectation)) {
            if (matchInstance && argMatchers == null) {
               currentExpectation = expectation;
               break;
            }

            aggregateMatchingExpectations(expectation);
         }
      }

      if (currentExpectation != null) {
         int minInvocations = 1;
         int maxInvocations = -1;

         if (numberOfIterations > 0) {
            minInvocations = maxInvocations = numberOfIterations;
         }

         verifyConstraints(minInvocations, maxInvocations);
      }
   }

   private void aggregateMatchingExpectations(Expectation found)
   {
      if (currentExpectation == null) {
         currentExpectation = found;
         return;
      }

      if (aggregate == null) {
         aggregate = new Expectation(currentExpectation);
         currentExpectation = aggregate;
      }

      aggregate.constraints.addInvocationCount(found.constraints);
   }

   private void verifyConstraints(int minInvocations, int maxInvocations)
   {
      pendingError = currentExpectation.verifyConstraints(currentVerification, minInvocations, maxInvocations);
   }

   @Override
   void addVerifiedExpectation(VerifiedExpectation verifiedExpectation)
   {
      super.addVerifiedExpectation(verifiedExpectation);
      verifiedExpectations.add(verifiedExpectation);
   }

   @Override
   public void handleInvocationCountConstraint(int minInvocations, int maxInvocations)
   {
      validatePresenceOfExpectation(currentVerification);

      int multiplier = numberOfIterations <= 1 ? 1 : numberOfIterations;
      verifyConstraints(multiplier * minInvocations, multiplier * maxInvocations);
   }

   @Override
   public void applyHandlerForEachInvocation(Object invocationHandler)
   {
      if (pendingError != null) {
         return;
      }

      validatePresenceOfExpectation(currentVerification);

      InvocationHandler handler = new InvocationHandler(invocationHandler);
      int i = 0;

      for (int j = 0, n = expectationsInReplayOrder.size(); j < n; j++) {
         Expectation expectation = expectationsInReplayOrder.get(j);
         Object[] args = invocationArgumentsInReplayOrder.get(j);

         if (evaluateInvocationHandlerIfExpectationMatchesCurrent(expectation, args, handler, i)) {
            i++;
         }
      }
   }

   VerifiedExpectation firstExpectationVerified()
   {
      VerifiedExpectation first = null;

      for (VerifiedExpectation expectation : verifiedExpectations) {
         if (first == null || expectation.replayIndex < first.replayIndex) {
            first = expectation;
         }
      }

      return first;
   }
}
