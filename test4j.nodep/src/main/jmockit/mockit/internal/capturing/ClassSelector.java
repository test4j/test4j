/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.capturing;

public interface ClassSelector
{
   boolean shouldCapture(ClassLoader definingClassLoader, String className);
}
