/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

public final class AlwaysTrueMatcher implements ArgumentMatcher
{
   public static final ArgumentMatcher INSTANCE = new AlwaysTrueMatcher();

   private AlwaysTrueMatcher() {}
   public boolean matches(Object argValue) { return true; }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("any ").append(argumentMismatch.getParameterType());
   }
}
