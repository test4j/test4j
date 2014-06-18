/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates one or more {@linkplain #value() classes} to be mocked or stubbed out for the whole test class or the whole
 * test suite (in the case of a JUnit 4 test suite definition class).
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/UsingMocksAndStubs.html">In the Tutorial</a>
 *
 * @see MockUp
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UsingMocksAndStubs
{
   /**
    * A mix of mock and real classes from which the set of methods and constructors to be mocked or stubbed out is
    * obtained.
    * <p/>
    * A <em>mock class</em> is one which extends the {@linkplain MockUp MockUp&lt;T>} base class.
    * It can define mocks and/or stubs for individual methods/constructors in a real class.
    * Any other kind of class passed in this attribute is regarded as a "real class", which will have all of its methods
    * and constructors, as well static initializers, stubbed out.
    */
   Class<?>[] value();
}
