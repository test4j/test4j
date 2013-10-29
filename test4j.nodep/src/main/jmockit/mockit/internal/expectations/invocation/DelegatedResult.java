/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import mockit.*;
import mockit.internal.util.*;

final class DelegatedResult extends DynamicInvocationResult
{
   DelegatedResult(Delegate<?> delegate)
   {
      super(delegate, MethodReflection.findNonPrivateHandlerMethod(delegate));
   }

   @Override
   Object produceResult(
      Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
      throws Throwable
   {
      return invokeMethodOnTargetObject(invokedObject, invocation, constraints, args);
   }
}
