/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.util.*;

import mockit.internal.capturing.*;
import mockit.internal.state.*;

class TypeRedefinitions
{
   protected final Object parentObject;
   protected MockedType typeMetadata;
   protected int typesRedefined;
   private final List<Class<?>> targetClasses;
   protected CaptureOfImplementations captureOfNewInstances;

   protected TypeRedefinitions(Object parentObject)
   {
      this.parentObject = parentObject;
      targetClasses = new ArrayList<Class<?>>(2);
   }

   protected final void addTargetClass(boolean withInstancesToCapture, Class<?> targetClass)
   {
      if (!withInstancesToCapture || !targetClasses.contains(targetClass)) {
         targetClasses.add(targetClass);
      }
   }

   public final int getTypesRedefined() { return typesRedefined; }
   public final List<Class<?>> getTargetClasses() { return targetClasses; }
   public CaptureOfImplementations getCaptureOfNewInstances() { return captureOfNewInstances; }

   protected final void registerMock(Object mock)
   {
      TestRun.getExecutingTest().registerMock(typeMetadata, mock);
   }

   protected final void clearTargetClasses() { targetClasses.clear(); }

   public void cleanUp()
   {
      if (captureOfNewInstances != null) {
         captureOfNewInstances.cleanUp();
         captureOfNewInstances = null;
      }
   }
}
