/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.lang.reflect.*;

import mockit.*;
import mockit.external.asm4.Type;
import mockit.internal.util.*;

final class DelegatedResult extends DynamicInvocationResult
{
   DelegatedResult(Delegate<?> delegate)
   {
      super(delegate, findDelegateMethodIfSingle(delegate));
   }

   private static Method findDelegateMethodIfSingle(Delegate<?> delegate)
   {
      Method[] declaredMethods = delegate.getClass().getDeclaredMethods();
      return declaredMethods.length == 1 ? declaredMethods[0] : null;
   }

   @Override
   Object produceResult(
      Object invokedObject, ExpectedInvocation invocation, InvocationConstraints constraints, Object[] args)
      throws Throwable
   {
      if (methodToInvoke == null) {
         String methodName = adaptNameAndArgumentsForDelegate(invocation, args);
         methodToInvoke = Utilities.findCompatibleMethod(targetObject.getClass(), methodName, args);
         determineWhetherMethodToInvokeHasInvocationParameter();
      }

      return invokeMethodOnTargetObject(invokedObject, invocation, constraints, args);
   }

   private String adaptNameAndArgumentsForDelegate(ExpectedInvocation invocation, Object[] args)
   {
      String methodNameAndDesc = invocation.getMethodNameAndDescription();
      int leftParen = methodNameAndDesc.indexOf('(');

      replaceNullArgumentsWithClassObjectsIfAny(methodNameAndDesc, leftParen, args);

      String methodName = methodNameAndDesc.substring(0, leftParen);

      if ("<init>".equals(methodName)) {
         methodName = "$init";
      }

      return methodName;
   }

   private void replaceNullArgumentsWithClassObjectsIfAny(String methodNameAndDesc, int leftParen, Object[] args)
   {
      Type[] argTypes = null;

      for (int i = 0; i < args.length; i++) {
         if (args[i] == null) {
            if (argTypes == null) {
               String methodDesc = methodNameAndDesc.substring(leftParen);
               argTypes = Type.getArgumentTypes(methodDesc);
            }

            args[i] = Utilities.getClassForType(argTypes[i]);
         }
      }
   }
}
