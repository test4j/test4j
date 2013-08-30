/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import mockit.internal.expectations.argumentMatching.*;

public abstract class TestOnlyPhase extends Phase
{
   protected int numberOfIterations;
   protected Object nextInstanceToMatch;
   protected boolean matchInstance;
   protected List<ArgumentMatcher> argMatchers;
   Expectation currentExpectation;

   TestOnlyPhase(RecordAndReplayExecution recordAndReplay)
   {
      super(recordAndReplay);
   }

   public final void setNumberOfIterations(int numberOfIterations)
   {
      this.numberOfIterations = numberOfIterations;
   }

   public final void setNextInstanceToMatch(Object nextInstanceToMatch)
   {
      this.nextInstanceToMatch = nextInstanceToMatch;
   }

   public final void addArgMatcher(ArgumentMatcher matcher)
   {
      createArgMatchersListIfNeeded();
      argMatchers.add(matcher);
   }

   private void createArgMatchersListIfNeeded()
   {
      if (argMatchers == null) {
         argMatchers = new ArrayList<ArgumentMatcher>();
      }
   }

   public final void moveArgMatcher(int originalMatcherIndex, int toIndex)
   {
      int i = 0;

      for (int matchersFound = 0; matchersFound <= originalMatcherIndex; i++) {
         if (argMatchers.get(i) != null) {
            matchersFound++;
         }
      }

      for (i--; i < toIndex; i++) {
         argMatchers.add(i, null);
      }
   }

   final Expectation getCurrentExpectation()
   {
      validatePresenceOfExpectation(currentExpectation);
      return currentExpectation;
   }

   final void validatePresenceOfExpectation(Expectation expectation)
   {
      if (expectation == null) {
         throw new IllegalStateException(
            "Missing invocation to mocked type at this point; please make sure such invocations appear only after " +
            "the declaration of a suitable mock field or parameter");
      }
   }

   public void setMaxInvocationCount(int maxInvocations)
   {
      int currentMinimum = getCurrentExpectation().constraints.minInvocations;

      if (numberOfIterations > 0) {
         currentMinimum /= numberOfIterations;
      }

      int minInvocations = maxInvocations < 0 ? currentMinimum : Math.min(currentMinimum, maxInvocations);

      handleInvocationCountConstraint(minInvocations, maxInvocations);
   }

   public abstract void handleInvocationCountConstraint(int minInvocations, int maxInvocations);

   public abstract void setCustomErrorMessage(CharSequence customMessage);

   public abstract void applyHandlerForEachInvocation(Object invocationHandler);
}
