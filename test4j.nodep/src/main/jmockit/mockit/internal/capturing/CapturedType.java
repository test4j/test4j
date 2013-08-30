/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.capturing;

import java.lang.reflect.*;

import static mockit.internal.util.Utilities.*;

final class CapturedType
{
   final Class<?> baseType;
   private final ClassSelector classSelector;

   CapturedType(Class<?> baseType, ClassSelector classSelector)
   {
      this.baseType = baseType;
      this.classSelector = classSelector;
   }

   boolean isToBeCaptured(Class<?> aClass)
   {
      return
         aClass != baseType && !Proxy.isProxyClass(aClass) && baseType.isAssignableFrom(aClass) &&
         isToBeCaptured(aClass.getClassLoader(), aClass.getName());
   }

   boolean isToBeCaptured(ClassLoader cl, String className)
   {
      return !isGeneratedClass(className) && classSelector.shouldCapture(cl, className);
   }
}
