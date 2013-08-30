/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import mockit.internal.expectations.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class LocalFieldTypeRedefinitions extends FieldTypeRedefinitions
{
   private final RecordAndReplayExecution execution;

   public LocalFieldTypeRedefinitions(Object objectWithMockFields, RecordAndReplayExecution execution)
   {
      super(objectWithMockFields);
      this.execution = execution;
   }

   public void redefineLocalFieldTypes()
   {
      redefineFieldTypes(parentObject.getClass(), false);
   }

   @Override
   protected void redefineTypeForMockField()
   {
      TypeRedefinition typeRedefinition = new TypeRedefinition(parentObject, typeMetadata);

      if (finalField) {
         typeRedefinition.redefineTypeForFinalField();
         registerMockedClassIfNonStrict();

         if (typeMetadata.getMaxInstancesToCapture() > 0) {
            execution.addMockedTypeToMatchOnInstance(typeRedefinition.targetClass);
         }

         TestRun.getExecutingTest().addFinalLocalMockField(parentObject, typeMetadata);
      }
      else {
         Object mock = typeRedefinition.redefineType().create();
         Utilities.setFieldValue(field, parentObject, mock);
         registerMock(mock);
      }

      execution.addLocalMock(typeMetadata.declaredType, parentObject);
      addTargetClass(typeMetadata.withInstancesToCapture(), typeRedefinition.targetClass);
   }

   @Override
   public boolean captureNewInstanceForApplicableMockField(Object mock)
   {
      return
         captureOfNewInstances != null &&
         getCaptureOfNewInstances().captureNewInstanceForApplicableMockField(parentObject, mock);
   }
}
