/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import java.lang.reflect.*;
import java.util.*;

/**
 * Holds state associated with mock class containing {@linkplain mockit.Mock annotated mocks}.
 */
public final class MockStates
{
   /**
    * For each mock class containing @Mock annotations with at least one invocation expectation specified or at least
    * one reentrant mock, a runtime state will be kept here.
    */
   private final Map<String, List<MockState>> mockClassToMockStates;

   /**
    * For each annotated mock method with at least one invocation expectation, its mock state will
    * also be kept here, as an optimization.
    */
   private final Set<MockState> mockStatesWithExpectations;

   public MockStates()
   {
      mockClassToMockStates = new HashMap<String, List<MockState>>(8);
      mockStatesWithExpectations = new LinkedHashSet<MockState>(10);
   }

   void addMockStates(List<MockState> mockStates)
   {
      for (MockState mockState : mockStates) {
         if (mockState.isWithExpectations()) {
            mockStatesWithExpectations.add(mockState);
         }
      }
   }

   void addMockClassAndItsStates(String mockClassInternalName, List<MockState> mockStates)
   {
      mockClassToMockStates.put(mockClassInternalName, mockStates);
   }

   public void removeClassState(Class<?> redefinedClass, String internalNameForOneOrMoreMockClasses)
   {
      removeMockStates(redefinedClass);

      if (internalNameForOneOrMoreMockClasses != null) {
         if (internalNameForOneOrMoreMockClasses.indexOf(' ') < 0) {
            removeMockStates(internalNameForOneOrMoreMockClasses);
         }
         else {
            String[] mockClassesInternalNames = internalNameForOneOrMoreMockClasses.split(" ");

            for (String mockClassInternalName : mockClassesInternalNames) {
               removeMockStates(mockClassInternalName);
            }
         }
      }
   }

   private void removeMockStates(Class<?> redefinedClass)
   {
      for (Iterator<List<MockState>> itr = mockClassToMockStates.values().iterator(); itr.hasNext(); ) {
         List<MockState> mockStates = itr.next();
         MockState mockState = mockStates.get(0);

         if (mockState.getRealClass() == redefinedClass) {
            mockStatesWithExpectations.removeAll(mockStates);
            mockStates.clear();
            itr.remove();
         }
      }
   }

   private void removeMockStates(String mockClassInternalName)
   {
      List<MockState> mockStates = mockClassToMockStates.remove(mockClassInternalName);

      if (mockStates != null) {
         mockStatesWithExpectations.removeAll(mockStates);
      }
   }

   public boolean updateMockState(String mockClassName, int mockStateIndex)
   {
      MockState mockState = getMockState(mockClassName, mockStateIndex);

      if (mockState.isOnReentrantCall()) {
         mockState.exitReentrantCall();
         return false;
      }

      mockState.update();
      return true;
   }

   MockState getMockState(String mockClassInternalName, int mockStateIndex)
   {
      List<MockState> mockStates = mockClassToMockStates.get(mockClassInternalName);
      return mockStates.get(mockStateIndex);
   }

   public Method getMockMethod(String mockClassDesc, int mockStateIndex, Class<?> mockClass, Class<?>[] paramTypes)
   {
      MockState mockState = getMockState(mockClassDesc, mockStateIndex);

      if (mockState != null) {
         return mockState.getMockMethod(mockClass, paramTypes);
      }

      return null;
   }

   public void exitReentrantMock(String mockClassInternalName, int mockStateIndex)
   {
      MockState mockState = getMockState(mockClassInternalName, mockStateIndex);
      mockState.exitReentrantCall();
   }

   public MockInvocation createMockInvocation(
      String mockClassInternalName, int mockStateIndex, Object invokedInstance, Object[] invokedArguments)
   {
      MockState mockState = getMockState(mockClassInternalName, mockStateIndex);
      return new MockInvocation(invokedInstance, invokedArguments, mockState);
   }

   public void verifyExpectations()
   {
      for (MockState mockState : mockStatesWithExpectations) {
         mockState.verifyExpectations();
      }
   }

   public void resetExpectations()
   {
      for (MockState mockState : mockStatesWithExpectations) {
         mockState.reset();
      }
   }
}
