/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates an instance field holding either a default return value for a given return type, or a checked exception
 * instance to be thrown for a given checked exception type.
 * If the field is not explicitly assigned a value, then one will be created and assigned automatically, provided the
 * declared type of the field is a class having a no-args constructor.
 * <p/>
 * This annotation is only relevant inside expectation blocks, and only applies to mocked methods/constructors for which
 * no matching expectation is recorded.
 * <p/>
 * If more than one input field of the <em>same</em> type is declared in an expectation block, then the first matching
 * invocation will take its result from the first field (in the textual order of declaration), the second one from the
 * second field, and so on until the last field of that type, which will then be used for all remaining invocations.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#defaultResults">In the
 * Tutorial</a>
 *
 * @see #invocations invocations
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Input
{
   /**
    * Specifies a (positive) fixed number of matching invocations that will get this field's result value.
    * Any additional invocations will either get the result specified by the next input field of the same type (when
    * available), or get the regular default result as if no input field was specified.
    * <p/>
    * If not specified, an unlimited number of matching invocations will get the default result associated with the
    * input field.
    */
   int invocations() default Integer.MAX_VALUE;
}
