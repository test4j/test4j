/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.capturing;

import static java.lang.reflect.Proxy.*;

import static mockit.internal.util.GeneratedClasses.*;

final class CapturedType
{
   final Class<?> baseType;

   CapturedType(Class<?> baseType) { this.baseType = baseType; }

   boolean isToBeCaptured(Class<?> aClass)
   {
      return
         aClass != baseType && !isProxyClass(aClass) && baseType.isAssignableFrom(aClass) &&
         !isGeneratedClass(aClass.getName());
   }
}
