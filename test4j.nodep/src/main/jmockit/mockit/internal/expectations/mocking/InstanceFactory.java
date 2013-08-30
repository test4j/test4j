/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;

import sun.reflect.*;

import mockit.internal.state.*;
import mockit.internal.util.*;

public abstract class InstanceFactory
{
   protected Object lastInstance;

   public abstract Object create();

   public final Object getLastInstance() { return lastInstance; }
   public final void clearLastInstance() { lastInstance = null; }

   static final class InterfaceInstanceFactory extends InstanceFactory
   {
      private final Object emptyProxy;

      InterfaceInstanceFactory(Object emptyProxy) { this.emptyProxy = emptyProxy; }

      @Override
      public Object create() { lastInstance = emptyProxy; return emptyProxy; }
   }

   @SuppressWarnings("UseOfSunClasses")
   static final class ClassInstanceFactory extends InstanceFactory
   {
      private static final ReflectionFactory REFLECTION_FACTORY = ReflectionFactory.getReflectionFactory();
      private static final Constructor<?> OBJECT_CONSTRUCTOR;
      static
      {
         try { OBJECT_CONSTRUCTOR = Object.class.getConstructor(); }
         catch (NoSuchMethodException e) { throw new RuntimeException(e); }
      }

      private final Constructor<?> fakeConstructor;

      ClassInstanceFactory(Class<?> concreteClass)
      {
         fakeConstructor = REFLECTION_FACTORY.newConstructorForSerialization(concreteClass, OBJECT_CONSTRUCTOR);
      }

      @Override
      public Object create()
      {
         TestRun.exitNoMockingZone();

         try {
            lastInstance = fakeConstructor.newInstance();
            return lastInstance;
         }
         catch (NoClassDefFoundError e) {
            StackTrace.filterStackTrace(e);
            e.printStackTrace();
            throw e;
         }
         catch (ExceptionInInitializerError e) {
            StackTrace.filterStackTrace(e);
            e.printStackTrace();
            throw e;
         }
         catch (InstantiationException e) { throw new RuntimeException(e); }
         catch (IllegalAccessException e) { throw new RuntimeException(e); }
         catch (InvocationTargetException e) { throw new RuntimeException(e.getCause()); }
         finally {
            TestRun.enterNoMockingZone();
         }
      }
   }

   static final class EnumInstanceFactory extends InstanceFactory
   {
      private final Object anEnumValue;

      EnumInstanceFactory(Class<?> enumClass) { anEnumValue = enumClass.getEnumConstants()[0]; }

      @Override
      public Object create() { lastInstance = anEnumValue; return anEnumValue; }
   }
}
