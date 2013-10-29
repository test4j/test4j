/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.lang.reflect.*;
import java.util.concurrent.locks.*;

import mockit.*;
import mockit.internal.expectations.*;
import mockit.internal.util.*;

abstract class DynamicInvocationResult extends InvocationResult
{
   private static final Object[] NO_ARGS = {};

   private final Object targetObject;
   final Method methodToInvoke;
   private int numberOfRegularParameters;
   private boolean hasInvocationParameter;

   DynamicInvocationResult(Object targetObject, Method methodToInvoke)
   {
      this.targetObject = targetObject;
      this.methodToInvoke = methodToInvoke;
      determineWhetherMethodToInvokeHasInvocationParameter();
   }

   private void determineWhetherMethodToInvokeHasInvocationParameter()
   {
      Class<?>[] parameters = methodToInvoke.getParameterTypes();
      int n = parameters.length;
      hasInvocationParameter = n > 0 && parameters[0] == Invocation.class;
      numberOfRegularParameters = hasInvocationParameter ? n - 1 : n;
   }

   public final Object invokeMethodOnTargetObject(
      Object mockOrRealObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
   {
      Object[] delegateArgs = numberOfRegularParameters == 0 ? NO_ARGS : args;
      Object result;

      if (hasInvocationParameter) {
         result = invokeMethodWithContext(mockOrRealObject, invocation, constraints, args, delegateArgs);
      }
      else {
         result = executeMethodToInvoke(delegateArgs);
      }

      return result;
   }

   private Object invokeMethodWithContext(
      Object mockOrRealObject, ExpectedInvocation expectedInvocation, InvocationConstraints constraints,
      Object[] invokedArgs, Object[] delegateArgs)
   {
      DelegateInvocation invocation =
         new DelegateInvocation(mockOrRealObject, invokedArgs, expectedInvocation, constraints);
      Object[] delegateArgsWithInvocation = ParameterReflection.argumentsWithExtraFirstValue(delegateArgs, invocation);

      try {
         Object result = executeMethodToInvoke(delegateArgsWithInvocation);
         return invocation.proceedIntoConstructor ? Void.class : result;
      }
      finally {
         constraints.setLimits(invocation.getMinInvocations(), invocation.getMaxInvocations());
      }
   }

   protected final Object executeMethodToInvoke(Object[] args)
   {
      ReentrantLock reentrantLock = RecordAndReplayExecution.RECORD_OR_REPLAY_LOCK;

      if (!reentrantLock.isHeldByCurrentThread()) {
         return MethodReflection.invoke(targetObject, methodToInvoke, args);
      }

      reentrantLock.unlock();

      try {
         return MethodReflection.invoke(targetObject, methodToInvoke, args);
      }
      finally {
         //noinspection LockAcquiredButNotSafelyReleased
         reentrantLock.lock();
      }
   }
}