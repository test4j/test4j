/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.lang.reflect.*;
import java.util.*;

import mockit.*;
import mockit.internal.expectations.invocation.InvocationResult.*;
import mockit.internal.util.*;

public final class InvocationResults
{
   private final ExpectedInvocation invocation;
   private final InvocationConstraints constraints;
   private InvocationResult currentResult;
   private InvocationResult lastResult;
   private int resultCount;

   public InvocationResults(ExpectedInvocation invocation, InvocationConstraints constraints)
   {
      this.invocation = invocation;
      this.constraints = constraints;
   }

   public void addReturnValue(Object value)
   {
      InvocationResult result =
         value instanceof Delegate ? new DelegatedResult((Delegate<?>) value) : new ReturnValueResult(value);

      addResult(result);
   }

   public void addReturnValues(Object array)
   {
      int n = validateMultiValuedResult(array);

      for (int i = 0; i < n; i++) {
         Object value = Array.get(array, i);
         addReturnValue(value);
      }
   }

   private int validateMultiValuedResult(Object array)
   {
      int n = Array.getLength(array);

      if (n == 0) {
         reportInvalidReturnValue();
      }
      
      return n;
   }

   private void reportInvalidReturnValue()
   {
      Class<?> returnType = Utilities.getReturnType(invocation.getMethodNameAndDescription());
      throw new IllegalArgumentException("Invalid return value for method returning " + returnType);
   }

   public void addReturnValues(Iterable<?> values)
   {
      validateMultiValuedResult(values.iterator());

      for (Object value : values) {
         addReturnValue(value);
      }
   }

   private void validateMultiValuedResult(Iterator<?> values)
   {
      if (!values.hasNext()) {
         reportInvalidReturnValue();
      }
   }

   public void addReturnValues(Object... values)
   {
      for (Object value : values) {
         addReturnValue(value);
      }
   }

   public void addResults(Object array)
   {
      int n = validateMultiValuedResult(array);

      for (int i = 0; i < n; i++) {
         Object value = Array.get(array, i);
         addConsecutiveResult(value);
      }
   }

   private void addConsecutiveResult(Object result)
   {
      if (result instanceof Throwable) {
         addThrowable((Throwable) result);
      }
      else {
         addReturnValue(result);
      }
   }

   public void addResults(Iterable<?> values)
   {
      validateMultiValuedResult(values.iterator());

      for (Object value : values) {
         addConsecutiveResult(value);
      }
   }

   public void addDeferredReturnValues(Iterator<?> values)
   {
      validateMultiValuedResult(values);

      InvocationResult result = new DeferredReturnValues(values);
      addResult(result);
      constraints.setUnlimitedMaxInvocations();
   }

   public void addDeferredResults(Iterator<?> values)
   {
      validateMultiValuedResult(values);

      InvocationResult result = new DeferredResults(values);
      addResult(result);
      constraints.setUnlimitedMaxInvocations();
   }

   public void addThrowable(Throwable t)
   {
      addResult(new ThrowableResult(t));
   }

   public void addResult(InvocationResult result)
   {
      resultCount++;
      constraints.adjustMaxInvocations(resultCount);

      if (currentResult == null) {
         currentResult = result;
         lastResult = result;
      }
      else {
         lastResult.next = result;
         lastResult = result;
      }
   }

   public Object produceResult(Object invokedObject, Object[] invocationArgs) throws Throwable
   {
      InvocationResult resultToBeProduced = currentResult;
      InvocationResult nextResult = resultToBeProduced.next;

      if (nextResult != null) {
         currentResult = nextResult;
      }

      Object result = resultToBeProduced.produceResult(invokedObject, invocation, constraints, invocationArgs);

      return result;
   }
}
