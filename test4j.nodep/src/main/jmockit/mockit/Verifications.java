/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.expectations.argumentMatching.*;
import mockit.internal.state.*;
import mockit.internal.expectations.*;
import mockit.internal.util.*;

/**
 * A set of expectations on mocked types and/or instances to be verified against the invocations which actually occurred
 * during the test.
 * As such, these so called <em>verification blocks</em> can only appear <em>after</em> having exercised the code under
 * test.
 * <pre>
 *
 * // Exercise tested code, then verify that expected invocations occurred in any order:
 * new Verifications() {{
 *    <strong>mock1</strong>.expectedMethod(<em>anyInt</em>);
 *    <strong>mock2</strong>.anotherExpectedMethod(1, "test"); <em>times</em> = 2;
 * }};</pre>
 * The relative order of invocations is not relevant here; for that, use {@link VerificationsInOrder} instead.
 * Any subset of actual invocations can be verified; to make sure that <em>all</em> have been, if needed, use
 * {@link FullVerifications} instead.
 * <p/>
 * For an expectation inside a verification block to succeed (ie, pass verification), a matching invocation must have
 * occurred during the replay (exercise) phase of the test, <em>at least once</em>.
 * This is only the <em>default</em> verification behavior, though. Just like with recorded expectations, it's possible
 * to specify different invocation count constraints through the {@link #times}, {@link #minTimes}, and
 * {@link #maxTimes} fields.
 * <p/>
 * Besides the mock fields and mock parameters available to the test, a verification block can also <em>import</em>
 * local mock fields declared inside expectation blocks, by declaring a local field of the desired mocked type.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#verification">In the
 * Tutorial</a>
 *
 * @see Expectations#notStrict()
 * @see NonStrict
 * @see #Verifications()
 * @see #Verifications(int)
 */
public abstract class Verifications extends Invocations
{
   final BaseVerificationPhase verificationPhase;

   /**
    * Begins a block unordered verifications on the mocked types/instances invoked during the replay phase of the test.
    *
    * @see #Verifications(int)
    */
   protected Verifications() { this(false); }

   /**
    * Begins a block of unordered verifications on the mocked types/instances invoked during the replay phase of the
    * test, considering that such invocations occurred in a given number of iterations.
    * <p/>
    * The effect of specifying a (positive) number of iterations is equivalent to setting to that number the lower and
    * upper invocation count limits for each expectation verified inside the block.
    * If, however, the lower/upper limit is explicitly specified for an expectation, the given number of iterations
    * becomes a multiplier.
    * When not specified, at least one matching invocation will be required to have occurred; therefore, specifying
    * <em>1 (one)</em> iteration is different from not specifying the number of iterations at all.
    * <p/>
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#iterations">In the Tutorial</a>
    *
    * @param numberOfIterations the positive number of iterations for the whole set of invocations verified inside the
    * block
    *
    * @see #Verifications()
    * @see #times
    * @see #minTimes
    * @see #maxTimes
    */
   protected Verifications(int numberOfIterations)
   {
      this(false);
      verificationPhase.setNumberOfIterations(numberOfIterations);
   }

   Verifications(boolean inOrder)
   {
      RecordAndReplayExecution instance = TestRun.getExecutingTest().getRecordAndReplayForVerifications();

      Map<Type,Object> availableLocalMocks = instance.getLocalMocks();

      if (!availableLocalMocks.isEmpty()) {
         importMocksIntoLocalFields(getClass(), availableLocalMocks);
      }

      verificationPhase = instance.startVerifications(inOrder);
   }

   private void importMocksIntoLocalFields(Class<?> ownerClass, Map<Type, Object> localMocks)
   {
      Field[] fields = ownerClass.getDeclaredFields();

      for (Field fieldToImport : fields) {
         if (!Modifier.isFinal(fieldToImport.getModifiers())) {
            importMockIntoLocalField(localMocks, fieldToImport);
         }
      }

      Class<?> superClass = ownerClass.getSuperclass();

      if (
         superClass != Verifications.class && superClass != VerificationsInOrder.class &&
         superClass != FullVerifications.class && superClass != FullVerificationsInOrder.class
      ) {
         importMocksIntoLocalFields(superClass, localMocks);
      }
   }

   private void importMockIntoLocalField(Map<Type, Object> localMocks, Field field)
   {
      Type mockedType = field.getGenericType();
      Object owner = localMocks.get(mockedType);

      if (owner != null) {
         Object mock = Utilities.getField(owner.getClass(), mockedType, owner);
         Utilities.setFieldValue(field, this, mock);
      }
   }

   @Override
   final BaseVerificationPhase getCurrentPhase() { return verificationPhase; }

   /**
    * Captures the argument value passed into the associated expectation parameter, for a matching invocation that
    * occurred when the tested code was exercised.
    * This method should be used in a local variable assignment expression inside a verification block.
    * For example:
    * <pre>
    *    new Verifications() {{
    *       String name;
    *       int age;
    *       personDAOMock.create(age = withCapture(), name = withCapture());
    *       assertFalse(name.isEmpty());
    *       assertTrue(age >= 18);
    *    }};</pre>
    * <p/>
    * If there is more than one matching invocation, then only the last one is considered.
    *
    * @return the captured argument value
    *
    * @see #withCapture(java.util.List)
    */
   protected final <T> T withCapture()
   {
      verificationPhase.addArgMatcher(AlwaysTrueMatcher.INSTANCE);
      return null;
   }
}
