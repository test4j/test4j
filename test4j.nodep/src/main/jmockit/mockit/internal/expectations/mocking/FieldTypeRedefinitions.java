/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;

import static mockit.external.asm4.Opcodes.*;

import mockit.*;
import mockit.internal.state.*;

public abstract class FieldTypeRedefinitions extends TypeRedefinitions
{
   private static final int FIELD_ACCESS_MASK = ACC_SYNTHETIC + ACC_STATIC;

   protected Field field;
   protected boolean finalField;

   protected FieldTypeRedefinitions(Object objectWithMockFields)
   {
      super(objectWithMockFields);
   }

   protected final void redefineFieldTypes(Class<?> classWithMockFields, boolean isTestClass)
   {
      Class<?> superClass = classWithMockFields.getSuperclass();

      //noinspection UnnecessaryFullyQualifiedName
      if (
         superClass != null && superClass != Object.class &&
         superClass != mockit.Expectations.class && superClass != mockit.NonStrictExpectations.class
      ) {
         redefineFieldTypes(superClass, isTestClass);
      }

      Field[] fields = classWithMockFields.getDeclaredFields();
      DefaultResults defaultResults = TestRun.getExecutingTest().defaultResults;

      for (Field candidateField : fields) {
         int fieldModifiers = candidateField.getModifiers();

         if ((fieldModifiers & FIELD_ACCESS_MASK) == 0) {
            if (candidateField.isAnnotationPresent(Input.class)) {
               defaultResults.add(candidateField, parentObject);
            }
            else {
               field = candidateField;
               redefineFieldType(isTestClass, fieldModifiers);
               field = null;
            }
         }
      }
   }

   private void redefineFieldType(boolean fromTestClass, int modifiers)
   {
      typeMetadata = new MockedType(field, fromTestClass);

      if (typeMetadata.isMockField()) {
         finalField = isFinal(modifiers);

         redefineTypeForMockField();
         typesRedefined++;

         registerCaptureOfNewInstances();
      }

      typeMetadata = null;
   }

   protected abstract void redefineTypeForMockField();

   protected final void registerMockedClassIfNonStrict()
   {
      if (typeMetadata.nonStrict) {
         TestRun.getExecutingTest().addNonStrictMock(typeMetadata.getClassType());
      }
   }

   @Override
   public final CaptureOfNewInstancesForFields getCaptureOfNewInstances()
   {
      return (CaptureOfNewInstancesForFields) captureOfNewInstances;
   }

   private void registerCaptureOfNewInstances()
   {
      if (typeMetadata.getMaxInstancesToCapture() <= 0) {
         return;
      }

      if (captureOfNewInstances == null) {
         captureOfNewInstances = new CaptureOfNewInstancesForFields();
      }

      getCaptureOfNewInstances().registerCaptureOfNewInstances(typeMetadata, null);
   }

   /**
    * Returns true iff the mock instance concrete class is not mocked in some test, ie it's a class
    * which only appears in the code under test.
    */
   public abstract boolean captureNewInstanceForApplicableMockField(Object mock);
}
