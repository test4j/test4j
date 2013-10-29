/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;
import java.util.*;

public final class ParameterReflection
{
   static final Class<?>[] NO_PARAMETERS = new Class<?>[0];

   static String getParameterTypesDescription(Type[] paramTypes)
   {
      String paramTypesDesc = Arrays.asList(paramTypes).toString();
      return paramTypesDesc.replace("class ", "").replace('[', '(').replace(']', ')');
   }

   public static Class<?>[] getArgumentTypesFromArgumentValues(Object... args)
   {
      if (args.length == 0) {
         return NO_PARAMETERS;
      }

      Class<?>[] argTypes = new Class<?>[args.length];

      for (int i = 0; i < args.length; i++) {
         argTypes[i] = getArgumentTypeFromArgumentValue(i, args);
      }

      return argTypes;
   }

   private static Class<?> getArgumentTypeFromArgumentValue(int i, Object[] args)
   {
      Object arg = args[i];

      if (arg == null) {
         throw new IllegalArgumentException("Invalid null value passed as argument " + i);
      }

      Class<?> argType;

      if (arg instanceof Class<?>) {
         argType = (Class<?>) arg;
         args[i] = null;
      }
      else {
         argType = GeneratedClasses.getMockedClass(arg);
      }

      return argType;
   }

   public static Object[] argumentsWithExtraFirstValue(Object[] args, Object firstValue)
   {
      Object[] args2 = new Object[1 + args.length];
      args2[0] = firstValue;
      System.arraycopy(args, 0, args2, 1, args.length);
      return args2;
   }

   static boolean hasMoreSpecificTypes(Class<?>[] currentTypes, Class<?>[] previousTypes)
   {
      for (int i = 0; i < currentTypes.length; i++) {
         Class<?> current = wrappedIfPrimitive(currentTypes[i]);
         Class<?> previous = wrappedIfPrimitive(previousTypes[i]);

         if (current != previous && previous.isAssignableFrom(current)) {
            return true;
         }
      }

      return false;
   }

   private static Class<?> wrappedIfPrimitive(Class<?> parameterType)
   {
      return parameterType.isPrimitive() ? AutoBoxing.getWrapperType(parameterType) : parameterType;
   }

   static boolean acceptsArgumentTypes(Class<?>[] paramTypes, Class<?>[] argTypes, int firstParameter)
   {
      for (int i = firstParameter; i < paramTypes.length; i++) {
         Class<?> parType = paramTypes[i];
         Class<?> argType = argTypes[i - firstParameter];

         if (isSameTypeIgnoringAutoBoxing(parType, argType) || parType.isAssignableFrom(argType)) {
            // OK, move to next parameter.
         }
         else {
            return false;
         }
      }

      return true;
   }

   static boolean isSameTypeIgnoringAutoBoxing(Class<?> firstType, Class<?> secondType)
   {
      return
         firstType == secondType ||
         firstType.isPrimitive() && isWrapperOfPrimitiveType(firstType, secondType) ||
         secondType.isPrimitive() && isWrapperOfPrimitiveType(secondType, firstType);
   }

   private static boolean isWrapperOfPrimitiveType(Class<?> primitiveType, Class<?> otherType)
   {
      return primitiveType == AutoBoxing.getPrimitiveType(otherType);
   }

   static int indexOfFirstRealParameter(Class<?>[] mockParameterTypes, Class<?>[] realParameterTypes)
   {
      int extraParameters = mockParameterTypes.length - realParameterTypes.length;

      if (extraParameters == 1) {
         //noinspection UnnecessaryFullyQualifiedName
         return mockParameterTypes[0] == mockit.Invocation.class ? 1 : -1;
      }
      else if (extraParameters != 0) {
         return -1;
      }

      return 0;
   }

   static boolean matchesParameterTypes(Class<?>[] declaredTypes, Class<?>[] specifiedTypes, int firstParameter)
   {
      for (int i = firstParameter; i < declaredTypes.length; i++) {
         Class<?> declaredType = declaredTypes[i];
         Class<?> specifiedType = specifiedTypes[i - firstParameter];

         if (isSameTypeIgnoringAutoBoxing(declaredType, specifiedType)) {
            // OK, move to next parameter.
         }
         else {
            return false;
         }
      }

      return true;
   }
}
