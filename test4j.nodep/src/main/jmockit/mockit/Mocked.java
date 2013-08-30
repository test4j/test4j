/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates an instance <em>field</em> or test method <em>parameter</em> of a <em>mocked type</em> whose value will be
 * a <em>mocked instance</em>.
 * Such fields or parameters can be of any type, except for primitive and array types.
 * The declared type of the <em>mock field</em> or <em>mock parameter</em> is taken to be the mocked type, unless
 * {@linkplain #realClassName specified otherwise}.
 * <p/>
 * For the duration of each test where the mocked type is in scope, all new instances of that type, as well as those
 * previously created, will also be mocked.
 * When the mocked type is a class, all super-classes up to but not including {@code java.lang.Object} are also mocked.
 * <em>Static methods</em> and <em>constructors</em> belonging to a mocked class type are mocked as well, just like
 * instance methods; such methods can even be {@code final}, {@code private}, or {@code native}.
 * <p/>
 * When a method or constructor is mocked, an invocation does not result in the execution of the original code, but in a
 * (generated) call into JMockit, which then responds with either a default or a <em>recorded</em> result (or with a
 * constraint violation, if the invocation is deemed to be unexpected - which depends on a few factors discussed
 * elsewhere).
 * <p/>
 * Static <em>class initializers</em> (including assignments to static fields) of a mocked class are <em>not</em>
 * stubbed out, unless {@linkplain #stubOutClassInitialization specified otherwise}.
 * <p/>
 * An instance mock <em>field</em> can be declared in a test class, in a super-class of a test class, or in an
 * {@link Expectations} subclass.
 * A mock <em>parameter</em>, on the other hand, can only be declared as a test method parameter (in JUnit or TestNG
 * tests).
 * <p/>
 * Note that not only a concrete {@code Expectations} subclass used in specific tests can declare mock fields, but
 * also any intermediate super-class in the inheritance hierarchy from {@code Expectations} to the "final" subclass.
 * This can be used to define reusable {@code Expectations} subclasses, containing mock fields that are common to a
 * whole group of tests.
 * <p/>
 * Note also that for inner {@code Expectations} subclasses the full set of mock fields will consist of the local
 * "inner" fields plus any fields declared in the test class.
 * This can be used to declare mock fields that are common to all tests in the class, even though some of these tests
 * may define additional mock fields in specific {@code Expectations} subclasses.
 * The same applies to mock parameters declared for the test method.
 * <p/>
 * Normally, a new mocked instance gets created and assigned to a declared mock field automatically.
 * If needed, the test itself can provide the instance by declaring the mock field as {@code final} and explicitly
 * assigning it with the desired instance (even so, it will still be a <em>mocked</em> instance).
 * If no instance is necessary (perhaps because only static methods or constructors will be called), then this final
 * field can receive the {@code null} reference.
 * Mock parameters, on the other hand, will always receive a new mocked instance whenever the test method is executed by
 * the test runner.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#declaration">In the
 * Tutorial</a>
 *
 * @see #methods methods
 * @see #inverse inverse
 * @see #stubOutClassInitialization stubOutClassInitialization
 * @see #realClassName realClassName
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Mocked
{
   /**
    * Same as specifying only the {@link #methods methods} attribute.
    */
   String[] value() default {};

   /**
    * One or more <em>mock filters</em>.
    * Given a target class for mocking, only those methods and constructors which match at least one filter will be
    * mocked.
    * <p/>
    * If no other annotation attribute needs to be specified for a given mocked type, then for convenience the value for
    * this attribute can also be specified through the default {@link #value value} attribute, that is, by just
    * supplying the list of mock filters without naming any annotation attribute.
    * <p/>
    * Each mock filter must follow the syntax <strong>{@code [nameRegex][(paramTypeName...)]}</strong>, where
    * {@code nameRegex} is a {@linkplain java.util.regex.Pattern regular expression} for matching method names, and
    * {@code paramTypeName} is the name of a primitive or reference parameter type (actually, any suffix of the type
    * name is enough, like "String" instead of the full class name "java.lang.String").
    * If {@code nameRegex} is omitted the filter matches only constructors.
    * If {@code (paramTypeName...)} is omitted the filter matches methods with any parameters.
    * <p/>
    * If no filters are specified, then all methods and constructors declared in the target class are mocked, and all
    * static initializers are stubbed out.
    * <p/>
    * A filter containing just the empty string matches <em>no</em> methods, no constructors, and no static initializers
    * of the target class; this can be used to obtain a mocked instance where no executable code is actually mocked or
    * stubbed out.
    * <p/>
    * The special filter {@code "<clinit>"} can be used to match the static initializers of the target class.
    * If only this filter is specified then the static initializers are stubbed out and no methods or constructors are
    * mocked; the opposite can be achieved by using the {@link #inverse inverse} attribute.
    */
   String[] methods() default {};

   /**
    * Indicates whether the mock filters are to be inverted or not.
    * If inverted, only the methods and constructors matching them are <strong>not</strong> mocked.
    */
   boolean inverse() default false;

   /**
    * Indicates whether <em>static initialization code</em> in the mocked class should be stubbed out or not.
    * Static initialization includes the execution of assignments to static fields of the class and the execution of
    * static initialization blocks, if any.
    * (Note that {@code static final} fields initialized with <em>compile-time</em> constants are not assigned at
    * runtime, remaining unaffected whether the class is stubbed out or not.)
    * <p/>
    * By default, static initialization code in a mocked class is <em>not</em> stubbed out.
    * The JVM will only perform static initialization of a class <em>once</em>, so stubbing out the initialization code
    * can have unexpected consequences.
    * Static initialization will occur the first time the class is instantiated, has a static method called on it, or
    * has a static field whose value is defined at runtime accessed; these are the only events which prompt the JVM to
    * initialize a class.
    * If the original class initialization code was stubbed out, then it will not be there to be executed at the time of
    * static initialization, potentially leaving static fields {@code null} and later causing
    * {@code NullPointerException}'s to occur.
    */
   boolean stubOutClassInitialization() default false;

   /**
    * Specify the fully qualified name of the concrete class to be considered as the mocked type when it cannot be used
    * as the declared type, and the instance automatically created by JMockit for a super-type wouldn't be appropriate
    * for the test.
    * <p/>
    * Note that this attribute can also be used when the desired concrete class is not accessible to the test (for
    * example, if it's a private inner class inside the code under test).
    *
    * @see Deencapsulation#newInstance(String, Class[], Object...)
    */
   String realClassName() default "";
}
