/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;
import java.util.*;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class DynamicPartialMocking
{
   public final List<Object> targetInstances;
   private final Map<Class<?>, byte[]> modifiedClassfiles;
   private final boolean nonStrict;

   public DynamicPartialMocking(boolean nonStrict)
   {
      targetInstances = new ArrayList<Object>(2);
      modifiedClassfiles = new HashMap<Class<?>, byte[]>();
      this.nonStrict = nonStrict;
   }

   public void redefineTypes(Object[] classesOrInstancesToBePartiallyMocked)
   {
      for (Object classOrInstance : classesOrInstancesToBePartiallyMocked) {
         redefineTargetType(classOrInstance);
      }

      new RedefinitionEngine().redefineMethods(modifiedClassfiles);
      modifiedClassfiles.clear();
   }

   private void redefineTargetType(Object classOrInstance)
   {
      Class<?> targetClass;

      if (classOrInstance instanceof Class) {
         targetClass = (Class<?>) classOrInstance;
         validateTargetClassType(targetClass);
         registerAsMocked(targetClass);
         redefineClassAndItsSuperClasses(targetClass, false);
      }
      else {
         targetClass = classOrInstance.getClass();
         validateTargetClassType(targetClass);
         registerAsMocked(classOrInstance);
         redefineClassAndItsSuperClasses(targetClass, true);
         targetInstances.add(classOrInstance);
      }

      TestRun.mockFixture().registerMockedClass(targetClass);
   }

   private void validateTargetClassType(Class<?> targetClass)
   {
      if (
         targetClass.isInterface() || targetClass.isAnnotation() || targetClass.isArray() ||
         targetClass.isPrimitive() || Utilities.isWrapperOfPrimitiveType(targetClass) ||
         Utilities.isGeneratedImplementationClass(targetClass)
      ) {
         throw new IllegalArgumentException("Invalid type for dynamic mocking: " + targetClass);
      }
   }

   public void registerAsMocked(Class<?> mockedClass)
   {
      if (nonStrict) {
         ExecutingTest executingTest = TestRun.getExecutingTest();

         do {
            executingTest.registerAsNonStrictlyMocked(mockedClass);

            //noinspection AssignmentToMethodParameter
            mockedClass = mockedClass.getSuperclass();
         }
         while (mockedClass != null && mockedClass != Object.class && mockedClass != Proxy.class);
      }
   }

   private void registerAsMocked(Object mock)
   {
      if (nonStrict) {
         TestRun.getExecutingTest().registerAsNonStrictlyMocked(mock);
      }
   }

   private void redefineClassAndItsSuperClasses(Class<?> realClass, boolean methodsOnly)
   {
      redefineClass(realClass, methodsOnly);
      Class<?> superClass = realClass.getSuperclass();

      if (superClass != null && superClass != Object.class && superClass != Proxy.class) {
         redefineClassAndItsSuperClasses(superClass, methodsOnly);
      }
   }

   private void redefineClass(Class<?> realClass, boolean methodsOnly)
   {
      ClassReader classReader = new ClassFile(realClass, false).getReader();

      ExpectationsModifier modifier = new ExpectationsModifier(realClass.getClassLoader(), classReader, null);
      modifier.useDynamicMocking(methodsOnly);

      classReader.accept(modifier, 0);
      byte[] modifiedClass = modifier.toByteArray();

      modifiedClassfiles.put(realClass, modifiedClass);
   }
}
