/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.util.*;

import static mockit.internal.util.Utilities.containsReference;

import mockit.external.asm4.*;
import mockit.internal.expectations.*;
import mockit.internal.expectations.invocation.*;
import mockit.internal.expectations.mocking.*;
import mockit.internal.util.*;

@SuppressWarnings("ClassWithTooManyFields")
public final class ExecutingTest
{
   private RecordAndReplayExecution currentRecordAndReplay;
   private RecordAndReplayExecution recordAndReplayForLastTestMethod;
   private boolean shouldIgnoreMockingCallbacks;
   private boolean proceeding;

   private ParameterTypeRedefinitions parameterTypeRedefinitions;

   private final Map<MockedType, Object> finalLocalMockFields = new HashMap<MockedType, Object>(4);
   private final List<Object> injectableMocks = new ArrayList<Object>();
   private final Map<Object, Object> originalToCapturedInstance = new IdentityHashMap<Object, Object>(4);
   private final List<Object> nonStrictMocks = new ArrayList<Object>();
   private final List<Object> strictMocks = new ArrayList<Object>();

   private final Map<String, MockedTypeCascade> cascadingTypes = new HashMap<String, MockedTypeCascade>(4);
   public final DefaultResults defaultResults = new DefaultResults();

   RecordAndReplayExecution getRecordAndReplay(boolean createIfUndefined)
   {
      if (currentRecordAndReplay == null && createIfUndefined) {
         setRecordAndReplay(new RecordAndReplayExecution());
      }

      return currentRecordAndReplay;
   }

   public RecordAndReplayExecution getRecordAndReplay()
   {
      recordAndReplayForLastTestMethod = null;
      RecordAndReplayExecution previous = currentRecordAndReplay;
      currentRecordAndReplay = null;
      return previous;
   }

   public void setRecordAndReplay(RecordAndReplayExecution newRecordAndReplay)
   {
      recordAndReplayForLastTestMethod = null;
      currentRecordAndReplay = newRecordAndReplay;
   }

   public RecordAndReplayExecution getCurrentRecordAndReplay() { return currentRecordAndReplay; }

   public boolean isShouldIgnoreMockingCallbacks() { return shouldIgnoreMockingCallbacks; }
   public void setShouldIgnoreMockingCallbacks(boolean flag) { shouldIgnoreMockingCallbacks = flag; }

   public boolean isProceedingIntoRealImplementation()
   {
      boolean result = proceeding;
      proceeding = false;
      return result;
   }

   public void markAsProceedingIntoRealImplementation() { proceeding = true; }

   public void clearRecordAndReplayForVerifications()
   {
      recordAndReplayForLastTestMethod = null;
   }

   public RecordAndReplayExecution getRecordAndReplayForVerifications()
   {
      if (currentRecordAndReplay == null) {
         if (recordAndReplayForLastTestMethod != null) {
            currentRecordAndReplay = recordAndReplayForLastTestMethod;
         }
         else {
            // This should only happen if no expectations at all were created by the whole test, but
            // there is one (probably empty) verification block.
            currentRecordAndReplay = new RecordAndReplayExecution();
         }
      }

      //noinspection LockAcquiredButNotSafelyReleased
      RecordAndReplayExecution.TEST_ONLY_PHASE_LOCK.lock();

      return currentRecordAndReplay;
   }

   public ParameterTypeRedefinitions getParameterTypeRedefinitions() { return parameterTypeRedefinitions; }
   public void setParameterTypeRedefinitions(ParameterTypeRedefinitions redefinitions)
   { parameterTypeRedefinitions = redefinitions; }

   public void clearInjectableAndNonStrictMocks()
   {
      injectableMocks.clear();
      clearNonStrictMocks();
      originalToCapturedInstance.clear();
   }

   public void addInjectableMock(Object mock)
   {
      if (!isInjectableMock(mock)) {
         injectableMocks.add(mock);
      }
   }

   public boolean isInjectableMock(Object mock)
   {
      for (Object injectableMock : injectableMocks) {
         if (mock == injectableMock) {
            return true;
         }
      }

      return false;
   }

   public void addCapturedInstanceForInjectableMock(Object originalInstance, Object capturedInstance)
   {
      injectableMocks.add(capturedInstance);
      addCapturedInstance(originalInstance, capturedInstance);
   }

   public void addCapturedInstance(Object originalInstance, Object capturedInstance)
   {
      originalToCapturedInstance.put(capturedInstance, originalInstance);
   }

   public boolean isInvokedInstanceEquivalentToCapturedInstance(Object invokedInstance, Object capturedInstance)
   {
      return
         invokedInstance == originalToCapturedInstance.get(capturedInstance) ||
         capturedInstance == originalToCapturedInstance.get(invokedInstance);
   }

   public void discardCascadedMockWhenInjectable(Object oldMock)
   {
      for (int i = 0, n = injectableMocks.size(); i < n; i++) {
         if (injectableMocks.get(i) == oldMock) {
            injectableMocks.remove(i);
            return;
         }
      }
   }

   public void addNonStrictMock(Class<?> mockedClass)
   {
      String mockedClassDesc = Type.getInternalName(mockedClass);
      addNonStrictMock(mockedClassDesc.intern());
   }

   private void addNonStrictMock(Object mock)
   {
      if (!containsNonStrictMock(mock)) {
         nonStrictMocks.add(mock);
      }
   }

   private boolean containsNonStrictMock(Object mockOrClassDesc)
   {
      return containsReference(nonStrictMocks, mockOrClassDesc);
   }

   public void addFinalLocalMockField(Object owner, MockedType typeMetadata)
   {
      finalLocalMockFields.put(typeMetadata, owner);
   }

   public void addStrictMock(Object mock, String mockClassDesc)
   {
      addStrictMock(mock);

      if (mockClassDesc != null) {
         String uniqueMockClassDesc = mockClassDesc.intern();

         if (!containsStrictMock(uniqueMockClassDesc) && !containsNonStrictMock(uniqueMockClassDesc)) {
            strictMocks.add(uniqueMockClassDesc);
         }
      }
   }

   private void addStrictMock(Object mock)
   {
      if (mock != null && !containsStrictMock(mock)) {
         strictMocks.add(mock);
      }
   }

   private boolean containsStrictMock(Object mockOrClassDesc)
   {
      return containsReference(strictMocks, mockOrClassDesc);
   }

   public void registerAsNonStrictlyMocked(Class<?> mockedClass)
   {
      String toBeRegistered = Type.getInternalName(mockedClass).intern();
      registerAsNonStrictMock(toBeRegistered, mockedClass);
   }

   public void registerAsNonStrictlyMocked(Object mockedObject) { registerAsNonStrictMock(mockedObject, mockedObject); }

   private void registerAsNonStrictMock(Object toBeRegistered, Object mockedObjectOrClass)
   {
      if (containsStrictMock(toBeRegistered)) {
         throw new IllegalArgumentException("Already mocked strictly: " + mockedObjectOrClass);
      }

      addNonStrictMock(toBeRegistered);
   }

   public boolean isNonStrictInvocation(Object mock, String mockClassDesc, String mockNameAndDesc)
   {
      if (isInstanceMethodWithStandardBehavior(mock, mockNameAndDesc)) {
         return true;
      }

      for (Object nonStrictMock : nonStrictMocks) {
         if (nonStrictMock == mock || nonStrictMock == mockClassDesc) {
            return true;
         }
      }

      return false;
   }

   private boolean isInstanceMethodWithStandardBehavior(Object mock, String nameAndDesc)
   {
      return
         mock != null && nameAndDesc.charAt(0) != '<' &&
         ("equals(Ljava/lang/Object;)Z hashCode()I toString()Ljava/lang/String;".contains(nameAndDesc) ||
          mock instanceof Comparable<?> && nameAndDesc.startsWith("compareTo(L") && nameAndDesc.endsWith(";)I"));
   }

   public void registerAdditionalMocksFromFinalLocalMockFieldsIfAny()
   {
      if (!finalLocalMockFields.isEmpty()) {
         for (
            Iterator<Map.Entry<MockedType, Object>> itr = finalLocalMockFields.entrySet().iterator();
            itr.hasNext();
         ) {
            Map.Entry<MockedType, Object> fieldAndOwner = itr.next();
            MockedType typeMetadata = fieldAndOwner.getKey();
            Object mock = Utilities.getFieldValue(typeMetadata.field, fieldAndOwner.getValue());

            // A null field value will occur for invocations executed during initialization of the owner instance.
            if (mock != null) {
               registerMock(typeMetadata, mock);
               itr.remove();
            }
         }
      }
   }

   public void registerMock(MockedType typeMetadata, Object mock)
   {
      if (typeMetadata.injectable) {
         addInjectableMock(mock);
      }

      if (typeMetadata.nonStrict) {
         addNonStrictMock(mock);
         addNonStrictMock(mock.getClass());
      }
   }

   public boolean isStrictInvocation(Object mock, String mockClassDesc, String mockNameAndDesc)
   {
      if (isInstanceMethodWithStandardBehavior(mock, mockNameAndDesc)) {
         return false;
      }

      for (Object strictMock : strictMocks) {
         if (strictMock == mock) {
            return true;
         }
         else if (strictMock == mockClassDesc) {
            addStrictMock(mock);
            return true;
         }
      }

      return false;
   }

   public void clearNonStrictMocks()
   {
      finalLocalMockFields.clear();
      nonStrictMocks.clear();
   }

   public void addCascadingType(String mockedTypeDesc, MockedType mockedType)
   {
      if (!cascadingTypes.containsKey(mockedTypeDesc)) {
         cascadingTypes.put(mockedTypeDesc, new MockedTypeCascade(mockedType));
      }
   }
   
   public MockedTypeCascade getMockedTypeCascade(String mockedTypeDesc, Object mockInstance)
   {
      if (cascadingTypes.isEmpty()) {
         return null;
      }

      MockedTypeCascade cascade = cascadingTypes.get(mockedTypeDesc);

      if (cascade != null || mockInstance == null) {
         return cascade;
      }

      return getMockedTypeCascade(mockedTypeDesc, mockInstance.getClass());
   }

   private MockedTypeCascade getMockedTypeCascade(String invokedTypeDesc, Class<?> mockedType)
   {
      Class<?> typeToLookFor = mockedType;

      do {
         String typeDesc = Type.getInternalName(typeToLookFor);

         if (invokedTypeDesc.equals(typeDesc)) {
            return null;
         }

         MockedTypeCascade cascade = cascadingTypes.get(typeDesc);

         if (cascade != null) {
            return cascade;
         }

         typeToLookFor = typeToLookFor.getSuperclass();
      }
      while (typeToLookFor != Object.class);
      
      return null;
   }

   void finishExecution(boolean clearSharedMocks)
   {
      recordAndReplayForLastTestMethod = currentRecordAndReplay;
      currentRecordAndReplay = null;

      if (parameterTypeRedefinitions != null) {
         parameterTypeRedefinitions.cleanUp();
         parameterTypeRedefinitions = null;
      }

      if (clearSharedMocks) {
         clearNonStrictMocks();
      }

      strictMocks.clear();
      clearNonSharedCascadingTypes();
      defaultResults.clear();
   }

   private void clearNonSharedCascadingTypes()
   {
      Iterator<MockedTypeCascade> itr = cascadingTypes.values().iterator();

      while (itr.hasNext()) {
         MockedTypeCascade cascade = itr.next();

         if (cascade.isSharedBetweenTests()) {
            cascade.discardCascadedMocks();
         }
         else {
            itr.remove();
         }
      }
   }

   public void clearCascadingTypes()
   {
      cascadingTypes.clear();
   }
}
