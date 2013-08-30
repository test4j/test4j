/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.util.*;

import mockit.external.asm4.*;
import mockit.internal.capturing.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

class CaptureOfNewInstances extends CaptureOfImplementations
{
   static final class Capture
   {
      final MockedType typeMetadata;
      private Object originalMockInstance;
      private final List<Object> instancesCaptured;

      private Capture(MockedType typeMetadata, Object originalMockInstance)
      {
         this.typeMetadata = typeMetadata;
         this.originalMockInstance = originalMockInstance;
         instancesCaptured = new ArrayList<Object>(4);
      }

      private boolean isInstanceAlreadyCaptured(Object mock)
      {
         return Utilities.containsReference(instancesCaptured, mock);
      }

      private boolean captureInstance(Object fieldOwner, Object instance)
      {
         if (instancesCaptured.size() < typeMetadata.getMaxInstancesToCapture()) {
            if (fieldOwner != null && originalMockInstance == null) {
               originalMockInstance = Utilities.getFieldValue(typeMetadata.field, fieldOwner);
            }

            instancesCaptured.add(instance);
            return true;
         }

         return false;
      }

      void reset()
      {
         instancesCaptured.clear();
      }
   }

   final Map<Class<?>, List<Capture>> baseTypeToCaptures;
   private MockedType typeMetadata;
   Capture captureFound;

   CaptureOfNewInstances()
   {
      baseTypeToCaptures = new HashMap<Class<?>, List<Capture>>();
   }

   @Override
   public final ClassVisitor createModifier(ClassLoader cl, ClassReader cr, String baseTypeDesc)
   {
      ExpectationsModifier modifier = new ExpectationsModifier(cl, cr, typeMetadata);
      modifier.setClassNameForCapturedInstanceMethods(baseTypeDesc);

      if (typeMetadata.injectable) {
         modifier.useDynamicMockingForInstanceMethods(typeMetadata);
      }

      return modifier;
   }

   final void registerCaptureOfNewInstances(MockedType typeMetadata, Object mockInstance)
   {
      this.typeMetadata = typeMetadata;

      Class<?> baseType = typeMetadata.getClassType();

      if (!typeMetadata.isFinalFieldOrParameter()) {
         makeSureAllSubtypesAreModified(baseType, typeMetadata.fieldFromTestClass);
      }

      List<Capture> captures = baseTypeToCaptures.get(baseType);

      if (captures == null) {
         captures = new ArrayList<Capture>();
         baseTypeToCaptures.put(baseType, captures);
      }

      captures.add(new Capture(typeMetadata, mockInstance));
   }

   @Override
   protected final ClassSelector createClassSelector()
   {
      final String[] classNameFilters = typeMetadata.capturing.classNames();
      final boolean inverseFilters = typeMetadata.capturing.inverse();

      return new ClassSelector()
      {
         public boolean shouldCapture(ClassLoader definingClassLoader, String className)
         {
            if (classNameFilters == null || classNameFilters.length == 0) {
               return true;
            }

            for (String classNameRegex : classNameFilters) {
               if (className.matches(classNameRegex)) {
                  return !inverseFilters;
               }
            }

            return inverseFilters;
         }
      };
   }

   final boolean captureNewInstance(Object fieldOwner, Object mock)
   {
      captureFound = null;

      Class<?> mockedClass = mock.getClass();
      List<Capture> captures = baseTypeToCaptures.get(mockedClass);
      boolean constructorModifiedForCaptureOnly = captures == null;

      if (constructorModifiedForCaptureOnly) {
         captures = findCaptures(mockedClass);

         if (captures == null) {
            return false;
         }
      }

      for (Capture capture : captures) {
         if (capture.isInstanceAlreadyCaptured(mock)) {
            break;
         }
         else if (capture.captureInstance(fieldOwner, mock)) {
            captureFound = capture;
            break;
         }
      }

      if (typeMetadata.injectable) {
         if (captureFound != null) {
            TestRun.getExecutingTest().addCapturedInstanceForInjectableMock(captureFound.originalMockInstance, mock);
         }

         constructorModifiedForCaptureOnly = true;
      }
      else if (captureFound != null) {
         TestRun.getExecutingTest().addCapturedInstance(captureFound.originalMockInstance, mock);
      }

      return constructorModifiedForCaptureOnly;
   }

   private List<Capture> findCaptures(Class<?> mockedClass)
   {
      Class<?>[] interfaces = mockedClass.getInterfaces();

      for (Class<?> anInterface : interfaces) {
         List<Capture> found = baseTypeToCaptures.get(anInterface);

         if (found != null) {
            return found;
         }
      }

      Class<?> superclass = mockedClass.getSuperclass();

      if (superclass == Object.class) {
         return null;
      }

      List<Capture> found = baseTypeToCaptures.get(superclass);

      return found != null ? found : findCaptures(superclass);
   }

   @Override
   public final void cleanUp()
   {
      super.cleanUp();
      baseTypeToCaptures.clear();
   }
}