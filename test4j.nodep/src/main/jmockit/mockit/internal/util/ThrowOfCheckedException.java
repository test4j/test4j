/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

public final class ThrowOfCheckedException
{
   private static Exception exceptionToThrow;

   ThrowOfCheckedException() throws Exception { throw exceptionToThrow; }

   public static synchronized void doThrow(Exception checkedException)
   {
      exceptionToThrow = checkedException;
      ConstructorReflection.newInstanceUsingDefaultConstructor(ThrowOfCheckedException.class);
   }
}
