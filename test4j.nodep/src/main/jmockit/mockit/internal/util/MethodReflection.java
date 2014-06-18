/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;

import static mockit.internal.util.ParameterReflection.*;

public final class MethodReflection
{
   public static <T> T invoke(
      Class<?> theClass, Object targetInstance, String methodName, Class<?>[] paramTypes, Object... methodArgs)
   {
      Method method = findSpecifiedMethod(theClass, methodName, paramTypes);
      T result = invoke(targetInstance, method, methodArgs);
      return result;
   }

   private static Method findSpecifiedMethod(Class<?> theClass, String methodName, Class<?>[] paramTypes)
   {
      for (Method declaredMethod : theClass.getDeclaredMethods()) {
         if (declaredMethod.getName().equals(methodName)) {
            Class<?>[] declaredParameterTypes = declaredMethod.getParameterTypes();
            int firstRealParameter = indexOfFirstRealParameter(declaredParameterTypes, paramTypes);

            if (
               firstRealParameter >= 0 &&
               matchesParameterTypes(declaredMethod.getParameterTypes(), paramTypes, firstRealParameter)
            ) {
               return declaredMethod;
            }
         }
      }

      Class<?> superClass = theClass.getSuperclass();

      if (superClass != null && superClass != Object.class) {
         return findSpecifiedMethod(superClass, methodName, paramTypes);
      }

      String paramTypesDesc = getParameterTypesDescription(paramTypes);
      throw new IllegalArgumentException("Specified method not found: " + methodName + paramTypesDesc);
   }

   public static <T> T invokeWithCheckedThrows(
      Class<?> theClass, Object targetInstance, String methodName, Class<?>[] paramTypes, Object... methodArgs)
      throws Throwable
   {
      Method method = findSpecifiedMethod(theClass, methodName, paramTypes);
      T result = invokeWithCheckedThrows(targetInstance, method, methodArgs);
      return result;
   }

   public static <T> T invoke(Object targetInstance, Method method, Object... methodArgs)
   {
      Utilities.ensureThatMemberIsAccessible(method);

      try {
         //noinspection unchecked
         return (T) method.invoke(targetInstance, methodArgs);
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
      catch (IllegalArgumentException e) {
         StackTrace.filterStackTrace(e);
         throw new IllegalArgumentException("Failure to invoke method: " + method, e);
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

   public static <T> T invokeWithCheckedThrows(Object targetInstance, Method method, Object... methodArgs)
      throws Throwable
   {
      Utilities.ensureThatMemberIsAccessible(method);

      try {
         //noinspection unchecked
         return (T) method.invoke(targetInstance, methodArgs);
      }
      catch (IllegalArgumentException e) {
         StackTrace.filterStackTrace(e);
         throw new IllegalArgumentException("Failure to invoke method: " + method, e);
      }
      catch (InvocationTargetException e) {
         throw e.getCause();
      }
   }

   public static <T> T invoke(Class<?> theClass, Object targetInstance, String methodName, Object... methodArgs)
   {
      boolean staticMethod = targetInstance == null;
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(methodArgs);
      Method method = staticMethod ?
         findCompatibleStaticMethod(theClass, methodName, argTypes) :
         findCompatibleMethod(theClass, methodName, argTypes);

      if (staticMethod && !isStatic(method.getModifiers())) {
         throw new IllegalArgumentException(
            "Attempted to invoke non-static method without an instance to invoke it on");
      }

      T result = invoke(targetInstance, method, methodArgs);
      return result;
   }

   private static Method findCompatibleStaticMethod(Class<?> theClass, String methodName, Class<?>[] argTypes)
   {
      Method methodFound = findCompatibleMethodInClass(theClass, methodName, argTypes);

      if (methodFound != null) {
         return methodFound;
      }

      String argTypesDesc = getParameterTypesDescription(argTypes);
      throw new IllegalArgumentException("No compatible static method found: " + methodName + argTypesDesc);
   }

   public static Method findCompatibleMethod(Class<?> theClass, String methodName, Class<?>[] argTypes)
   {
      Method methodFound = null;

      while (true) {
         Method compatibleMethod = findCompatibleMethodInClass(theClass, methodName, argTypes);

         if (
            compatibleMethod != null &&
            (methodFound == null ||
             hasMoreSpecificTypes(compatibleMethod.getParameterTypes(), methodFound.getParameterTypes()))
         ) {
            methodFound = compatibleMethod;
         }

         Class<?> superClass = theClass.getSuperclass();

         if (superClass == null || superClass == Object.class) {
            break;
         }

         //noinspection AssignmentToMethodParameter
         theClass = superClass;
      }

      if (methodFound != null) {
         return methodFound;
      }

      String argTypesDesc = getParameterTypesDescription(argTypes);
      throw new IllegalArgumentException("No compatible method found: " + methodName + argTypesDesc);
   }

   private static Method findCompatibleMethodInClass(Class<?> theClass, String methodName, Class<?>[] argTypes)
   {
      Method found = null;
      Class<?>[] foundParamTypes = null;

      for (Method declaredMethod : theClass.getDeclaredMethods()) {
         if (declaredMethod.getName().equals(methodName)) {
            Class<?>[] declaredParamTypes = declaredMethod.getParameterTypes();
            int firstRealParameter = indexOfFirstRealParameter(declaredParamTypes, argTypes);

            if (
               firstRealParameter >= 0 &&
               (matchesParameterTypes(declaredParamTypes, argTypes, firstRealParameter) ||
                acceptsArgumentTypes(declaredParamTypes, argTypes, firstRealParameter)) &&
               (found == null || hasMoreSpecificTypes(declaredParamTypes, foundParamTypes))
            ) {
               found = declaredMethod;
               foundParamTypes = declaredParamTypes;
            }
         }
      }

      return found;
   }

   public static Method findNonPrivateHandlerMethod(Object handler)
   {
      Class<?> handlerClass = handler.getClass();
      Method nonPrivateMethod;

      do {
         nonPrivateMethod = findNonPrivateHandlerMethod(handlerClass);

         if (nonPrivateMethod != null) {
            break;
         }

         handlerClass = handlerClass.getSuperclass();
      }
      while (handlerClass != null && handlerClass != Object.class);

      if (nonPrivateMethod == null) {
         throw new IllegalArgumentException("No non-private invocation handler method found");
      }

      return nonPrivateMethod;
   }

   private static Method findNonPrivateHandlerMethod(Class<?> handlerClass)
   {
      Method[] declaredMethods = handlerClass.getDeclaredMethods();
      Method found = null;

      for (Method declaredMethod : declaredMethods) {
         if (!isPrivate(declaredMethod.getModifiers())) {
            if (found != null) {
               throw new IllegalArgumentException("More than one non-private invocation handler method found");
            }

            found = declaredMethod;
         }
      }

      return found;
   }
}
