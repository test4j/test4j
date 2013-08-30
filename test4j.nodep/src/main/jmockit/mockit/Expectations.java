/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

import mockit.internal.expectations.*;

/**
 * A set of <em>expected</em> and/or <em>allowed</em> method/constructor invocations on the mocked types/instances that
 * have been made available to the test through mock fields and/or mock parameters.
 * <pre>
 *
 * new Expectations() {{
 *    <strong>mock1</strong>.expectedMethod(<em>anyInt</em>); <em>result</em> = 123; <em>times</em> = 2;
 *    <strong>mock2</strong>.anotherExpectedMethod(1, "test"); returns("Abc", "xyz");
 *    <strong>MockedClass</strong>.allowedMethod(); notStrict();
 * }};
 * // Now exercise the tested code according to the recorded expectations.</pre>
 * Typically, this class is used by extending it with <em>anonymous inner classes</em> (so called <em>expectation
 * blocks</em>) inside test methods, which record expectations on mocked types or instances by calling instance methods
 * on mock fields/parameters, static methods on mocked classes, and/or constructors of mocked classes.
 * Arguments passed in such calls are later matched to the actual arguments passed from the code under test.
 * <p/>
 * Any instance field declared in a subclass is considered a <em>local</em> mock field, provided it has a mockable type
 * and is either non-private or annotated with {@linkplain Mocked @Mocked}.
 * <p/>
 * There are several special fields and methods which can be used in the expectation block, to: a) record desired return
 * values or exceptions/errors to be thrown ({@link #result}, {@link #returns(Object, Object...)}); b) relax or
 * constrain the matching of argument values ({@link #anyInt}, {@link #anyString}, {@link #withNotNull()}, etc.);
 * c) relax or constrain the expected and/or allowed number of matching invocations ({@link #times}, {@link #minTimes},
 * {@link #maxTimes}).
 * <p/>
 * Individual expectations are defined during the <em>record phase</em>, and later exercised during the
 * <em>replay phase</em> of the test.
 * At the end of the test, the test runner will automatically assert that all <em>expected</em> invocations actually
 * occurred during the replay phase.
 * An expectation block may also record expectations that are merely <em>allowed</em> to occur, and as such are not
 * implicitly verified at the end of the test.
 * <p/>
 * Additional features and details:
 * <ul>
 * <li>
 * A <strong>mock field</strong> can be of any non-primitive type, including interfaces, abstract classes, and concrete
 * classes (even {@code final} classes).
 * An instance will be automatically created when the subclass gets instantiated, unless the field is {@code final}
 * (in which case, the test code itself will have the responsibility of obtaining an appropriate instance).
 * This mocked instance can then be used inside the expectation block for the recording of expectations on instance
 * methods; <strong>static methods</strong> and <strong>constructors</strong> belonging to the mocked class or its
 * super-classes are also mocked, and can also have expectations recorded on them.
 * </li>
 * <li>
 * Unless specified otherwise, all expectations defined inside an {@code Expectations} immediate subclass will be
 * <em>strict</em>, meaning that the recorded invocations are <em>expected</em> to occur in the same order during the
 * replay phase, and that non-recorded invocations are <em>not allowed</em>.
 * This default behavior can be overridden for individual expectations through the {@link #notStrict()} method, and for
 * whole mocked types through the {@link NonStrict} annotation.
 * </li>
 * <li>
 * There is a set of API methods that allow the {@linkplain #newInstance(String, Class[], Object...) instantiation of
 * non-accessible (to the test) classes}, the {@linkplain #invoke(Object, String, Object...) invocation of
 * non-accessible methods}, and the {@linkplain #setField(Object, String, Object) setting of non-accessible fields}.
 * Most tests shouldn't need these facilities, though.
 * </li>
 * <li>
 * By default, the exact instance on which instance method invocations occur during the replay phase is <em>not</em>
 * verified to be the same as the instance used when recording the corresponding expectation, <em>unless</em> the
 * mock field/parameter was declared to be {@linkplain Injectable @Injectable}.
 * If such verification on non-injectable instances is needed, the {@link #onInstance(Object)} method should be used.
 * </li>
 * <li>
 * There are additional constructors which provide other features:
 * {@linkplain #Expectations(Object...) dynamic partial mocking}, and
 * {@linkplain #Expectations(Integer, Object...) iterated invocations}.
 * </li>
 * </ul>
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#expectation">In the
 * Tutorial</a>
 *
 * @see #Expectations()
 */
public abstract class Expectations extends Invocations
{
   private final RecordAndReplayExecution execution;

   /**
    * A value assigned to this field will be taken as the result for the current expectation.
    * <p/>
    * If the value is a {@link Throwable} then it will be <em>thrown</em> when a matching invocation occurs in the
    * replay phase.
    * Otherwise, it's assumed to be a <em>return value</em> for a non-void method, and will be returned at replay time
    * from a matching invocation.
    * If the current expectation is for a method which actually <em>returns</em> an exception or error (as opposed to
    * <em>throwing</em> one), then the {@link #returns(Object)} method should be used instead, as it only applies to
    * return values.
    * <p/>
    * Attempting to return a value that is incompatible with the method return type will cause a
    * {@code ClassCastException} to be thrown at replay time.
    * If, however, the recorded invocation is to a constructor or {@code void} method, then a matching invocation during
    * replay will be allowed, with the specified return value disregarded.
    * <p/>
    * If the value assigned to the field is an array or of a type assignable to {@link Iterable} or to {@link Iterator},
    * then it is taken as a sequence of <em>consecutive results</em> for the current expectation, as long as the method
    * return type is not itself of an equivalent array/iterable/iterator type.
    * Another way to specify consecutive results is to simply write multiple consecutive assignments to the field, for
    * the same expectation.
    * <p/>
    * Finally, custom results can be provided through a {@linkplain mockit.Delegate} object assigned to the field.
    * <p/>
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#results">In the Tutorial</a>
    *
    * @see #returns(Object)
    * @see #returns(Object, Object...)
    */
   protected static Object result;

   /**
    * Initializes this set of expectations, entering the <em>record</em> phase.
    * <p/>
    * For each associated {@linkplain Mocked mocked type}, the following tasks are performed:
    * <ol>
    * <li>
    * Redefines the <em>target class for mocking</em> derived from the mocked type.
    * </li>
    * <li>
    * If the declared type to be mocked is an abstract class, then generates a concrete subclass with empty
    * implementations for all inherited abstract methods.
    * </li>
    * <li>
    * If the mocked type is the declared type of a non-<code>final</code> instance field, then creates and assigns a new
    * (mocked) instance to that field.
    * </li>
    * </ol>
    * After this, test code can start recording invocations on the mocked types and/or mocked instances.
    * Each and every such call made from inside the expectation block is recorded.
    *
    * @see #Expectations(Object...)
    * @see #Expectations(Integer, Object...)
    */
   protected Expectations()
   {
      execution = new RecordAndReplayExecution(this, (Object[]) null);
   }

   /**
    * Same as {@link #Expectations()}, except that one or more classes will be partially mocked according to the
    * expectations recorded in the expectation block.
    * Such classes are those directly specified as well as those to which any given instances belong.
    * <p/>
    * During the replay phase, any invocations to one of these classes or instances will execute real production code,
    * unless a matching invocation was recorded as an expectation inside the block.
    * <p/>
    * For a given {@code Class} object, all constructors and methods will be considered for mocking, from the specified
    * class up to but not including {@code java.lang.Object}.
    * <p/>
    * For a given <em>object</em>, all methods will be considered for mocking, from the concrete class of the given
    * object up to but not including {@code java.lang.Object}.
    * The constructors of those classes will <em>not</em> be considered.
    * During replay, invocations to instance methods will only match expectations recorded on the given instance
    * (or instances, if more than one was given).
    * <p/>
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#dynamicPartial">In the
    * Tutorial</a>
    *
    * @param classesOrObjectsToBePartiallyMocked one or more classes or objects whose classes are to be considered for
    * partial mocking
    *
    * @throws IllegalArgumentException if given a class literal for an interface, an annotation, an array, a
    * primitive/wrapper type, or a {@linkplain java.lang.reflect.Proxy#isProxyClass(Class) proxy class} created for an
    * interface, or if given a value/instance of such a type
    * 
    * @see #Expectations()
    * @see #Expectations(Integer, Object...)
    */
   protected Expectations(Object... classesOrObjectsToBePartiallyMocked)
   {
      execution = new RecordAndReplayExecution(this, classesOrObjectsToBePartiallyMocked);
   }

   /**
    * Identical to {@link #Expectations(Object...)}, but considering that the invocations inside the block will occur in
    * a given number of iterations.
    * <p/>
    * The effect of specifying a number of iterations larger than 1 (one) is equivalent to duplicating (like in "copy &
    * paste") the whole sequence of <em>strict</em> invocations in the block.
    * For any <em>non-strict</em> invocation inside the same block, the effect will be equivalent to multiplying the
    * minimum and maximum invocation count by the specified number of iterations.
    * <p/>
    * It's also valid to have multiple expectation blocks for the same test, each with an arbitrary number of
    * iterations, and containing any mix of strict and non-strict expectations.
    * <p/>
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#iteratedExpectations">In
    * the Tutorial</a>
    * 
    * @param numberOfIterations the positive number of iterations for the whole set of invocations recorded inside the
    * block; when not specified, 1 (one) iteration is assumed
    *
    * @see #Expectations()
    * @see #Expectations(Object...)
    */
   protected Expectations(Integer numberOfIterations, Object... classesOrObjectsToBePartiallyMocked)
   {
      this(classesOrObjectsToBePartiallyMocked);
      getCurrentPhase().setNumberOfIterations(numberOfIterations);
   }

   @Override
   final RecordPhase getCurrentPhase()
   {
      return execution.getRecordPhase();
   }

   // Methods for setting expected return values //////////////////////////////////////////////////////////////////////

   /**
    * Specifies that the previously recorded method invocation will return a given value during replay.
    * <p/>
    * More than one return value can be specified for the same invocation by simply calling this method multiple times,
    * with the desired consecutive values to be later returned.
    * For an strict expectation, the maximum number of expected invocations is automatically adjusted so that one
    * invocation for each return value is allowed; if a larger number of invocations is explicitly allowed then the last
    * recorded return value is used for all remaining invocations during the replay phase.
    * <p/>
    * It's also possible to specify a sequence of values to be returned by consecutive invocations, by simply passing
    * an array, a {@linkplain Collection collection}, an {@linkplain Iterable iterable}, or an
    * {@linkplain Iterator iterator}.
    * The return type of the recorded method, however, must <em>not</em> be of one of these non-singular types.
    * If it is, the multi-valued argument will be returned by a single invocation at replay time.
    * <p/>
    * If this method is used for a constructor or {@code void} method, the given return value will be ignored,
    * but a matching invocation will be allowed during replay; it will simply do nothing.
    * <p/>
    * For a non-void method, if no return value is recorded then all invocations to it will return the appropriate
    * default value according to the method return type:
    * <ul>
    * <li>Primitive: the standard default value is returned (ie {@code false} for {@code boolean}, '\0' for
    * {@code char}, {@code 0} for {@code int}, and so on).</li>
    * <li>{@code java.util.Collection} or {@code java.util.List}: returns {@link Collections#EMPTY_LIST}</li>
    * <li>{@code java.util.Set}: returns {@link Collections#EMPTY_SET}.</li>
    * <li>{@code java.util.SortedSet}: returns an unmodifiable empty sorted set.</li>
    * <li>{@code java.util.Map}: returns {@link Collections#EMPTY_MAP}.</li>
    * <li>{@code java.util.SortedMap}: returns an unmodifiable empty sorted map.</li>
    * <li>A reference type (including {@code String} and wrapper types for primitives, and excluding the exact
    * collection types above): returns {@code null}.</li>
    * <li>An array type: an array with zero elements (empty) in each dimension is returned.</li>
    * </ul>
    * Finally, value(s) to be returned can also be determined at replay time through a {@link Delegate} instance passed
    * as argument to this method (typically created as an anonymous class).
    *
    * @param value the value to be returned when the method is replayed; must be compatible with the method's return
    * type
    *
    * @throws IllegalStateException if not currently recording an invocation
    *
    * @see #result
    * @see #returns(Object, Object...)
    */
   protected final void returns(Object value)
   {
      getCurrentPhase().addReturnValueOrValues(value);
   }

   /**
    * Specifies that the previously recorded method invocation will return a given sequence of values during replay.
    * <p/>
    * Using this method is equivalent to calling {@link #returns(Object)} two or more times in sequence, except when the
    * recorded method can return an iterable (including any {@code Collection} subtype), an iterator, or an array:
    * <ol>
    * <li>If the return type is iterable and can receive a {@link List} value, then the given sequence of values will be
    * converted into an {@code ArrayList}; this list will then be returned by matching invocations at replay time.</li>
    * <li>If the return type is {@code SortedSet} or a sub-type, then the given sequence of values will be converted
    * into a {@code TreeSet}; otherwise, if it is {@code Set} or a sub-type, then a {@code LinkedHashSet} will be
    * created to hold the values; the set will then be returned by matching invocations at replay time.</li>
    * <li>If the return type is {@code Iterator} or a sub-type, then the given sequence of values will be converted into
    * a {@code List} and the iterator created from this list will be returned by matching invocations at replay
    * time.</li>
    * <li>If the return type is an array, then the given sequence of values will be converted to an array of the same
    * type, which will be returned by matching invocations at replay time.</li>
    * </ol>
    * The current expectation will have its upper invocation count automatically set to the total number of values
    * specified to be returned. This upper limit can be overridden through the {@code maxTimes} field, if necessary.
    * <p/>
    * If this method is used for a constructor or {@code void} method, the given return values will be ignored,
    * but matching invocations will be allowed during replay; they will simply do nothing.
    *
    * @param firstValue the first value to be returned in the replay phase
    * @param remainingValues the remaining values to be returned, in the same order
    *
    * @throws IllegalStateException if not currently recording an invocation
    */
   protected final void returns(Object firstValue, Object... remainingValues)
   {
      getCurrentPhase().addSequenceOfReturnValues(firstValue, remainingValues);
   }

   // Methods for defining expectation strictness /////////////////////////////////////////////////////////////////////

   /**
    * Marks the preceding invocation as belonging to a <em>non-strict</em> expectation.
    * Note that all invocations on {@link NonStrict} mocked types/instances will be automatically considered non-strict.
    * The same is true for all invocations inside a {@link NonStrictExpectations} block.
    * <p/>
    * For a non-strict expectation, any number (including zero) of invocations with matching arguments can occur while
    * in the replay phase, in any order, and they will all produce the same result (usually, the
    * {@linkplain #result specified return value}).
    * Two or more non-strict expectations can be recorded for the same method or constructor, as long as the arguments
    * differ. Argument matchers can be used as well.
    * <p/>
    * Expected invocation counts can also be specified for a non-strict expectation (with one of the "times" fields).
    */
   protected final void notStrict()
   {
      getCurrentPhase().setNotStrict();
   }
}
