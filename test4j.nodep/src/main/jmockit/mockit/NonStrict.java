/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * For tests using {@link mockit.Expectations}, indicates a mock field/parameter of a <em>non-strict</em> mocked type,
 * whose expectations are <em>allowed</em> but not <em>expected</em> to occur (unless specified otherwise).
 *
 * @deprecated Use {@link NonStrictExpectations} instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Deprecated
public @interface NonStrict
{
}
