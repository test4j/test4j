/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

/**
 * An empty interface to be used with the {@link Expectations#result} field or the
 * <a href="Expectations.html#with(mockit.Delegate)">{@code Invocations#with(Delegate)}</a> method, allowing test code
 * to define invocation results or argument matching rules based on test-specific logic.
 * <p/>
 * The name and parameters of the delegate method must fit one of the following alternatives:
 * <ul>
 * <li>
 *    A method matching the signature of the recorded method/constructor.
 *    That is, they should have the same name and parameters. In the case of delegating a constructor, a delegate
 *    <em>method</em> should still be used, with name "$init".
 *    In this case, the delegate class can have any number of additional methods.
 * </li>
 * <li>
 *    A <em>single</em> method with the same or a different name as the recorded method, but having the <em>same</em>
 *    parameters.
 * </li>
 * <li>
 *    A <em>single</em> method with the same or a different name, but having <em>no</em> parameters even if the recorded
 *    method/constructor has one or more of them.
 *    In this case, argument values passed during replay are simply ignored as far as the delegate is concerned.
 * </li>
 * </ul>
 * Additionally, in any of the above three alternatives, the delegate method is allowed to declare an additional
 * parameter of type {@link Invocation}.
 * If said parameter is present, it must be the first one.
 * At replay time, it will always receive a suitable object representing the associated invocation which happened to
 * match the recorded expectation.
 * <p/>
 * When used with the {@code result} field, the result of a delegate method execution can be any return value compatible
 * with the recorded method's return type, or a thrown error/exception.
 * <p/>
 * When used with the {@code with} method, the delegate method must return a {@code boolean}, being {@code true} for a
 * successfully matched argument or {@code false} otherwise.
 * <p/>
 * At replay time, when the mocked method/constructor is called the corresponding "delegate" method will be called.
 * The arguments passed to the delegate method will be the same as those received by the recorded invocation during
 * replay.
 * Even {@code static} methods in the mocked type can have delegates, which in turn can be static or not.
 * The same is true for {@code private}, {@code final}, and {@code native} methods.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#delegates">In the Tutorial</a>
 * <p/>
 * Sample tests:
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/easymock/test/org/easymock/samples/DocumentManager_JMockit_Test.java">DocumentManager_JMockit_Test</a>
 *
 * @param <T> the type of the argument to be matched, when used with the {@code with(Delegate&lt;T>)} method
 */
public interface Delegate<T>
{
}
