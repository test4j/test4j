/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.util.*;

final class CaptureOfNewInstancesForFields extends CaptureOfNewInstances
{
   boolean captureNewInstanceForApplicableMockField(Object fieldOwner, Object mock)
   {
      boolean constructorModifiedForCaptureOnly = captureNewInstance(fieldOwner, mock);

      if (captureFound != null) {
         Field mockField = captureFound.typeMetadata.field;
         Utilities.setFieldValue(mockField, fieldOwner, mock);
      }

      return constructorModifiedForCaptureOnly;
   }

   void resetCaptureCount(Field mockField)
   {
      for (List<Capture> fieldsWithCapture : baseTypeToCaptures.values()) {
         resetCaptureCount(mockField, fieldsWithCapture);
      }
   }

   private void resetCaptureCount(Field mockField, List<Capture> fieldsWithCapture)
   {
      for (Capture fieldWithCapture : fieldsWithCapture) {
         if (fieldWithCapture.typeMetadata.field == mockField) {
            fieldWithCapture.reset();
         }
      }
   }
}
