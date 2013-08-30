/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.lang.instrument.*;
import java.security.*;

import mockit.internal.state.*;
import mockit.internal.util.*;

final class ProxyClassfileSavingTransformer implements ClassFileTransformer
{
   public byte[] transform(
      ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
   {
      if (classBeingRedefined == null) {
         saveClassfileIfProxyClass(className, classfileBuffer);
      }

      return null;
   }

   private void saveClassfileIfProxyClass(String className, byte[] classfileBuffer)
   {
      String proxyClassName = null;
      int p = className.indexOf("$Proxy");

      if (p >= 0 && Utilities.isAllNumeric(className, p + 5)) {
         if (p == 0) {
            proxyClassName = className;
         }
         else if (className.charAt(p - 1) == '/') {
            proxyClassName = className.replace('/', '.');
         }
      }

      if (proxyClassName != null) {
         TestRun.proxyClasses().add(proxyClassName, classfileBuffer);
      }
   }
}
