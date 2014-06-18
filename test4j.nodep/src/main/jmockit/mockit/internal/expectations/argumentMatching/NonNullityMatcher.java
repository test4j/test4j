/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

public final class NonNullityMatcher implements ArgumentMatcher
{
   public static final ArgumentMatcher INSTANCE = new NonNullityMatcher();

   private NonNullityMatcher() {}
   public boolean matches(Object argValue) { return argValue != null; }
   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch) { argumentMismatch.append("not null"); }
}

