/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.lang.reflect.*;
import java.util.concurrent.*;

import mockit.Invocation;
import mockit.internal.state.*;

final class DelegateInvocation extends Invocation implements Callable<Method>
{
   private final InvocationArguments invocationArguments;
   boolean proceedIntoConstructor;

   DelegateInvocation(
      Object invokedInstance, Object[] invokedArguments,
      ExpectedInvocation expectedInvocation, InvocationConstraints constraints)
   {
      super(
         invokedInstance, invokedArguments,
         constraints.invocationCount, constraints.minInvocations, constraints.maxInvocations);
      invocationArguments = expectedInvocation.arguments;
   }

   /**
    * Returns the {@code Method} object corresponding to the mocked method, or {@code null} if it's a mocked
    * constructor.
    */
   public Method call()
   {
      if (invocationArguments.isForConstructor()) {
         proceedIntoConstructor = true;
         return null;
      }

      TestRun.getExecutingTest().markAsProceedingIntoRealImplementation();
      return invocationArguments.getRealMethod().method;
   }
}
