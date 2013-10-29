/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.lang.reflect.*;
import java.util.*;

import mockit.external.asm4.Type;

public final class ParameterNames
{
   private static final Map<String, Map<String, String[]>> classesToMethodsToParameters =
      new HashMap<String, Map<String, String[]>>();
   private static final String[] NO_PARAMETERS = new String[0];

   public static boolean hasNamesForClass(String classDesc)
   {
      return classesToMethodsToParameters.containsKey(classDesc);
   }

   public static void registerName(
      String classDesc, int methodAccess, String methodName, String methodDesc, String name, int index)
   {
      if ("this".equals(name)) {
         return;
      }

      Map<String, String[]> methodsToParameters = classesToMethodsToParameters.get(classDesc);

      if (methodsToParameters == null) {
         methodsToParameters = new HashMap<String, String[]>();
         classesToMethodsToParameters.put(classDesc, methodsToParameters);
      }

      String methodKey = methodName + methodDesc;
      String[] parameterNames = methodsToParameters.get(methodKey);

      if (parameterNames == null) {
         int numParameters = Type.getArgumentTypes(methodDesc).length;
         parameterNames = numParameters == 0 ? NO_PARAMETERS : new String[numParameters];
         methodsToParameters.put(methodKey, parameterNames);
      }

      if (!Modifier.isStatic(methodAccess)) {
         index--;
      }

      if (index < parameterNames.length) {
         parameterNames[index] = name;
      }
   }

   public static String getName(String classDesc, String methodDesc, int index)
   {
      Map<String, String[]> methodsToParameters = classesToMethodsToParameters.get(classDesc);

      if (methodsToParameters == null) {
         return null;
      }

      String[] parameterNames = methodsToParameters.get(methodDesc);
      return parameterNames == null ? null : parameterNames[index];
   }
}
