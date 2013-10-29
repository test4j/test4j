/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;

public final class RealMethod
{
   public final Method method;

   public RealMethod(Class<?> realClass, String methodName, String methodDesc)
   {
      method = initialize(realClass, methodName, methodDesc);
   }

   public RealMethod(String className, String methodNameAndDesc)
   {
      this(ClassLoad.loadClass(className), methodNameAndDesc);
   }

   public RealMethod(Class<?> realClass, String methodNameAndDesc)
   {
      int p = methodNameAndDesc.indexOf('(');
      String methodName = methodNameAndDesc.substring(0, p);
      String methodDesc = methodNameAndDesc.substring(p);
      method = initialize(realClass, methodName, methodDesc);
   }

   private Method initialize(Class<?> realClass, String methodName, String methodDesc)
   {
      Class<?>[] parameterTypes = TypeDescriptor.getParameterTypes(methodDesc);
      Class<?> ownerClass = realClass;

      while (true) {
         try {
            return ownerClass.getDeclaredMethod(methodName, parameterTypes);
         }
         catch (NoSuchMethodException e) {
            ownerClass = ownerClass.getSuperclass();

            if (ownerClass == Object.class) {
               throw new RuntimeException(e);
            }
         }
      }
   }
}
