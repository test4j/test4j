/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import mockit.internal.*;
import mockit.internal.expectations.argumentMatching.*;
import mockit.internal.expectations.invocation.*;
import mockit.internal.util.*;

public abstract class BaseVerificationPhase extends TestOnlyPhase
{
   final List<Expectation> expectationsInReplayOrder;
   final List<Object[]> invocationArgumentsInReplayOrder;
   private boolean allMockedInvocationsDuringReplayMustBeVerified;
   private Object[] mockedTypesAndInstancesToFullyVerify;
   protected Expectation currentVerification;
   protected int replayIndex;
   protected Error pendingError;

   protected BaseVerificationPhase(
      RecordAndReplayExecution recordAndReplay,
      List<Expectation> expectationsInReplayOrder, List<Object[]> invocationArgumentsInReplayOrder)
   {
      super(recordAndReplay);
      this.expectationsInReplayOrder = expectationsInReplayOrder;
      this.invocationArgumentsInReplayOrder = invocationArgumentsInReplayOrder;
   }

   public final void setAllInvocationsMustBeVerified() { allMockedInvocationsDuringReplayMustBeVerified = true; }

   public final void setMockedTypesToFullyVerify(Object[] mockedTypesAndInstancesToFullyVerify)
   {
      this.mockedTypesAndInstancesToFullyVerify = mockedTypesAndInstancesToFullyVerify;
   }

   @Override
   final Object handleInvocation(
      Object mock, int access, String mockClassDesc, String mockNameAndDesc, String genericSignature,
      String exceptions, boolean withRealImpl, Object[] args)
   {
      if (pendingError != null) {
         recordAndReplay.setErrorThrown(pendingError);
         pendingError = null;
         return null;
      }

      matchInstance = nextInstanceToMatch != null && mock == nextInstanceToMatch;

      ExpectedInvocation currentInvocation =
         new ExpectedInvocation(mock, access, mockClassDesc, mockNameAndDesc, matchInstance, genericSignature, args);
      currentInvocation.arguments.setMatchers(argMatchers);
      currentVerification = new Expectation(null, currentInvocation, true);

      currentExpectation = null;
      findNonStrictExpectation(mock, mockClassDesc, mockNameAndDesc, args);
      argMatchers = null;

      if (matchInstance) {
         nextInstanceToMatch = null;
      }

      if (recordAndReplay.getErrorThrown() != null) {
         return null;
      }

      if (currentExpectation == null) {
         pendingError = currentVerification.invocation.errorForMissingInvocation();
         currentExpectation = currentVerification;
      }

      return currentExpectation.invocation.getDefaultValueForReturnType(this);
   }

   abstract void findNonStrictExpectation(Object mock, String mockClassDesc, String mockNameAndDesc, Object[] args);

   final boolean matches(
      Object mock, String mockClassDesc, String mockNameAndDesc, Object[] args,
      Expectation replayExpectation, Object[] replayArgs)
   {
      ExpectedInvocation invocation = replayExpectation.invocation;
      Map<Object, Object> instanceMap = getInstanceMap();

      if (
         invocation.isMatch(mock, mockClassDesc, mockNameAndDesc, instanceMap) &&
         (!matchInstance || invocation.isEquivalentInstance(mock, instanceMap))
      ) {
         Object[] originalArgs = invocation.arguments.prepareForVerification(args, argMatchers);
         boolean argumentsMatch = invocation.arguments.isMatch(replayArgs, instanceMap);
         invocation.arguments.setValuesWithNoMatchers(originalArgs);

         if (argumentsMatch) {
            addVerifiedExpectation(replayExpectation, replayArgs, argMatchers);
            return true;
         }
      }

      return false;
   }

   private void addVerifiedExpectation(Expectation expectation, Object[] args, List<ArgumentMatcher> matchers)
   {
      int i = expectationsInReplayOrder.indexOf(expectation);
      addVerifiedExpectation(new VerifiedExpectation(expectation, args, matchers, i));
   }

   void addVerifiedExpectation(VerifiedExpectation verifiedExpectation)
   {
      recordAndReplay.executionState.verifiedExpectations.add(verifiedExpectation);
   }

   @Override
   public final void setMaxInvocationCount(int maxInvocations)
   {
      if (maxInvocations == 0 || pendingError == null) {
         super.setMaxInvocationCount(maxInvocations);
      }
   }

   @Override
   public final void setCustomErrorMessage(CharSequence customMessage)
   {
      Expectation expectation = getCurrentExpectation();

      if (pendingError == null) {
         expectation.setCustomErrorMessage(customMessage);
      }
      else if (customMessage != null) {
         String finalMessage = customMessage + "\n" + pendingError.getMessage();
         StackTraceElement[] previousStackTrace = pendingError.getStackTrace();
         pendingError = pendingError instanceof MissingInvocation ?
            new MissingInvocation(finalMessage) : new UnexpectedInvocation(finalMessage);
         pendingError.setStackTrace(previousStackTrace);
      }
   }

   final boolean evaluateInvocationHandlerIfExpectationMatchesCurrent(
      Expectation replayExpectation, Object[] replayArgs, InvocationHandlerResult handler, int matchedInvocations)
   {
      if (matchesCurrentVerification(replayExpectation, replayArgs)) {
         InvocationConstraints constraints = currentVerification.constraints;
         int originalInvocationCount = constraints.invocationCount;

         try {
            constraints.invocationCount = matchedInvocations + 1;
            handler.produceResult(
               replayExpectation.invocation.instance, replayExpectation.invocation, constraints, replayArgs);
         }
         finally {
            constraints.invocationCount = originalInvocationCount;
         }

         return true;
      }

      return false;
   }

   private boolean matchesCurrentVerification(Expectation replayExpectation, Object[] replayArgs)
   {
      ExpectedInvocation replayInvocation = replayExpectation.invocation;
      ExpectedInvocation verifiedInvocation = currentVerification.invocation;
      Map<Object, Object> instanceMap = getInstanceMap();

      if (
         replayInvocation.isMatch(
            verifiedInvocation.instance, verifiedInvocation.getClassDesc(),
            verifiedInvocation.getMethodNameAndDescription(), instanceMap) &&
         (!matchInstance || replayInvocation.isEquivalentInstance(verifiedInvocation.instance, instanceMap))
      ) {
         if (verifiedInvocation.arguments.isMatch(replayArgs, instanceMap)) {
            addVerifiedExpectation(replayExpectation, replayArgs, verifiedInvocation.arguments.getMatchers());
            return true;
         }
      }

      return false;
   }

   protected Error endVerification()
   {
      if (pendingError != null) {
         return pendingError;
      }

      if (allMockedInvocationsDuringReplayMustBeVerified) {
         return validateThatAllInvocationsWereVerified();
      }

      return null;
   }

   private Error validateThatAllInvocationsWereVerified()
   {
      List<Expectation> notVerified = new ArrayList<Expectation>();

      for (int i = 0; i < expectationsInReplayOrder.size(); i++) {
         Expectation replayExpectation = expectationsInReplayOrder.get(i);

         if (replayExpectation != null && isEligibleForFullVerification(replayExpectation)) {
            Object[] replayArgs = invocationArgumentsInReplayOrder.get(i);

            if (!wasVerified(replayExpectation, replayArgs)) {
               notVerified.add(replayExpectation);
            }
         }
      }

      if (!notVerified.isEmpty()) {
         if (mockedTypesAndInstancesToFullyVerify == null) {
            Expectation firstUnexpected = notVerified.get(0);
            return firstUnexpected.invocation.errorForUnexpectedInvocation();
         }

         return validateThatUnverifiedInvocationsAreAllowed(notVerified);
      }

      return null;
   }

   private boolean isEligibleForFullVerification(Expectation replayExpectation)
   {
      return !replayExpectation.executedRealImplementation && replayExpectation.constraints.minInvocations <= 0;
   }

   private boolean wasVerified(Expectation replayExpectation, Object[] replayArgs)
   {
      InvocationArguments invokedArgs = replayExpectation.invocation.arguments;
      List<VerifiedExpectation> expectationsVerified = recordAndReplay.executionState.verifiedExpectations;

      for (int j = 0; j < expectationsVerified.size(); j++) {
         VerifiedExpectation verified = expectationsVerified.get(j);

         if (verified.expectation == replayExpectation) {
            Object[] storedArgs = invokedArgs.prepareForVerification(verified.arguments, verified.argMatchers);
            boolean argumentsMatch = invokedArgs.isMatch(replayArgs, getInstanceMap());
            invokedArgs.setValuesWithNoMatchers(storedArgs);

            if (argumentsMatch) {
               if (shouldDiscardInformationAboutVerifiedInvocationOnceUsed()) {
                  expectationsVerified.remove(j);
               }

               return true;
            }
         }
      }

      invokedArgs.setValuesWithNoMatchers(replayArgs);
      return false;
   }

   boolean shouldDiscardInformationAboutVerifiedInvocationOnceUsed() { return false; }

   private Error validateThatUnverifiedInvocationsAreAllowed(List<Expectation> unverified)
   {
      for (Expectation expectation : unverified) {
         ExpectedInvocation invocation = expectation.invocation;

         if (isInvocationToBeVerified(invocation)) {
            return invocation.errorForUnexpectedInvocation();
         }
      }

      return null;
   }

   private boolean isInvocationToBeVerified(ExpectedInvocation unverifiedInvocation)
   {
      String invokedClassName = unverifiedInvocation.getClassName();
      Object invokedInstance = unverifiedInvocation.instance;

      for (Object mockedTypeOrInstance : mockedTypesAndInstancesToFullyVerify) {
         if (mockedTypeOrInstance instanceof Class) {
            Class<?> mockedType = (Class<?>) mockedTypeOrInstance;

            if (invokedClassName.equals(mockedType.getName())) {
               return true;
            }
         }
         else if (invokedInstance == null) {
            Class<?> invokedClass = ClassLoad.loadClass(invokedClassName);

            if (invokedClass.isInstance(mockedTypeOrInstance)) {
               return true;
            }
         }
         else if (unverifiedInvocation.matchInstance) {
            if (mockedTypeOrInstance == invokedInstance) {
               return true;
            }
         }
         else if (invokedInstance.getClass().isInstance(mockedTypeOrInstance)) {
            return true;
         }
      }

      return false;
   }

   public final Object getArgumentValueForCurrentVerification(int parameterIndex)
   {
      List<VerifiedExpectation> verifiedExpectations = recordAndReplay.executionState.verifiedExpectations;

      if (verifiedExpectations.isEmpty()) {
         return currentVerification.invocation.getArgumentValues()[parameterIndex];
      }

      VerifiedExpectation lastMatched = verifiedExpectations.get(verifiedExpectations.size() - 1);
      return lastMatched.arguments[parameterIndex];
   }

   public final void discardReplayedInvocations()
   {
      if (mockedTypesAndInstancesToFullyVerify == null) {
         expectationsInReplayOrder.clear();
         invocationArgumentsInReplayOrder.clear();
      }
      else {
         for (int i = expectationsInReplayOrder.size() - 1; i >= 0; i--) {
            Expectation expectation = expectationsInReplayOrder.get(i);

            if (isInvocationToBeVerified(expectation.invocation)) {
               expectationsInReplayOrder.remove(i);
               invocationArgumentsInReplayOrder.remove(i);
            }
         }
      }
   }
}
