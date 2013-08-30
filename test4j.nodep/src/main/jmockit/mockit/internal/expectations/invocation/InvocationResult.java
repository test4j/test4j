/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.util.*;

abstract class InvocationResult
{
   InvocationResult next;

   abstract Object produceResult(
      Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
      throws Throwable;

   static final class ReturnValueResult extends InvocationResult
   {
      private final Object returnValue;

      ReturnValueResult(Object returnValue) { this.returnValue = returnValue; }

      @Override
      Object produceResult(
         Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
      {
         return returnValue;
      }
   }

   static final class ThrowableResult extends InvocationResult
   {
      private final Throwable throwable;

      ThrowableResult(Throwable throwable) { this.throwable = throwable; }

      @Override
      Object produceResult(
         Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
         throws Throwable
      {
         throwable.fillInStackTrace();
         throw throwable;
      }
   }

   static final class DeferredReturnValues extends InvocationResult
   {
      private final Iterator<?> values;

      DeferredReturnValues(Iterator<?> values) { this.values = values; }

      @Override
      Object produceResult(
         Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
         throws Throwable
      {
         return values.hasNext() ? values.next() : null;
      }
   }

   static final class DeferredResults extends InvocationResult
   {
      private final Iterator<?> values;

      DeferredResults(Iterator<?> values) { this.values = values; }

      @Override
      Object produceResult(
         Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
         throws Throwable
      {
         Object nextValue = values.hasNext() ? values.next() : null;

         if (nextValue instanceof Throwable) {
            Throwable t = (Throwable) nextValue;
            t.fillInStackTrace();
            throw t;
         }

         return nextValue;
      }
   }
}
