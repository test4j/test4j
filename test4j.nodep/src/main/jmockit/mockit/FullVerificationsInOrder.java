/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

/**
 * A combination of {@link FullVerifications} and {@link VerificationsInOrder}.
 * <pre>
 *
 * // Exercise tested code, then verify that expected invocations occurred in the same order,
 * // with no invocations left unverified:
 * new FullVerificationsInOrder() {{
 *    <strong>mock1</strong>.firstExpectedMethod(<em>anyInt</em>); <em>times</em> = 1;
 *    <strong>mock2</strong>.secondExpectedMethod(1, <em>anyString</em>);
 *    <strong>MockedClass</strong>.finalMethod(<em>anyBoolean</em>, null);
 * }};</pre>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#FullVerificationInOrder">In
 * the Tutorial</a>
 */
public abstract class FullVerificationsInOrder extends Verifications
{
   /**
    * Begins <em>in-order</em> verification for <em>all</em> invocations on the mocked types/instances that can
    * potentially be invoked during the replay phase.
    *
    * @see #FullVerificationsInOrder(int)
    * @see #FullVerificationsInOrder(Object...)
    * @see #FullVerificationsInOrder(Integer, Object...)
    */
   protected FullVerificationsInOrder()
   {
      super(true);
      verificationPhase.setAllInvocationsMustBeVerified();
   }

   /**
    * Begins <em>in-order</em> verification for <em>all</em> mocked types/instances invoked during the replay phase of
    * the test, considering that such invocations occurred in a given number of iterations.
    * <p/>
    * The effect of specifying a number of iterations larger than 1 (one) is equivalent to duplicating (like in "copy &
    * paste") the whole sequence of invocations in the block.
    *
    * @param numberOfIterations the positive number of iterations for the whole set of invocations verified inside the
    * block; when not specified, 1 (one) iteration is assumed
    *
    * @see #FullVerificationsInOrder()
    * @see #FullVerificationsInOrder(Object...)
    * @see #FullVerificationsInOrder(Integer, Object...)
    */
   protected FullVerificationsInOrder(int numberOfIterations)
   {
      super(true);
      verificationPhase.setAllInvocationsMustBeVerified();
      verificationPhase.setNumberOfIterations(numberOfIterations);
   }

   /**
    * Same as {@link #FullVerificationsInOrder()}, but restricting the verification to the specified mocked types and/or
    * mocked instances.
    *
    * @param mockedTypesAndInstancesToVerify one or more of the mocked types (ie, {@code Class} objects) and/or mocked
    * instances that are in scope for the test; for a given mocked <em>instance</em>, all classes up to (but not
    * including) {@code java.lang.Object} are considered
    *
    * @see #FullVerificationsInOrder()
    * @see #FullVerificationsInOrder(int)
    * @see #FullVerificationsInOrder(Integer, Object...)
    */
   protected FullVerificationsInOrder(Object... mockedTypesAndInstancesToVerify)
   {
      this();
      verificationPhase.setMockedTypesToFullyVerify(mockedTypesAndInstancesToVerify);
   }

   /**
    * Same as {@link #FullVerificationsInOrder(int)}, but restricting the verification to the specified mocked types
    * and/or mocked instances.
    *
    * @param mockedTypesAndInstancesToVerify one or more of the mocked types (ie, {@code Class} objects) and/or mocked
    * instances that are in scope for the test; for a given mocked <em>instance</em>, all classes up to (but not
    * including) {@code java.lang.Object} are considered
    *
    * @see #FullVerificationsInOrder()
    * @see #FullVerificationsInOrder(int)
    * @see #FullVerificationsInOrder(Object...)
    */
   protected FullVerificationsInOrder(Integer numberOfIterations, Object... mockedTypesAndInstancesToVerify)
   {
      this(numberOfIterations);
      verificationPhase.setMockedTypesToFullyVerify(mockedTypesAndInstancesToVerify);
   }
}
