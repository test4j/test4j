/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates an instance <em>field</em> or test method <em>parameter</em> of a <em>mocked type</em> whose value will be
 * a <em>mocked instance</em>.
 * Such fields or parameters can be of any type, except for primitive and array types.
 * The declared type of the <em>mock field</em> or <em>mock parameter</em> is taken to be the mocked type.
 * <p/>
 * For the duration of each test where the mocked type is in scope, all new instances of that type, as well as those
 * previously created, will also be mocked.
 * When the mocked type is a class, all super-classes up to but not including {@code java.lang.Object} are also mocked.
 * <em>Static methods</em> and <em>constructors</em> belonging to a mocked class type are mocked as well, just like
 * instance methods; such methods can even be {@code final}, {@code private}, or {@code native}.
 * <p/>
 * An {@code enum} type can also be mocked. The {@code java.lang.Enum} base class, however, is <em>not</em> mocked by
 * default, similarly to {@code Object} when mocking a class.
 * That said, if needed these base types <em>can</em> be mocked by explicitly declaring a mock field or mock parameter
 * of the specific base type.
 * <p/>
 * When a method or constructor is mocked, an invocation does not result in the execution of the original code, but in a
 * (generated) call into JMockit, which then responds with either a default or a <em>recorded</em> result (or with a
 * constraint violation, if the invocation is deemed to be unexpected - which depends on a few factors discussed
 * elsewhere).
 * <p/>
 * Static <em>class initializers</em> (including assignments to {@code static} fields) of a mocked class are not
 * affected, unless {@linkplain #stubOutClassInitialization specified otherwise}.
 * <p/>
 * An instance mock <em>field</em> can be declared in a test class, in a super-class of a test class, or in an
 * {@link Expectations} subclass.
 * A mock <em>parameter</em>, on the other hand, can only be declared as a test method parameter (in JUnit or TestNG
 * tests).
 * <p/>
 * Normally, a new mocked instance gets created and assigned to a declared mock field automatically.
 * In the case of a mocked {@code enum}, the first enumeration element is selected.
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
 * @see #value
 * @see #stubOutClassInitialization
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Mocked
{
   /**
    * One or more <em>mock filters</em>.
    * Given a target class for mocking, only those methods and constructors which match at least one filter will be
    * mocked.
    * <p/>
    * Each mock filter must follow the syntax <strong>{@code [nameRegex][(paramTypeName...)]}</strong>, where
    * {@code nameRegex} is a {@linkplain java.util.regex.Pattern regular expression} for matching method names, and
    * {@code paramTypeName} is the name of a primitive or reference parameter type (actually, any suffix of the type
    * name is enough, like "String" instead of the full class name "java.lang.String").
    * If {@code nameRegex} is omitted the filter matches only constructors.
    * If {@code (paramTypeName...)} is omitted the filter matches methods with any parameters.
    * <p/>
    * If no filters are specified, then all methods and constructors declared in the target class are mocked.
    * <p/>
    * A filter containing just the empty string matches <em>no</em> methods or constructors of the target class;
    * this can be used to obtain a mocked instance where no executable code is actually mocked.
    *
    * @see #stubOutClassInitialization
    */
   String[] value() default {};

   /**
    * Same as specifying the {@link #value} attribute (which is the <em>default</em> attribute for this annotation).
    *
    * @deprecated Use the default {@link #value} attribute instead; this one is going to be removed in a future release.
    */
   @Deprecated
   String[] methods() default {};

   /**
    * Indicates whether the mock filters are to be inverted or not.
    * If inverted, only the methods and constructors matching them are <strong>not</strong> mocked.
    *
    * @deprecated This attribute is going to be removed in a future release.
    */
   @Deprecated
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
}
