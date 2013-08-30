/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

final class FailureState
{
   private final Thread testThread;
   private Error errorThrownInAnotherThread;

   /**
    * Holds an error associated to an ExpectedInvocation that is to be reported to the user.
    * <p/>
    * This field is also set if and when an unexpected invocation is detected, so that any future
    * invocations in this same phase execution can rethrow the original error instead of throwing a
    * new one, which would hide the original.
    * Such a situation can happen when test code or the code under test contains a "catch" or
    * "finally" block where a mock invocation is made after a previous such invocation in the "try"
    * block already failed.
    */
   private Error errorThrown;

   FailureState()
   {
      testThread = Thread.currentThread();
   }

   Error getErrorThrown() { return errorThrown; }
   void setErrorThrown(Error error) { errorThrown = error; }
   void clearErrorThrown() { errorThrown = null; }

   void reportErrorThrownIfAny()
   {
      if (errorThrown != null) {
         if (testThread == Thread.currentThread()) {
            throw errorThrown;
         }
         else {
            errorThrownInAnotherThread = errorThrown;
         }
      }
   }

   Error getErrorThrownInAnotherThreadIfAny(Error errorFromTestThread)
   {
      return errorThrownInAnotherThread == null ? errorFromTestThread : errorThrownInAnotherThread;
   }
}
