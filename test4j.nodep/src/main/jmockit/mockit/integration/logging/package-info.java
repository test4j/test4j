/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */

/**
 * Provides integration with logging frameworks such as Log4j, SLF4j, and standard JDK logging, with the purpose of
 * isolating code under test from them.
 * <p/>
 * Contains one {@linkplain mockit.MockClass mock class} for each supported logging framework, to be used with the
 * {@link mockit.UsingMocksAndStubs} annotation when applied to a test class.
 */
package mockit.integration.logging;
