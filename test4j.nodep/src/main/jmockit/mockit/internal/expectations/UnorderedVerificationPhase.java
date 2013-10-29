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
      if (!matchInstance && recordAndReplay.executionState.isToBeMatchedOnInstance(mock, mockNameAndDesc)) {
         matchInstance = true;
      }

      replayIndex = -1;

      for (int i = 0, n = expectationsInReplayOrder.size(); i < n; i++) {
         Expectation replayExpectation = expectationsInReplayOrder.get(i);
         Object[] replayArgs = invocationArgumentsInReplayOrder.get(i);

         if (matches(mock, mockClassDesc, mockNameAndDesc, args, replayExpectation, replayArgs)) {
            replayIndex = i;
            currentVerification.constraints.invocationCount++;
            currentExpectation = replayExpectation;
         }
      }

      if (replayIndex >= 0) {
         pendingError = verifyConstraints();
      }
   }

   private Error verifyConstraints()
   {
      ExpectedInvocation lastInvocation = expectationsInReplayOrder.get(replayIndex).invocation;
      Object[] lastArgs = invocationArgumentsInReplayOrder.get(replayIndex);
      int minInvocations = numberOfIterations > 0 ? numberOfIterations : 1;
      int maxInvocations = numberOfIterations > 0 ? numberOfIterations : -1;

      return currentVerification.verifyConstraints(lastInvocation, lastArgs, minInvocations, maxInvocations);
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
      ExpectedInvocation replayInvocation =
         replayIndex < 0 ? null : expectationsInReplayOrder.get(replayIndex).invocation;
      Object[] replayArgs = replayIndex < 0 ? null : invocationArgumentsInReplayOrder.get(replayIndex);
      pendingError =
         currentVerification.verifyConstraints(
            replayInvocation, replayArgs, multiplier * minInvocations, multiplier * maxInvocations);
   }

   @Override
   public void applyHandlerForEachInvocation(Object invocationHandler)
   {
      if (pendingError != null) {
         return;
      }

      validatePresenceOfExpectation(currentVerification);

      InvocationHandlerResult handler = new InvocationHandlerResult(invocationHandler);
      int matchedExpectations = 0;

      for (int i = 0, n = expectationsInReplayOrder.size(); i < n; i++) {
         Expectation expectation = expectationsInReplayOrder.get(i);
         Object[] args = invocationArgumentsInReplayOrder.get(i);

         if (evaluateInvocationHandlerIfExpectationMatchesCurrent(expectation, args, handler, matchedExpectations)) {
            matchedExpectations++;
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
