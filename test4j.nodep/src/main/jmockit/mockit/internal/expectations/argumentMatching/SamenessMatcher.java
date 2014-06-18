/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

public final class SamenessMatcher implements ArgumentMatcher
{
   private final Object object;

   public SamenessMatcher(Object object) { this.object = object; }

   public boolean matches(Object argValue) { return argValue == object; }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("same instance as ").appendFormatted(object);
   }
}
