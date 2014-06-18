/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import java.lang.reflect.*;

import mockit.*;
import mockit.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class MockMethodBridge extends MockingBridge
{
   public static final MockingBridge MB = new MockMethodBridge();

   private MockMethodBridge() { super(MockMethodBridge.class); }

   public Object invoke(Object mocked, Method method, Object[] args) throws Throwable
   {
      String mockDesc = (String) args[3];

      if (wasCalledDuringClassLoading()) {
         return DefaultValues.computeForReturnType(mockDesc);
      }

      String mockClassDesc = (String) args[1];

      if (notToBeMocked(mocked, mockClassDesc)) {
         return Void.class;
      }

      boolean callingInstanceMethod = (Boolean) args[0];
      String mockName = (String) args[2];
      int mockStateIndex = (Integer) args[4];
      int mockInstanceIndex = (Integer) args[5];
      boolean startupMock = (Boolean) args[6];
      Object[] mockArgs = extractMockArguments(args);

      return callMock(
         mocked, callingInstanceMethod, mockClassDesc, mockName, mockDesc, mockStateIndex, mockInstanceIndex,
         startupMock, mockArgs);
   }

   private static Object callMock(
      Object mocked, boolean callingInstanceMethod, String mockClassInternalName, String mockName, String mockDesc,
      int mockStateIndex, int mockInstanceIndex, boolean startupMock, Object[] mockArgs) throws Throwable
   {
      Object mock;
      Class<?> mockClass;

      if (callingInstanceMethod) {
         mock = startupMock ? TestRun.getStartupMock(mockInstanceIndex) : TestRun.getMock(mockInstanceIndex);
         mockClass = mock.getClass();
      }
      else {
         mock = mocked;
         String mockClassName = mockClassInternalName.replace('/', '.');
         mockClass = ClassLoad.loadClass(mockClassName);
      }

      Class<?>[] paramClasses = TypeDescriptor.getParameterTypes(mockDesc);
      Method mockMethod = mockStateIndex < 0 ? null :
         TestRun.getMockClasses().getMockStates().getMockMethod(
            mockClassInternalName, mockStateIndex, mockClass, paramClasses);
      MockInvocation invocation = null;

      if (paramClasses.length > 0 && paramClasses[0] == Invocation.class) {
         invocation = TestRun.createMockInvocation(mockClassInternalName, mockStateIndex, mocked, mockArgs);
         //noinspection AssignmentToMethodParameter
         mockArgs = ParameterReflection.argumentsWithExtraFirstValue(mockArgs, invocation);
      }

      Object result =
         mockMethod == null ?
            MethodReflection.invokeWithCheckedThrows(mockClass, mock, mockName, paramClasses, mockArgs) :
            MethodReflection.invokeWithCheckedThrows(mock, mockMethod, mockArgs);

      return invocation != null && invocation.shouldProceedIntoConstructor() ? Void.class : result;
   }
}
