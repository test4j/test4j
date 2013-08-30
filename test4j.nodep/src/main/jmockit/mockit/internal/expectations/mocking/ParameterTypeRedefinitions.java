/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import mockit.internal.state.*;

public final class ParameterTypeRedefinitions extends TypeRedefinitions
{
   private final Type[] paramTypes;
   private final Annotation[][] paramAnnotations;
   private final Object[] paramValues;
   private final MockedType[] mockParameters;
   private final List<MockedType> injectableParameters;

   public ParameterTypeRedefinitions(Object owner, Method testMethod, Object[] parameterValues)
   {
      super(owner);

      TestRun.enterNoMockingZone();

      try {
         paramTypes = testMethod.getGenericParameterTypes();
         paramAnnotations = testMethod.getParameterAnnotations();
         int n = paramTypes.length;
         paramValues = parameterValues == null || parameterValues.length != n ? new Object[n] : parameterValues;
         mockParameters = new MockedType[n];
         injectableParameters = new ArrayList<MockedType>(n);

         String testClassDesc = mockit.external.asm4.Type.getInternalName(testMethod.getDeclaringClass());
         String testMethodDesc = testMethod.getName() + mockit.external.asm4.Type.getMethodDescriptor(testMethod);

         for (int i = 0; i < n; i++) {
            getMockedTypeFromMockParameterDeclaration(testClassDesc, testMethodDesc, i);
         }

         redefineAndInstantiateMockedTypes();
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }

   private void getMockedTypeFromMockParameterDeclaration(String testClassDesc, String testMethodDesc, int paramIndex)
   {
      Type paramType = paramTypes[paramIndex];
      Annotation[] annotationsOnParameter = paramAnnotations[paramIndex];

      typeMetadata = new MockedType(testClassDesc, testMethodDesc, paramIndex, paramType, annotationsOnParameter);
      mockParameters[paramIndex] = typeMetadata;

      if (typeMetadata.injectable) {
         injectableParameters.add(typeMetadata);
         paramValues[paramIndex] = typeMetadata.providedValue;
      }
   }

   private void redefineAndInstantiateMockedTypes()
   {
      for (int i = 0; i < mockParameters.length; i++) {
         typeMetadata = mockParameters[i];

         if (typeMetadata.isMockableType()) {
            Object mockedInstance = redefineAndInstantiateMockedType();
            paramValues[i] = mockedInstance;
            typeMetadata.providedValue = mockedInstance;
         }
      }
   }

   private Object redefineAndInstantiateMockedType()
   {
      TypeRedefinition typeRedefinition = new TypeRedefinition(parentObject, typeMetadata);
      Object mock = typeRedefinition.redefineType().create();
      registerMock(mock);

      if (typeMetadata.withInstancesToCapture()) {
         registerCaptureOfNewInstances(mock);
      }

      addTargetClass(typeMetadata.withInstancesToCapture(), typeRedefinition.targetClass);
      typesRedefined++;

      return mock;
   }

   private void registerCaptureOfNewInstances(Object originalInstance)
   {
      CaptureOfNewInstancesForParameters capture = getCaptureOfNewInstances();

      if (capture == null) {
         capture = new CaptureOfNewInstancesForParameters();
         captureOfNewInstances = capture;
      }

      capture.registerCaptureOfNewInstances(typeMetadata, originalInstance);
      capture.makeSureAllSubtypesAreModified(typeMetadata.getClassType());
   }

   @Override
   public CaptureOfNewInstancesForParameters getCaptureOfNewInstances()
   {
      return (CaptureOfNewInstancesForParameters) captureOfNewInstances;
   }

   public List<MockedType> getInjectableParameters() { return injectableParameters; }
   public Object[] getParameterValues() { return paramValues; }
}
