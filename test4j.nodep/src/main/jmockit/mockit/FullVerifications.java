/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

/**
 * Same as {@link Verifications}, but checking that <em>all</em> invocations in the replay phase are explicitly
 * verified, except for those already verified through other means.
 * As such, the verification block represents a full set of verifications for the mocked types/instances used in the
 * test.
 * <pre>
 *
 * // Exercise tested code, then verify that expected invocations occurred in any order,
 * // with no invocations left unverified:
 * new FullVerifications() {{
 *    <strong>mock1</strong>.expectedMethod(<em>anyInt</em>);
 *    <strong>mock2</strong>.anotherExpectedMethod(1, "test"); <em>times</em> = 2;
 * }};</pre>
 * Any invocation in the replay phase not covered by one of these verifications will cause an assertion error to be
 * thrown.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#FullVerification">In the
 * Tutorial</a>
 */
public abstract class FullVerifications extends Verifications
{
   /**
    * Begins <em>full</em> verification on the mocked types/instances that can potentially be invoked during the replay
    * phase of the test.
    *
    * @see #FullVerifications(int)
    * @see #FullVerifications(Integer, Object...)
    * @see #FullVerifications(Object...)
    */
   protected FullVerifications()
   {
      verificationPhase.setAllInvocationsMustBeVerified();
   }

   /**
    * Begins <em>full</em> verification on the mocked types/instances invoked during the replay phase of the test,
    * considering that such invocations occurred in a given number of iterations.
    * <p/>
    * The effect of specifying a (positive) number of iterations is equivalent to setting to that number the lower and
    * upper invocation count limits for each expectation verified inside the block.
    * If, however, the lower/upper limit is explicitly specified for an expectation, the given number of iterations
    * becomes a multiplier.
    * When not specified, at least one matching invocation will be required to have occurred; therefore, specifying
    * <em>1 (one)</em> iteration is different from not specifying the number of iterations at all.
    *
    * @param numberOfIterations the positive number of iterations for the whole set of invocations verified inside the
    * block
    *
    * @see #FullVerifications()
    * @see #FullVerifications(Integer, Object...)
    * @see #FullVerifications(Object...)
    * @see #times
    * @see #minTimes
    * @see #maxTimes
    */
   protected FullVerifications(int numberOfIterations)
   {
      super(numberOfIterations);
      verificationPhase.setAllInvocationsMustBeVerified();
   }

   /**
    * Same as {@link #FullVerifications()}, but restricting the verification to the specified mocked types and/or
    * mocked instances.
    *
    * @param mockedTypesAndInstancesToVerify one or more of the mocked types (ie, {@code Class} objects) and/or mocked
    * instances that are in scope for the test; for a given mocked <em>instance</em>, all classes up to (but not
    * including) {@code java.lang.Object} are considered
    *
    * @see #FullVerifications()
    * @see #FullVerifications(int)
    * @see #FullVerifications(Integer, Object...)
    */
   protected FullVerifications(Object... mockedTypesAndInstancesToVerify)
   {
      this();
      verificationPhase.setMockedTypesToFullyVerify(mockedTypesAndInstancesToVerify);
   }

   /**
    * Same as {@link #FullVerifications(int)}, but restricting the verification to the specified mocked types and/or
    * mocked instances.
    *
    * @param numberOfIterations the positive number of iterations for the whole set of invocations verified inside the
    * block
    * @param mockedTypesAndInstancesToVerify one or more of the mocked types (ie, {@code Class} objects) and/or mocked
    * instances that are in scope for the test; for a given mocked <em>instance</em>, all classes up to (but not
    * including) {@code java.lang.Object} are considered
    *
    * @see #FullVerifications()
    * @see #FullVerifications(int)
    * @see #FullVerifications(Object...)
    */
   protected FullVerifications(Integer numberOfIterations, Object... mockedTypesAndInstancesToVerify)
   {
      this(numberOfIterations);
      verificationPhase.setMockedTypesToFullyVerify(mockedTypesAndInstancesToVerify);
   }
}
