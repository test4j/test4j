/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * For tests using {@link mockit.Expectations}, indicates a mock field/parameter of a <em>non-strict</em> mocked type,
 * whose expectations are <em>allowed</em> but not <em>expected</em> to occur (unless specified otherwise).
 * <p/>
 * While in the replay phase, invocations on the non-strict mocked type can be made in any number and in any order.
 * For each of these invocations, the result (return value or thrown error/exception) will be either a "no-op" (doing
 * nothing for constructors and {@code void} methods, or returning the default value appropriate to the return type) or
 * whatever was specified through a matching invocation executed in the record phase (matching on the parameter values,
 * optionally using argument matchers).
 * Note that multiple invocations to the same method or constructor can be recorded, provided different arguments are
 * used.
 * <p/>
 * Each recorded non-strict invocation still specifies an expectation, but without any invocation count constraint to be
 * satisfied. Therefore, a corresponding invocation in the replay phase will not be required for the test to pass.
 * By default, any number of such corresponding invocations can occur in the replay phase for the recorded invocation,
 * in any order.
 * If a lower/upper limit or an exact number of invocations is expected, then the appropriate constraint method should
 * be used after recording the invocation.
 * <p/>
 * Invocations to non-strict mocks or mocked types that should occur in the replay phase can be explicitly verified at
 * the end of the test using a {@linkplain mockit.Verifications verification block}.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#strictMocks">In the
 * Tutorial</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface NonStrict
{
}
