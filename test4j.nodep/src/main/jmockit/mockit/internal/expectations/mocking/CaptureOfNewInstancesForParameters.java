/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

public final class CaptureOfNewInstancesForParameters extends CaptureOfNewInstances
{
   CaptureOfNewInstancesForParameters() {}

   public boolean captureNewInstanceForApplicableMockParameter(Object mock)
   {
      return captureNewInstance(null, mock);
   }
}
