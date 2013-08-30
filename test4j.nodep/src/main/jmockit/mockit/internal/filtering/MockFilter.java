/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.filtering;

public interface MockFilter
{
   boolean matches(String name, String desc);
}
