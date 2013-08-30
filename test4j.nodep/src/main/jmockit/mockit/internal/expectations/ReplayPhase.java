/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import mockit.internal.expectations.invocation.*;
import mockit.internal.state.*;

final class ReplayPhase extends Phase
{
   // Fields for the handling of strict invocations:
   private int initialStrictExpectationIndexForCurrentBlock;
   int currentStrictExpectationIndex;
   private Expectation strictExpectation;

   // Fields for the handling of non-strict invocations:
   final List<Expectation> nonStrictInvocations;
   final List<Object[]> nonStrictInvocationArguments;

   ReplayPhase(RecordAndReplayExecution recordAndReplay)
   {
      super(recordAndReplay);
      nonStrictInvocations = new ArrayList<Expectation>();
      nonStrictInvocationArguments = new ArrayList<Object[]>();
      initialStrictExpectationIndexForCurrentBlock =
         Math.max(recordAndReplay.lastExpectationIndexInPreviousReplayPhase, 0);
      positionOnFirstStrictExpectation();
   }

   private void positionOnFirstStrictExpectation()
   {
      List<Expectation> expectations = getExpectations();

      if (expectations.isEmpty()) {
         currentStrictExpectationIndex = -1;
         strictExpectation = null ;
      }
      else {
         currentStrictExpectationIndex = initialStrictExpectationIndexForCurrentBlock;
         strictExpectation =
            currentStrictExpectationIndex < expectations.size() ?
               expectations.get(currentStrictExpectationIndex) : null;
      }
   }

   private List<Expectation> getExpectations() { return recordAndReplay.executionState.expectations; }

   @Override
   Object handleInvocation(
      Object mock, int mockAccess, String mockClsDesc, String mockDesc, String genericSignature, String exceptions,
      boolean withRealImpl, Object[] args) throws Throwable
   {
      Expectation nonStrictExpectation =
         recordAndReplay.executionState.findNonStrictExpectation(mock, mockClsDesc, mockDesc, args);

      if (nonStrictExpectation == null) {
         nonStrictExpectation = createExpectationIfNonStrictInvocation(
            mock, mockAccess, mockClsDesc, mockDesc, genericSignature, exceptions, args);
      }

      if (nonStrictExpectation != null) {
         nonStrictInvocations.add(nonStrictExpectation);
         nonStrictInvocationArguments.add(args);
         return updateConstraintsAndProduceResult(nonStrictExpectation, mock, withRealImpl, args);
      }

      return handleStrictInvocation(mock, mockClsDesc, mockDesc, withRealImpl, args);
   }

   private Expectation createExpectationIfNonStrictInvocation(
      Object mock, int mockAccess, String mockClassDesc, String mockNameAndDesc, String genericSignature,
      String exceptions, Object[] args)
   {
      Expectation expectation = null;

      if (!TestRun.getExecutingTest().isStrictInvocation(mock, mockClassDesc, mockNameAndDesc)) {
         ExpectedInvocation invocation =
            new ExpectedInvocation(
               mock, mockAccess, mockClassDesc, mockNameAndDesc, false, genericSignature, exceptions, args);
         expectation = new Expectation(null, invocation, true);
         recordAndReplay.executionState.addExpectation(expectation, true);
      }

      return expectation;
   }

   private Object updateConstraintsAndProduceResult(
      Expectation expectation, Object mock, boolean withRealImpl, Object[] args) throws Throwable
   {
      boolean executeRealImpl = withRealImpl && expectation.recordPhase == null;
      expectation.constraints.incrementInvocationCount();

      if (executeRealImpl) {
         expectation.executedRealImplementation = true;
         Object defaultResult = expectation.invocation.getDefaultResult();
         return defaultResult == null ? Void.class : defaultResult;
      }

      if (expectation.constraints.isInvocationCountMoreThanMaximumExpected()) {
         recordAndReplay.setErrorThrown(expectation.invocation.errorForUnexpectedInvocation(args));
         return null;
      }

      return expectation.produceResult(mock, args);
   }

   @SuppressWarnings("OverlyComplexMethod")
   private Object handleStrictInvocation(
      Object mock, String mockClassDesc, String mockNameAndDesc, boolean withRealImpl, Object[] replayArgs)
      throws Throwable
   {
      Map<Object, Object> instanceMap = getInstanceMap();

      while (true) {
         if (strictExpectation == null) {
            return handleUnexpectedInvocation(mock, mockClassDesc, mockNameAndDesc, withRealImpl, replayArgs);
         }

         ExpectedInvocation invocation = strictExpectation.invocation;

         if (invocation.isMatch(mock, mockClassDesc, mockNameAndDesc, instanceMap)) {
            if (mock != invocation.instance) {
               instanceMap.put(invocation.instance, mock);
            }

            Error error = invocation.arguments.assertMatch(replayArgs, instanceMap);

            if (error != null) {
               if (strictExpectation.constraints.isInvocationCountInExpectedRange()) {
                  moveToNextExpectation();
                  continue;
               }

               if (withRealImpl) {
                  return Void.class;
               }

               recordAndReplay.setErrorThrown(error);
               return null;
            }

            Expectation expectation = strictExpectation;

            if (expectation.constraints.incrementInvocationCount()) {
               moveToNextExpectation();
            }
            else if (expectation.constraints.isInvocationCountMoreThanMaximumExpected()) {
               recordAndReplay.setErrorThrown(invocation.errorForUnexpectedInvocation(replayArgs));
               return null;
            }

            return expectation.produceResult(mock, replayArgs);
         }
         else if (strictExpectation.constraints.isInvocationCountInExpectedRange()) {
            moveToNextExpectation();
         }
         else if (withRealImpl) {
            return Void.class;
         }
         else {
            recordAndReplay.setErrorThrown(
               invocation.errorForUnexpectedInvocation(mock, mockClassDesc, mockNameAndDesc, replayArgs));
            return null;
         }
      }
   }

   private Object handleUnexpectedInvocation(
      Object mock, String mockClassDesc, String mockNameAndDesc, boolean withRealImpl, Object[] replayArgs)
   {
      if (withRealImpl) {
         return Void.class;
      }

      recordAndReplay.setErrorThrown(
         new ExpectedInvocation(mock, mockClassDesc, mockNameAndDesc, replayArgs).errorForUnexpectedInvocation());

      return null;
   }

   private void moveToNextExpectation()
   {
      List<Expectation> expectations = getExpectations();
      RecordPhase expectationBlock = strictExpectation.recordPhase;
      currentStrictExpectationIndex++;

      strictExpectation =
         currentStrictExpectationIndex < expectations.size() ? expectations.get(currentStrictExpectationIndex) : null;

      if (expectationBlock.numberOfIterations <= 1) {
         if (strictExpectation != null && strictExpectation.recordPhase != expectationBlock) {
            initialStrictExpectationIndexForCurrentBlock = currentStrictExpectationIndex;
         }
      }
      else if (strictExpectation == null || strictExpectation.recordPhase != expectationBlock) {
         expectationBlock.numberOfIterations--;
         positionOnFirstStrictExpectation();
         resetInvocationCountsForStrictExpectations(expectationBlock);
      }
   }

   private void resetInvocationCountsForStrictExpectations(RecordPhase expectationBlock)
   {
      for (Expectation expectation : getExpectations()) {
         if (expectation.recordPhase == expectationBlock) {
            expectation.constraints.invocationCount = 0;
         }
      }
   }

   Error endExecution()
   {
      Expectation strict = strictExpectation;
      strictExpectation = null;

      if (strict != null && strict.constraints.isInvocationCountLessThanMinimumExpected()) {
         return strict.invocation.errorForMissingInvocation();
      }

      for (Expectation nonStrict : recordAndReplay.executionState.nonStrictExpectations) {
         InvocationConstraints constraints = nonStrict.constraints;

         if (constraints.isInvocationCountLessThanMinimumExpected()) {
            return constraints.errorForMissingExpectations(nonStrict.invocation);
         }
      }

      int nextStrictExpectationIndex = currentStrictExpectationIndex + 1;

      if (nextStrictExpectationIndex < getExpectations().size()) {
         Expectation nextStrictExpectation = getExpectations().get(nextStrictExpectationIndex);

         if (nextStrictExpectation.constraints.isInvocationCountLessThanMinimumExpected()) {
            return nextStrictExpectation.invocation.errorForMissingInvocation();
         }
      }

      return null;
   }
}
