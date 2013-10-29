/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

public final class InequalityMatcher extends EqualityMatcher
{
   public InequalityMatcher(Object notEqualArg) { super(notEqualArg); }

   @Override
   public boolean matches(Object argValue) { return !super.matches(argValue); }

   @Override
   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("not ").appendFormatted(object);
   }
}
