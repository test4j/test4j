/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates a {@linkplain Mocked mocked type} where the <em>return types</em> of mocked methods get automatically
 * mocked if and when an invocation to the method occurs, with a mocked instance being returned instead of {@code null}.
 * Further invocations can then be made on the <em>cascaded</em> instances, with the cascading process repeating itself
 * as needed.
 * <p/>
 * Methods returning {@code String}, primitive wrappers, or collection types are <em>not</em> considered for cascading.
 * <p/>
 * In a test having a cascading mocked type, a separate non-cascading mocked type can be used to record/verify
 * expectations on intermediate cascaded instances.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#cascading">In the
 * Tutorial</a>
 * <p/>
 * Sample tests:
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/jbossaop/test/jbossaop/testing/bank/BankBusinessTest.java"
 * >BankBusinessTest</a>,
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/mockit/CascadingFieldTest.java"
 * >CascadingFieldTest</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Cascading
{
}