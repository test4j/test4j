/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import static mockit.internal.util.Utilities.containsReference;

import mockit.internal.expectations.invocation.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

final class PhasedExecutionState
{
   final List<Expectation> expectations;
   final List<Expectation> nonStrictExpectations;
   final List<VerifiedExpectation> verifiedExpectations;
   final Map<Object, Object> instanceMap;
   final Map<Object, Object> replacementMap;
   private List<?> dynamicMockInstancesToMatch;
   private List<Class<?>> mockedTypesToMatchOnInstances;

   PhasedExecutionState()
   {
      expectations = new ArrayList<Expectation>();
      nonStrictExpectations = new ArrayList<Expectation>();
      verifiedExpectations = new ArrayList<VerifiedExpectation>();
      instanceMap = new IdentityHashMap<Object, Object>();
      replacementMap = new IdentityHashMap<Object, Object>();
   }

   void setDynamicMockInstancesToMatch(List<?> dynamicMockInstancesToMatch)
   {
      this.dynamicMockInstancesToMatch = dynamicMockInstancesToMatch;
   }

   void discoverMockedTypesToMatchOnInstances(List<Class<?>> targetClasses)
   {
      int numClasses = targetClasses.size();

      if (numClasses > 1) {
         for (int i = 0; i < numClasses; i++) {
            Class<?> targetClass = targetClasses.get(i);

            if (targetClasses.lastIndexOf(targetClass) > i) {
               addMockedTypeToMatchOnInstance(targetClass);
            }
         }
      }
   }

   void addMockedTypeToMatchOnInstance(Class<?> mockedType)
   {
      if (mockedTypesToMatchOnInstances == null) {
         mockedTypesToMatchOnInstances = new LinkedList<Class<?>>();
      }

      if (!containsReference(mockedTypesToMatchOnInstances, mockedType)) {
         mockedTypesToMatchOnInstances.add(mockedType);
      }
   }

   void addExpectation(Expectation expectation, boolean nonStrict)
   {
      ExpectedInvocation invocation = expectation.invocation;
      forceMatchingOnMockInstanceIfRequired(invocation);
      removeMatchingExpectationsCreatedBefore(invocation);

      if (nonStrict) {
         nonStrictExpectations.add(expectation);
      }
      else {
         expectations.add(expectation);
      }
   }

   private void forceMatchingOnMockInstanceIfRequired(ExpectedInvocation invocation)
   {
      if (isToBeMatchedOnInstance(invocation.instance, invocation.getMethodNameAndDescription())) {
         invocation.matchInstance = true;
      }
   }

   boolean isToBeMatchedOnInstance(Object mock, String mockNameAndDesc)
   {
      if (mock == null || mockNameAndDesc.charAt(0) == '<') {
         return false;
      }
      else if (dynamicMockInstancesToMatch != null && containsReference(dynamicMockInstancesToMatch, mock)) {
         return true;
      }
      else if (mockedTypesToMatchOnInstances != null) {
         Class<?> mockedClass = GeneratedClasses.getMockedClass(mock);

         if (containsReference(mockedTypesToMatchOnInstances, mockedClass)) {
            return true;
         }
      }
      else if (TestRun.getExecutingTest().isInjectableMock(mock)) {
         return true;
      }

      return false;
   }

   private void removeMatchingExpectationsCreatedBefore(ExpectedInvocation invocation)
   {
      Expectation previousExpectation = findPreviousNonStrictExpectation(invocation);

      if (previousExpectation != null) {
         nonStrictExpectations.remove(previousExpectation);
         invocation.copyDefaultReturnValue(previousExpectation.invocation);
      }
   }

   private Expectation findPreviousNonStrictExpectation(ExpectedInvocation newInvocation)
   {
      Object mock = newInvocation.instance;
      String mockClassDesc = newInvocation.getClassDesc();
      String mockNameAndDesc = newInvocation.getMethodNameAndDescription();
      InvocationArguments arguments = newInvocation.arguments;
      Object[] argValues = arguments.getValues();

      boolean staticOrConstructorInvocation = mock == null || newInvocation.isConstructor();
      boolean newInvocationWithMatchers = arguments.getMatchers() != null;

      for (int i = 0, n = nonStrictExpectations.size(); i < n; i++) {
         Expectation previousExpectation = nonStrictExpectations.get(i);
         ExpectedInvocation previousInvocation = previousExpectation.invocation;

         if (
            previousInvocation.isMatch(mockClassDesc, mockNameAndDesc) &&
            (staticOrConstructorInvocation || isMatchingInstance(mock, previousExpectation)) &&
            (newInvocationWithMatchers && arguments.hasEquivalentMatchers(previousInvocation.arguments) ||
             !newInvocationWithMatchers && previousInvocation.arguments.isMatch(argValues, instanceMap))
         ) {
            return previousExpectation;
         }
      }

      return null;
   }

   Expectation findNonStrictExpectation(Object mock, String mockClassDesc, String mockNameAndDesc, Object[] args)
   {
      boolean constructorInvocation = mockNameAndDesc.charAt(0) == '<';
      boolean staticOrConstructorInvocation = mock == null || constructorInvocation;
      Expectation replayExpectationFound = null;

      // Note: new expectations might get added to the list, so a regular loop would cause a CME:
      for (int i = 0, n = nonStrictExpectations.size(); i < n; i++) {
         Expectation nonStrict = nonStrictExpectations.get(i);

         if (replayExpectationFound != null && nonStrict.recordPhase == null) {
            continue;
         }

         ExpectedInvocation invocation = nonStrict.invocation;

         if (
            invocation.isMatch(mockClassDesc, mockNameAndDesc) &&
            (staticOrConstructorInvocation || isMatchingInstance(mock, nonStrict)) &&
            invocation.arguments.isMatch(args, instanceMap)
         ) {
            if (nonStrict.recordPhase == null) {
               replayExpectationFound = nonStrict;
               continue;
            }

            if (constructorInvocation && invocation.replacementInstance != null) {
               replacementMap.put(mock, invocation.replacementInstance);
            }

            return nonStrict;
         }
      }

      return replayExpectationFound;
   }

   private boolean isMatchingInstance(Object mock, Expectation expectation)
   {
      ExpectedInvocation invocation = expectation.invocation;

      if (invocation.instance == replacementMap.get(mock) || invocation.isEquivalentInstance(mock, instanceMap)) {
         return true;
      }

      if (TestRun.getExecutingTest().isInjectableMock(mock)) {
         return false;
      }

      if (dynamicMockInstancesToMatch != null) {
         if (containsReference(dynamicMockInstancesToMatch, mock)) {
            return false;
         }

         Class<?> invokedClass = invocation.instance.getClass();

         for (Object dynamicMock : dynamicMockInstancesToMatch) {
            if (dynamicMock.getClass() == invokedClass) {
               return false;
            }
         }
      }

      return !invocation.matchInstance && expectation.recordPhase != null;
   }

   Object getReplacementInstanceForMethodInvocation(Object invokedInstance, String methodNameAndDesc)
   {
      return methodNameAndDesc.charAt(0) == '<' ? null : replacementMap.get(invokedInstance);
   }
}
