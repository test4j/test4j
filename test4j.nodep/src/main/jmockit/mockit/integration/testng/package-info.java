/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */

/**
 * Provides integration with <em>TestNG</em> test runners, for versions 5.14+.
 * Contains the {@link mockit.integration.testng.Initializer} test listener class that can be specified to TestNG at
 * startup, as well as the "startup mock" implementation for integration with the TestNG test runner.
 * <p/>
 * This integration provides the same benefits to test code as the one for JUnit 4:
 * <ol>
 * <li>
 * Unexpected invocations specified through the Mockups or the Expectations API are automatically verified just before
 * the execution of a test ends (specifically, immediately after the execution of the test method).
 * </li>
 * <li>
 * Any mock classes applied with the Mockups API from inside a method annotated as a {@code @Test} or a
 * {@code @BeforeMethod} will be discarded right after the execution of the test or the test method, respectively.
 * </li>
 * <li>
 * Any {@linkplain mockit.MockClass mock class} set up for the whole test class (either through a call to
 * {@link mockit.Mockit#setUpMocks} from inside a {@code @BeforeClass} method, or by annotating the test class with
 * {@link mockit.UsingMocksAndStubs}) will only apply to the tests in this same test class.
 * That is, you should not explicitly tell JMockit to restore the mocked classes in an {@code @AfterClass} method.
 * </li>
 * <li>
 * Test methods will accept <em>mock parameters</em>, whose values are mocked instances automatically created by JMockit
 * and passed by the test runner when the test method gets executed. 
 * </li>
 * </ol>
 */
package mockit.integration.testng;
