/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

/**
 * An {@link Expectations} subclass where all expectations are non-strict.
 * <pre>
 *
 * new NonStrictExpectations() {{
 *    <strong>mock1</strong>.expectedMethod(<em>anyInt</em>); <em>result</em> = 123; <em>times</em> = 1;
 *    <strong>MockedClass</strong>.allowedMethod(); <em>result</em> = new IOException();
 *    <strong>mock2</strong>.anotherAllowedMethod(1, "test"); returns("Abc", "xyz");
 * }};
 *
 * // Now exercise the tested code according to the recorded expectations.
 * </pre>
  * <p/>
 * During the replay phase, invocations matching non-strict expectations can occur in any number and in any order.
 * For each of these invocations, the {@link #result} (return value or thrown error/exception) will be either a "no-op"
 * (doing nothing for constructors and {@code void} methods, or returning the default value appropriate to the return
 * type) or whatever was specified through a matching invocation executed in the record phase (matching on the parameter
 * values, optionally using {@linkplain #any argument matchers}).
 * Multiple expectations on the same method or constructor can be recorded, provided different arguments are used.
 * <p/>
 * Invocations occurring during replay that don't match any recorded expectation are allowed, and can be verified later
 * (after having exercised the code under test) through {@link Verifications} blocks.
 * <p/>
 * A lower/upper limit or an exact number of expected invocations can be specified for each recorded expectation,
 * by assigning the appropriate {@link #minTimes}, {@link #maxTimes}, or {@link #times} field just after recording the
 * expectation.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#strictness">In the
 * Tutorial</a>
 *
 * @see #NonStrictExpectations()
 * @see #NonStrictExpectations(Object...)
 * @see #NonStrictExpectations(Integer, Object...)
 */
public abstract class NonStrictExpectations extends Expectations
{
   /**
    * Identical to the corresponding super-constructor {@link Expectations#Expectations()}, except that all expectations
    * recorded will be non-strict.
    *
    * @see #NonStrictExpectations(Object...)
    * @see #NonStrictExpectations(Integer, Object...)
    */
   protected NonStrictExpectations() {}

   /**
    * Identical to the corresponding super-constructor {@link Expectations#Expectations(Object...)}, except that all
    * expectations recorded will be non-strict.
    * <p/>
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#dynamicPartial">In the
    * Tutorial</a>
    *
    * @see #NonStrictExpectations()
    * @see #NonStrictExpectations(Integer, Object...)
    */
   protected NonStrictExpectations(Object... classesOrObjectsToBePartiallyMocked)
   {
      super(classesOrObjectsToBePartiallyMocked);
   }

   /**
    * Identical to the corresponding super-constructor {@link Expectations#Expectations(Integer, Object...)}, except
    * that all expectations recorded will be non-strict.
    * <p/>
    * The effect of specifying a number of iterations larger than 1 (one) is equivalent to multiplying by that number
    * the lower and upper invocation count limits for each invocation inside the expectation block.
    * Note that by default the invocation count range for a non-strict expectation is [0, ∞), that is, a lower limit of
    * 0 (zero) and no upper limit, so the number of iterations will only be meaningful if a positive and finite limit is
    * explicitly specified for the expectation.
    * <p/>
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#iteratedExpectations">In
    * the Tutorial</a>
    *
    * @param numberOfIterations the positive number of iterations for the whole set of invocations recorded inside the
    * block; when not specified, 1 (one) iteration is assumed
    *
    * @see #NonStrictExpectations()
    * @see #NonStrictExpectations(Object...)
    */
   protected NonStrictExpectations(Integer numberOfIterations, Object... classesOrObjectsToBePartiallyMocked)
   {
      super(classesOrObjectsToBePartiallyMocked);
      getCurrentPhase().setNumberOfIterations(numberOfIterations);
   }
}
