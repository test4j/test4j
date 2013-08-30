/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.util.*;

import mockit.external.asm4.*;

public final class ParameterNames
{
   private static final Map<String, Map<String, String[]>> classesToMethodsToParameters =
      new HashMap<String, Map<String, String[]>>();

   public static boolean hasNamesForClass(String classDesc)
   {
      return classesToMethodsToParameters.containsKey(classDesc);
   }

   public static void registerName(String classDesc, String methodName, String methodDesc, String name)
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
         parameterNames = new String[Type.getArgumentTypes(methodDesc).length];
         methodsToParameters.put(methodKey, parameterNames);
      }

      for (int i = 0; i < parameterNames.length; i++) {
         if (parameterNames[i] == null) {
            parameterNames[i] = name;
            break;
         }
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
