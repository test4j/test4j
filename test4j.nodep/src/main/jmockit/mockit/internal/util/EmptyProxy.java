/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

/**
 * This marker interface exists only to guarantee that JMockit can get the bytecode definition of
 * each Proxy class it creates through <code>java.lang.reflect.Proxy</code>.
 * If such a class is created before JMockit is initialized, its bytecode won't be stored in
 * JMockit's cache. And since the JDK uses an internal cache for proxy classes, it won't create a
 * new one, therefore not going through the ProxyRegistrationTransformer. So, by always implementing
 * this additional interface, we can guarantee a new proxy class will be created when JMockit first
 * requests it for a given interface.
 */
public interface EmptyProxy {}
