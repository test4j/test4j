/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import java.lang.reflect.*;

import mockit.internal.util.*;

public final class ReflectiveMatcher implements ArgumentMatcher
{
   private final Object handlerObject;
   private Method handlerMethod;
   private Object matchedValue;

   public ReflectiveMatcher(Object handlerObject) { this.handlerObject = handlerObject; }

   public boolean matches(Object argValue)
   {
      if (handlerMethod == null) {
         handlerMethod = MethodReflection.findNonPrivateHandlerMethod(handlerObject);
      }

      matchedValue = argValue;
      Boolean result = MethodReflection.invoke(handlerObject, handlerMethod, argValue);

      return result == null || result;
   }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      if (handlerMethod != null) {
         argumentMismatch.append(handlerMethod.getName()).append('(');
         argumentMismatch.appendFormatted(matchedValue);
         argumentMismatch.append(") (should return true, was false)");
      }
      else {
         argumentMismatch.append('?');
      }

      argumentMismatch.markAsFinished();
   }
}
