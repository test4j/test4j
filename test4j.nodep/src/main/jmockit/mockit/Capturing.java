/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates a mock field or a mock parameter for which all classes extending/implementing the mocked type will
 * <em>also</em> get mocked.
 * <p/>
 * In the case of a non-final "capturing" mock field, mocked instances will (by default) be captured and assigned to
 * the mock field as they are created.
 * Otherwise (ie, when applied to a {@code final} mock field or to a mock parameter), instances are still captured but
 * not made directly available to the test.
 * The {@link #maxInstances} attribute allows an upper limit to the number of captured instances to be specified.
 * If multiple capturing mock fields of the same type are declared, this attribute can be used so that each distinct
 * instance gets assigned to a separate field.
 * <p/>
 * Note that, once a capturing mocked type is in scope, the capture of implementation classes and their instances can
 * happen at any moment before the first expected invocation is recorded, or during the recording and replay phases.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#capturing">In the
 * Tutorial</a>
 * <p/>
 * Sample tests:
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/integrationTests/SubclassTest.java">SubclassTest</a>,
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/mockit/CapturingImplementationsTest.java">CapturingImplementationsTest</a>,
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/TimingFramework/test/org/jdesktop/animation/timing/interpolation/PropertySetterTest.java">PropertySetterTest</a>
 *
 * @see #classNames classNames
 * @see #inverse inverse
 * @see #maxInstances maxInstances
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Capturing
{
   /**
    * When the annotation is applied to an instance field, this attribute specifies the maximum number of new instances
    * to <em>capture</em> (by assigning them to the field) while the test is running, between those instances which are
    * assignable to the mocked type and are created during the test.
    * <p/>
    * If {@code maxInstances} is zero (or negative), no instances created by a test are captured.
    * <p/>
    * If the value for this attribute is positive or unspecified (the default is {@code Integer.MAX_VALUE}), then
    * whenever an assignable instance is created during test execution and the specified number of new instances has not
    * been previously assigned, the (non-<code>final</code>) mock field will be assigned that new instance.
    * <p/>
    * It is valid to declare two or more fields of the same mocked type with a positive number of {@code maxInstances}
    * for each one of them, say {@code n1}, {@code n2}, etc.
    * In this case, the first {@code n1} new instances will be assigned to the first field, the following {@code n2} new
    * instances to the second, and so on.
    * <p/>
    * Notice that this attribute does not apply to {@code final} mock fields, which cannot be reassigned.
    */
   int maxInstances() default Integer.MAX_VALUE;

   /**
    * One or more implementation class filters.
    * Only classes which implement/extend the base type and match at least one filter will be considered.
    * <p/>
    * Each filter must contain a {@linkplain java.util.regex.Pattern regular expression} for matching fully qualified
    * class names.
    */
   String[] classNames() default {};

   /**
    * Indicates whether the implementation filters are to be inverted or not.
    * If inverted, only the classes matching them are <strong>not</strong> captured.
    */
   boolean inverse() default false;
}
