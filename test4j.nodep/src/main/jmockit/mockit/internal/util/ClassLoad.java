/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.util.*;
import java.util.concurrent.*;

import mockit.internal.state.*;

public final class ClassLoad
{
   private static final ClassLoader THIS_CL = ClassLoad.class.getClassLoader();
   private static final Map<String, Class<?>> LOADED_CLASSES = new ConcurrentHashMap<String, Class<?>>();

   public static void registerLoadedClass(Class<?> aClass)
   {
      LOADED_CLASSES.put(aClass.getName(), aClass);
   }

   public static <T> Class<T> loadByInternalName(String internalClassName)
   {
      return loadClass(internalClassName.replace('/', '.'));
   }

   public static <T> Class<T> loadClass(String className)
   {
      Class<?> loadedClass = LOADED_CLASSES.get(className);

      try {
         if (loadedClass == null) {
            loadedClass = loadClass(THIS_CL, className);

            if (loadedClass == null) {
               Class<?> testClass = TestRun.getCurrentTestClass();
               loadedClass = testClass == null ? null : loadClass(testClass.getClassLoader(), className);

               if (loadedClass == null) {
                  loadedClass = loadClass(Thread.currentThread().getContextClassLoader(), className);

                  if (loadedClass == null) {
                     throw new IllegalArgumentException("No class with name \"" + className + "\" found");
                  }
               }
            }
         }
      }
      catch (LinkageError e) {
         e.printStackTrace();
         throw e;
      }

      //noinspection unchecked
      return (Class<T>) loadedClass;
   }

   private static Class<?> loadClass(ClassLoader loader, String className)
   {
      try { return Class.forName(className, true, loader); } catch (ClassNotFoundException ignore) { return null; }
   }
}
