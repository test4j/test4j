/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.locks.*;

import mockit.*;
import mockit.internal.expectations.invocation.*;
import mockit.internal.expectations.mocking.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class RecordAndReplayExecution
{
   public static final ReentrantLock RECORD_OR_REPLAY_LOCK = new ReentrantLock();
   public static final ReentrantLock TEST_ONLY_PHASE_LOCK = new ReentrantLock();

   private final FieldTypeRedefinitions redefinitions;
   private final Map<Type, Object> typesAndTargetObjects;
   private final DynamicPartialMocking dynamicPartialMocking;

   final PhasedExecutionState executionState;
   final int lastExpectationIndexInPreviousReplayPhase;

   final FailureState failureState;

   private RecordPhase recordPhase;
   private ReplayPhase replayPhase;
   private BaseVerificationPhase verificationPhase;

   public RecordAndReplayExecution()
   {
      validateRecordingContext(false);
      executionState = new PhasedExecutionState();
      lastExpectationIndexInPreviousReplayPhase = 0;
      redefinitions = null;
      typesAndTargetObjects = new HashMap<Type, Object>(1);
      dynamicPartialMocking = null;
      validateThereIsAtLeastOneMockedTypeInScope();
      discoverMockedTypesAndInstancesForMatchingOnInstance();
      failureState = new FailureState();
      replayPhase = new ReplayPhase(this);
   }

   private int getLastExpectationIndexInPreviousReplayPhase()
   {
      return replayPhase == null ? -1 : replayPhase.currentStrictExpectationIndex;
   }

   private void validateRecordingContext(boolean recordingExpectations)
   {
      if (TestRun.getSharedFieldTypeRedefinitions() == null) {
         String msg;

         if (Startup.wasInitializedOnDemand()) {
            msg =
               "JMockit wasn't properly initialized; check that jmockit.jar precedes junit.jar in the classpath " +
               "(if using JUnit; if not, check the documentation)";
         }
         else if (recordingExpectations) {
            msg = "Invalid place to record expectations";
         }
         else {
            msg = "Invalid place to verify expectations";
         }

         IllegalStateException failure = new IllegalStateException(msg);
         StackTrace.filterStackTrace(failure);
         throw failure;
      }
   }

   public RecordAndReplayExecution(Expectations targetObject, Object... classesOrInstancesToBePartiallyMocked)
   {
      validateRecordingContext(targetObject != null);

      TestRun.enterNoMockingZone();
      ExecutingTest executingTest = TestRun.getExecutingTest();
      executingTest.setShouldIgnoreMockingCallbacks(true);

      try {
         RecordAndReplayExecution previous = executingTest.getRecordAndReplay();

         if (previous == null) {
            executionState = new PhasedExecutionState();
            lastExpectationIndexInPreviousReplayPhase = 0;
            typesAndTargetObjects = new HashMap<Type, Object>(2);
         }
         else {
            executionState = previous.executionState;
            lastExpectationIndexInPreviousReplayPhase = previous.getLastExpectationIndexInPreviousReplayPhase();
            typesAndTargetObjects = previous.typesAndTargetObjects;
         }

         failureState = new FailureState();

         boolean nonStrict = targetObject instanceof NonStrictExpectations;
         recordPhase = new RecordPhase(this, nonStrict);

         executingTest.setRecordAndReplay(this);

         redefinitions = redefineFieldTypes(targetObject);
         dynamicPartialMocking = applyDynamicPartialMocking(nonStrict, classesOrInstancesToBePartiallyMocked);

         validateThereIsAtLeastOneMockedTypeInScope();
         discoverMockedTypesAndInstancesForMatchingOnInstance();

         //noinspection LockAcquiredButNotSafelyReleased
         TEST_ONLY_PHASE_LOCK.lock();
      }
      catch (RuntimeException e) {
         executingTest.setRecordAndReplay(null);
         throw e;
      }
      finally {
         executingTest.setShouldIgnoreMockingCallbacks(false);
         TestRun.exitNoMockingZone();
      }
   }

   private FieldTypeRedefinitions redefineFieldTypes(Expectations targetObject)
   {
      LocalFieldTypeRedefinitions redefs = new LocalFieldTypeRedefinitions(targetObject, this);

      //noinspection CatchGenericClass
      try {
         redefs.redefineLocalFieldTypes();
         return redefs.getTypesRedefined() == 0 ? null : redefs;
      }
      catch (Error e) {
         redefs.cleanUp();
         StackTrace.filterStackTrace(e);
         throw e;
      }
      catch (RuntimeException e) {
         redefs.cleanUp();
         StackTrace.filterStackTrace(e);
         throw e;
      }
   }

   private void validateThereIsAtLeastOneMockedTypeInScope()
   {
      if (
         redefinitions == null && dynamicPartialMocking == null &&
         TestRun.getSharedFieldTypeRedefinitions().getTypesRedefined() == 0
      ) {
         ParameterTypeRedefinitions paramTypeRedefinitions = TestRun.getExecutingTest().getParameterTypeRedefinitions();

         if (paramTypeRedefinitions == null || paramTypeRedefinitions.getTypesRedefined() == 0) {
            throw new IllegalStateException(
               "No mocked types in scope; please declare mock fields or parameters for the types you need mocked");
         }
      }
   }

   private void discoverMockedTypesAndInstancesForMatchingOnInstance()
   {
      List<Class<?>> fields = TestRun.getSharedFieldTypeRedefinitions().getTargetClasses();
      List<Class<?>> targetClasses = new ArrayList<Class<?>>(fields);

      if (redefinitions != null) {
         targetClasses.addAll(redefinitions.getTargetClasses());
      }

      ParameterTypeRedefinitions paramTypeRedefinitions = TestRun.getExecutingTest().getParameterTypeRedefinitions();

      if (paramTypeRedefinitions != null) {
         targetClasses.addAll(paramTypeRedefinitions.getTargetClasses());
      }

      executionState.discoverMockedTypesToMatchOnInstances(targetClasses);

      if (dynamicPartialMocking != null) {
         executionState.setDynamicMockInstancesToMatch(dynamicPartialMocking.targetInstances);
      }
   }

   public Map<Type, Object> getLocalMocks() { return typesAndTargetObjects; }

   public void addLocalMock(Type mockFieldType, Object targetObject)
   {
      typesAndTargetObjects.put(mockFieldType, targetObject);
   }

   public void addMockedTypeToMatchOnInstance(Class<?> mockedType)
   {
      executionState.addMockedTypeToMatchOnInstance(mockedType);
   }

   private DynamicPartialMocking applyDynamicPartialMocking(boolean nonStrict, Object... classesOrInstances)
   {
      if (classesOrInstances == null || classesOrInstances.length == 0) {
         return null;
      }

      DynamicPartialMocking mocking = new DynamicPartialMocking(nonStrict);
      mocking.redefineTypes(classesOrInstances);
      return mocking;
   }

   public RecordPhase getRecordPhase()
   {
      if (recordPhase == null) {
         throw new IllegalStateException("Not in the recording phase");
      }

      return recordPhase;
   }

   Error getErrorThrown() { return failureState.getErrorThrown(); }
   void setErrorThrown(Error error) { failureState.setErrorThrown(error); }

   /**
    * Only to be called from generated bytecode or from the Mocking Bridge.
    */
   public static Object recordOrReplay(
      Object mock, int mockAccess, String classDesc, String mockDesc, String genericSignature, String exceptions,
      int executionMode, Object... args)
      throws Throwable
   {
      if (
         RECORD_OR_REPLAY_LOCK.isHeldByCurrentThread() ||
         TEST_ONLY_PHASE_LOCK.isLocked() && !TEST_ONLY_PHASE_LOCK.isHeldByCurrentThread()
      ) {
         // This occurs if called from a custom argument matching method, in the instantiation of an @Input value,
         // in a call to an overridden Object method (equals, hashCode, toString), during static initialization of a
         // mocked class which calls another mocked method, or from a different thread during a recording/verification.
         return defaultReturnValue(mock, classDesc, mockDesc, genericSignature, executionMode, args);
      }

      ExecutingTest executingTest = TestRun.getExecutingTest();

      if (executingTest.isShouldIgnoreMockingCallbacks()) {
         // This occurs when called from a reentrant delegate method, or during static initialization of a mocked class
         // being instantiated for a local mock field.
         return defaultReturnValue(executingTest, mock, classDesc, mockDesc, genericSignature, executionMode, args);
      }
      else if (executingTest.isProceedingIntoRealImplementation()) {
         if (executionMode == 0) {
            throw new UnsupportedOperationException(
               "Cannot proceed into method unless mocked type is injectable or dynamic");
         }

         return Void.class;
      }
      else if (mock != null && executionMode == 3 && !TestRun.mockFixture().isInstanceOfMockedClass(mock)) {
         return Void.class;
      }

      executingTest.registerAdditionalMocksFromFinalLocalMockFieldsIfAny();

      if (executionMode == 2 && (mock == null || !executingTest.isInjectableMock(mock))) {
         return Void.class;
      }

      RECORD_OR_REPLAY_LOCK.lock();

      try {
         RecordAndReplayExecution instance = TestRun.getRecordAndReplayForRunningTest(true);

         if (mockDesc.startsWith("<init>") && handleCallToConstructor(instance, mock, classDesc)) {
            return
               executionMode == 0 || executionMode == 3 ||
               executionMode == 1 && !inReplayPhase(instance) ||
               executingTest.isInjectableMock(mock) ? null : Void.class;
         }

         if (instance == null) {
            // This occurs when a constructor of the mocked class is called in a mock field assignment expression,
            // during initialization of a mocked class, or during the restoration of mocked classes between tests.
            return defaultReturnValue(executingTest, mock, classDesc, mockDesc, genericSignature, executionMode, args);
         }

         Phase currentPhase = instance.getCurrentPhase();
         instance.failureState.clearErrorThrown();

         boolean withRealImpl = executionMode == 1;
         Object result =
            currentPhase.handleInvocation(
               mock, mockAccess, classDesc, mockDesc, genericSignature, exceptions, withRealImpl, args);

         instance.failureState.reportErrorThrownIfAny();

         return result;
      }
      finally {
         RECORD_OR_REPLAY_LOCK.unlock();
      }
   }

   public static Object defaultReturnValue(
      Object mock, String classDesc, String nameAndDesc, String genericSignature, int executionMode, Object[] args)
   {
      if (mock != null) {
         Object rv = Utilities.evaluateObjectOverride(mock, nameAndDesc, args);

         if (rv != null) {
            return rv;
         }
      }

      String returnTypeDesc = DefaultValues.getReturnTypeDesc(nameAndDesc);
      Object cascadedInstance = MockedTypeCascade.getMock(classDesc, mock, returnTypeDesc, genericSignature);
      if (cascadedInstance != null) return cascadedInstance;

      return executionMode == 0 ? DefaultValues.computeForReturnType(nameAndDesc) : Void.class;
   }

   private static Object defaultReturnValue(
      ExecutingTest executingTest, Object mock, String classDesc, String nameAndDesc, String genericSignature,
      int executionMode, Object[] args) throws Throwable
   {
      RecordAndReplayExecution execution = executingTest.getCurrentRecordAndReplay();

      if (execution != null) {
         Expectation recordedExpectation =
            execution.executionState.findNonStrictExpectation(mock, classDesc, nameAndDesc, args);

         if (recordedExpectation != null) {
            return recordedExpectation.produceResult(mock, args);
         }
      }

      return defaultReturnValue(mock, classDesc, nameAndDesc, genericSignature, executionMode, args);
   }

   private static boolean inReplayPhase(RecordAndReplayExecution instance)
   {
      return instance == null || instance.replayPhase != null;
   }

   private static boolean handleCallToConstructor(RecordAndReplayExecution instance, Object mock, String classDesc)
   {
      if (TestRun.getCurrentTestInstance() != null && inReplayPhase(instance)) {
         FieldTypeRedefinitions fieldTypeRedefs = instance == null ? null : instance.redefinitions;

         if (fieldTypeRedefs != null && fieldTypeRedefs.captureNewInstanceForApplicableMockField(mock)) {
            return true;
         }

         ParameterTypeRedefinitions paramTypeRedefinitions = TestRun.getExecutingTest().getParameterTypeRedefinitions();

         if (paramTypeRedefinitions != null) {
            CaptureOfNewInstancesForParameters paramTypeCaptures = paramTypeRedefinitions.getCaptureOfNewInstances();

            if (paramTypeCaptures != null && paramTypeCaptures.captureNewInstanceForApplicableMockParameter(mock)) {
               return true;
            }
         }

         FieldTypeRedefinitions sharedFieldTypeRedefs = TestRun.getSharedFieldTypeRedefinitions();

         if (sharedFieldTypeRedefs.captureNewInstanceForApplicableMockField(mock)) {
            return true;
         }
      }

      return isCallToSuperClassConstructor(mock, classDesc);
   }

   private static boolean isCallToSuperClassConstructor(Object mock, String calledClassDesc)
   {
      Class<?> mockedClass = mock.getClass();

      if (Utilities.isAnonymousClass(mockedClass)) {
         // An anonymous class instantiation always invokes the constructor on the super-class,
         // so that is the class we need to consider, not the anonymous one.
         mockedClass = mockedClass.getSuperclass();

         if (mockedClass == Object.class) {
            return false;
         }
      }

      String calledClassName = calledClassDesc.replace('/', '.');

      return !calledClassName.equals(mockedClass.getName());
   }

   private Phase getCurrentPhase()
   {
      ReplayPhase replay = replayPhase;

      if (replay == null) {
         return recordPhase;
      }

      BaseVerificationPhase verification = verificationPhase;

      if (verification == null) {
         if (failureState.getErrorThrown() != null) {
            // This can only happen when called from a catch/finally block.
            throw failureState.getErrorThrown();
         }

         return replay;
      }

      return verification;
   }

   public BaseVerificationPhase startVerifications(boolean inOrder)
   {
      if (replayPhase == null) {
         throw new IllegalStateException("Not in the replay phase yet");
      }

      List<Expectation> expectations = replayPhase.nonStrictInvocations;
      List<Object[]> invocationArguments = replayPhase.nonStrictInvocationArguments;
      verificationPhase =
         inOrder ?
            new OrderedVerificationPhase(this, expectations, invocationArguments) :
            new UnorderedVerificationPhase(this, expectations, invocationArguments);

      return verificationPhase;
   }

   public static Error endCurrentReplayIfAny()
   {
      RecordAndReplayExecution instance = TestRun.getRecordAndReplayForRunningTest(false);
      return instance == null ? null : instance.endExecution();
   }

   private Error endExecution()
   {
      if (TEST_ONLY_PHASE_LOCK.isLocked()) {
         TEST_ONLY_PHASE_LOCK.unlock();
      }

      switchFromRecordToReplayIfNotYet();

      if (redefinitions != null) {
         redefinitions.cleanUp();
      }

      Error error = replayPhase.endExecution();

      if (error == null) {
         error = failureState.getErrorThrownInAnotherThreadIfAny(error);
      }

      if (error == null && verificationPhase != null) {
         error = verificationPhase.endVerification();
         verificationPhase = null;
      }

      return error;
   }

   private void switchFromRecordToReplayIfNotYet()
   {
      if (replayPhase == null) {
         recordPhase = null;
         replayPhase = new ReplayPhase(this);
      }
   }

   public TestOnlyPhase getCurrentTestOnlyPhase()
   {
      return recordPhase != null ? recordPhase : verificationPhase;
   }

   public void endInvocations()
   {
      TEST_ONLY_PHASE_LOCK.unlock();

      if (verificationPhase == null) {
         switchFromRecordToReplayIfNotYet();
      }
      else {
         Error error = verificationPhase.endVerification();
         verificationPhase = null;

         if (error != null) {
            throw error;
         }
      }
   }
}
