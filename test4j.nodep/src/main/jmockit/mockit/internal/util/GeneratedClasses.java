/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;

public final class GeneratedClasses
{
   public static final String SUBCLASS_PREFIX = "$Subclass_";
   public static final String IMPLCLASS_PREFIX = "$Impl_";

   public static String getNameForGeneratedClass(Class<?> aClass)
   {
      return getNameForGeneratedClass(aClass, aClass.getSimpleName());
   }

   public static String getNameForGeneratedClass(Class<?> aClass, String suffix)
   {
      String prefix = aClass.isInterface() ? IMPLCLASS_PREFIX : SUBCLASS_PREFIX;
      StringBuilder name = new StringBuilder(60).append(prefix).append(suffix);

      if (aClass.getClassLoader() != null) {
         Package targetPackage = aClass.getPackage();

         if (targetPackage != null && !targetPackage.isSealed()) {
            name.insert(0, '.').insert(0, targetPackage.getName());
         }
      }

      return name.toString();
   }

   public static boolean isGeneratedImplementationClass(Class<?> mockedType)
   {
      return Proxy.isProxyClass(mockedType) || isGeneratedImplementationClass(mockedType.getName());
   }

   private static boolean isGeneratedSubclass(String className)
   {
      return className.contains(SUBCLASS_PREFIX);
   }

   private static boolean isGeneratedImplementationClass(String className)
   {
      return className.contains(IMPLCLASS_PREFIX);
   }

   public static boolean isGeneratedClass(String className)
   {
      return isGeneratedSubclass(className) || isGeneratedImplementationClass(className);
   }

   public static Class<?> getMockedClassOrInterfaceType(Class<?> aClass)
   {
      if (isGeneratedImplementationClass(aClass)) {
         // Assumes that the proxy class implements a single interface.
         return aClass.getInterfaces()[0];
      }
      else if (isGeneratedSubclass(aClass.getName())) {
         return aClass.getSuperclass();
      }

      return aClass;
   }

   public static Class<?> getMockedClass(Object mock)
   {
      return getMockedClassOrInterfaceType(mock.getClass());
   }
}
