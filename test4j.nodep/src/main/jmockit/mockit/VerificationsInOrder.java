/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import mockit.internal.expectations.*;

/**
 * Same as {@link Verifications}, but checking that invocations in the replay phase occurred in the same order as
 * specified in this <em>ordered</em> verification block.
 * <pre>
 *
 * // Exercise tested code, then verify that expected invocations occurred in the same order:
 * new VerificationsInOrder() {{
 *    <strong>mock1</strong>.firstExpectedMethod(<em>anyInt</em>); <em>minTimes</em> = 1;
 *    <strong>mock2</strong>.secondExpectedMethod(1, "test"); <em>maxTimes</em> = 2;
 *    <strong>MockedClass</strong>.finalMethod(<em>anyString</em>);
 * }};</pre>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#VerificationInOrder">In the
 * Tutorial</a>
 *
 * @see #VerificationsInOrder()
 * @see #VerificationsInOrder(int)
 * @see #unverifiedInvocations()
 * @see #verifiedInvocations(Verifications)
 */
public abstract class VerificationsInOrder extends Verifications
{
   /**
    * Begins <em>in-order</em> verification on the mocked types/instances invoked during the replay phase of the test.
    *
    * @see #VerificationsInOrder(int)
    */
   protected VerificationsInOrder() { super(true); }

   /**
    * Begins <em>in-order</em> verification on the mocked types/instances invoked during the replay phase of the test.
    * <p/>
    * The effect of specifying a number of iterations larger than 1 (one) is equivalent to duplicating (like in "copy &
    * paste") the whole sequence of invocations in the block.
    *
    * @param numberOfIterations the positive number of iterations for the whole set of invocations
    * verified inside the block; when not specified, 1 (one) iteration is assumed
    *
    * @see #VerificationsInOrder()
    */
   protected VerificationsInOrder(int numberOfIterations)
   {
      super(true);
      verificationPhase.setNumberOfIterations(numberOfIterations);
   }

   /**
    * Accounts for a sequence of non-strict invocations executed in the replay phase that are not explicitly verified
    * in this block or in a previous block.
    * Such a "sequence" of invocations can include only a single invocation, or even be empty.
    * <p/>
    * Invocations matching an expectation recorded with a minimum invocation count - if any - are <em>also</em>
    * included here, since their replay order could not be verified otherwise.
    * This doesn't apply to <em>strict</em> expectations, though, since in that case the replay order must be as
    * recorded.
    * <p/>
    * This method can be used to verify that one or more consecutive invocations occurred <em>before</em> others, and
    * conversely to verify that one or more consecutive invocations occurred <em>after</em> others.
    * The call to this method marks the position where the unverified invocations are expected to have occurred,
    * relative to the explicitly verified ones.
    * <p/>
    * The exact sequence of unverified invocations accounted for by a particular call to this method depends on the
    * <em>position</em> of the call relative to the explicit verifications in the block.
    * Each grouping of explicit verifications in the block will correspond to a sequence of <em>consecutive</em>
    * (and verified) invocations in the replay phase of the test.
    * So, if this method is called more than once from the same verification block, each call will account for a
    * separate sequence of unverified invocations; each sequence will be verified to occur, as a whole, in the same
    * order as it appears relative to those groupings of verified invocations.
    * <p/>
    * Notice that when this method is not used, the invocations in the replay phase need <em>not</em> be consecutive,
    * but only have the same relative order as the verification calls.
    * <p/>
    * Finally, notice that you can combine an ordered block that verifies the position of some calls relative to others
    * with a later unordered block which verifies some or all of those other invocations.
    * The unordered block should not come before, however, since it would "consume" the verified invocations.
    *
    * @see #verifiedInvocations(Verifications)
    * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#partiallyOrdered">In
    * the Tutorial</a>
    */
   protected final void unverifiedInvocations()
   {
      ((OrderedVerificationPhase) verificationPhase).fixPositionOfUnverifiedExpectations();
   }

   /**
    * Accounts for a sequence of non-strict invocations executed in the replay phase that have already been explicitly
    * verified in a previous verification block.
    *
    * @param alreadyVerified an unordered verification block describing a group of already verified invocations
    *
    * @throws IllegalArgumentException if the given verifications are ordered
    *
    * @see #unverifiedInvocations()
    * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#partiallyOrdered">In
    * the Tutorial</a>
    */
   protected final void verifiedInvocations(Verifications alreadyVerified)
   {
      ((OrderedVerificationPhase) verificationPhase).checkOrderOfVerifiedInvocations(
         alreadyVerified.verificationPhase);
   }
}
