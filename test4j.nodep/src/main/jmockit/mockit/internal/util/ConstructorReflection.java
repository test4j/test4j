/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;

import static mockit.internal.util.ParameterReflection.*;

public final class ConstructorReflection
{
   public static <T> T newInstance(Class<T> aClass, Class<?>[] parameterTypes, Object... initArgs)
   {
      Constructor<T> constructor = findSpecifiedConstructor(aClass, parameterTypes);
      return invoke(constructor, initArgs);
   }

   private static <T> Constructor<T> findSpecifiedConstructor(Class<?> theClass, Class<?>[] paramTypes)
   {
      for (Constructor<?> declaredConstructor : theClass.getDeclaredConstructors()) {
         Class<?>[] declaredParameterTypes = declaredConstructor.getParameterTypes();
         int firstRealParameter = indexOfFirstRealParameter(declaredParameterTypes, paramTypes);

         if (
            firstRealParameter >= 0 &&
            matchesParameterTypes(declaredParameterTypes, paramTypes, firstRealParameter)
         ) {
            //noinspection unchecked
            return (Constructor<T>) declaredConstructor;
         }
      }

      String paramTypesDesc = getParameterTypesDescription(paramTypes);

      throw new IllegalArgumentException(
         "Specified constructor not found: " + theClass.getSimpleName() + paramTypesDesc);
   }

   public static <T> T invoke(Constructor<T> constructor, Object... initArgs)
   {
      Utilities.ensureThatMemberIsAccessible(constructor);

      Object[] args = initArgs != null? initArgs : getDefaultParameterValues(constructor);

      try {
         return constructor.newInstance(args);
      }
      catch (InstantiationException e) {
         throw new RuntimeException(e);
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
      catch (InvocationTargetException e) {
         Throwable cause = e.getCause();

         if (cause instanceof Error) {
            throw (Error) cause;
         }
         else if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
         }
         else {
            ThrowOfCheckedException.doThrow((Exception) cause);
            return null;
         }
      }
   }

   private static Object[] getDefaultParameterValues(Constructor<?> constructor)
   {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      int numParameters = parameterTypes.length;
      Object[] defaultArgs = new Object[numParameters];

      for (int i = 0; i < numParameters; i++) {
         Class<?> paramType = parameterTypes[i];
         defaultArgs[i] = DefaultValues.computeForType(paramType);
      }

      return defaultArgs;
   }

   public static <T> T newInstance(String className, Class<?>[] parameterTypes, Object... initArgs)
   {
      Class<T> theClass = ClassLoad.loadClass(className);
      return newInstance(theClass, parameterTypes, initArgs);
   }

   public static <T> T newInstance(String className, Object... nonNullArgs)
   {
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(nonNullArgs);
      Class<T> theClass = ClassLoad.loadClass(className);
      Constructor<T> constructor = findCompatibleConstructor(theClass, argTypes);
      return invoke(constructor, nonNullArgs);
   }

   private static <T> Constructor<T> findCompatibleConstructor(Class<?> theClass, Class<?>[] argTypes)
   {
      Constructor<T> found = null;
      Class<?>[] foundParameters = null;
      Constructor<?>[] declaredConstructors = theClass.getDeclaredConstructors();

      for (Constructor<?> declaredConstructor : declaredConstructors) {
         Class<?>[] declaredParamTypes = declaredConstructor.getParameterTypes();
         int firstRealParameter = indexOfFirstRealParameter(declaredParamTypes, argTypes);

         if (
            firstRealParameter >= 0 &&
            (matchesParameterTypes(declaredParamTypes, argTypes, firstRealParameter) ||
             acceptsArgumentTypes(declaredParamTypes, argTypes, firstRealParameter)) &&
            (found == null || hasMoreSpecificTypes(declaredParamTypes, foundParameters))
         ) {
            //noinspection unchecked
            found = (Constructor<T>) declaredConstructor;
            foundParameters = declaredParamTypes;
         }
      }

      if (found != null) {
         return found;
      }

      Class<?> declaringClass = theClass.getDeclaringClass();
      Class<?>[] paramTypes = declaredConstructors[0].getParameterTypes();

      if (paramTypes[0] == declaringClass && paramTypes.length > argTypes.length) {
         throw new IllegalArgumentException("Invalid instantiation of inner class; use newInnerInstance instead");
      }

      String argTypesDesc = getParameterTypesDescription(argTypes);
      throw new IllegalArgumentException("No compatible constructor found: " + theClass.getSimpleName() + argTypesDesc);
   }

   public static <T> T newInstance(Class<? extends T> aClass, Object... nonNullArgs)
   {
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(nonNullArgs);
      Constructor<T> constructor = findCompatibleConstructor(aClass, argTypes);
      return invoke(constructor, nonNullArgs);
   }

   public static <T> T newInstance(Class<T> aClass)
   {
      return newInstance(aClass, NO_PARAMETERS);
   }

   public static <T> T newInstanceUsingDefaultConstructor(Class<T> aClass)
   {
      try {
         //noinspection ClassNewInstance
         return aClass.newInstance();
      }
      catch (InstantiationException ie) {
         throw new RuntimeException(ie);
      }
      catch (IllegalAccessException ignore) {
         return newInstance(aClass);
      }
   }

   public static <T> T newInnerInstance(Class<? extends T> innerClass, Object outerInstance, Object... nonNullArgs)
   {
      Object[] initArgs = argumentsWithExtraFirstValue(nonNullArgs, outerInstance);
      return newInstance(innerClass, initArgs);
   }

   public static <T> T newInnerInstance(String innerClassName, Object outerInstance, Object... nonNullArgs)
   {
      String className = outerInstance.getClass().getName() + '$' + innerClassName;
      Class<T> innerClass = ClassLoad.loadClass(className);

      return newInnerInstance(innerClass, outerInstance, nonNullArgs);
   }
}
