/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.jar.*;
import java.util.logging.*;

import mockit.internal.util.*;

public abstract class MockingBridge extends Logger implements InvocationHandler
{
   private static final Object[] EMPTY_ARGS = {};
   private static final ReentrantLock LOCK = new ReentrantLock();

   public static void preventEventualClassLoadingConflicts()
   {
      // Pre-load certain JMockit classes to avoid NoClassDefFoundError's or re-entrancy loops during class loading
      // when certain JRE classes are mocked, such as ArrayList or Thread.
      try {
         Class.forName("mockit.Capturing");
         Class.forName("mockit.Delegate");
         Class.forName("mockit.Invocation");
         Class.forName("mockit.internal.RedefinitionEngine");
         Class.forName("mockit.internal.util.GeneratedClasses");
         Class.forName("mockit.internal.util.MethodReflection");
         Class.forName("mockit.internal.util.ObjectMethods");
         Class.forName("mockit.internal.util.TypeDescriptor");
         Class.forName("mockit.internal.expectations.RecordAndReplayExecution");
         Class.forName("mockit.internal.expectations.invocation.InvocationResults");
         Class.forName("mockit.internal.expectations.invocation.MockedTypeCascade");
         Class.forName("mockit.internal.expectations.mocking.BaseTypeRedefinition$MockedClass");
         Class.forName("mockit.internal.expectations.mocking.SharedFieldTypeRedefinitions");
         Class.forName("mockit.internal.expectations.argumentMatching.EqualityMatcher");
      }
      catch (ClassNotFoundException ignore) {}

      wasCalledDuringClassLoading();
      DefaultValues.computeForReturnType("()J");
   }

   /**
    * The instance is stored in a place directly accessible through the Java SE API, so that it can
    * be recovered from any class loader.
    */
   protected MockingBridge(Class<? extends MockingBridge> subclass)
   {
      super("mockit." + subclass.hashCode(), null);
      LogManager.getLogManager().addLogger(this);
   }

   protected static boolean notToBeMocked(Object mocked, String mockedClassDesc)
   {
      return
         mocked == null && "java/lang/System".equals(mockedClassDesc) && wasCalledDuringClassLoading() ||
         mocked != null && instanceOfClassThatParticipatesInClassLoading(mocked) && wasCalledDuringClassLoading();
   }

   private static boolean instanceOfClassThatParticipatesInClassLoading(Object mocked)
   {
      Class<?> mockedClass = mocked.getClass();
      return
         mockedClass == File.class || mockedClass == URL.class || mockedClass == FileInputStream.class ||
         JarFile.class.isInstance(mocked) || JarEntry.class.isInstance(mocked) || mockedClass == Manifest.class ||
         Vector.class.isInstance(mocked) || Hashtable.class.isInstance(mocked);
   }

   protected static boolean wasCalledDuringClassLoading()
   {
      if (LOCK.isHeldByCurrentThread()) return true;
      LOCK.lock();

      try {
         StackTrace st = new StackTrace(new Throwable());
         int n = st.getDepth();

         for (int i = 3; i < n; i++) {
            StackTraceElement ste = st.getElement(i);

            if ("ClassLoader.java".equals(ste.getFileName()) && "loadClass".equals(ste.getMethodName())) {
               return true;
            }
         }

         return false;
      }
      finally {
         LOCK.unlock();
      }
   }

   protected static Object[] extractMockArguments(Object[] args)
   {
      int i = 7;

      if (args.length > i) {
         Object[] mockArgs = new Object[args.length - i];
         System.arraycopy(args, i, mockArgs, 0, mockArgs.length);
         return mockArgs;
      }

      return EMPTY_ARGS;
   }
}
