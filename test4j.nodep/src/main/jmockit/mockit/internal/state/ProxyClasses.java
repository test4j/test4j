/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.util.*;

/**
 * Holds a map of names of proxy classes to the corresponding class files (bytecode arrays).
 * <p/>
 * Such classes are created at runtime, so they don't have class files stored on disk.
 * This map allows them to be redefined.
 */
public final class ProxyClasses
{
   private final Map<String, byte[]> classfiles = new HashMap<String, byte[]>(4);

   ProxyClasses() {}

   public byte[] getClassfile(String proxyClassName)
   {
      return classfiles.get(proxyClassName);
   }

   public void add(String className, byte[] proxyClassfile)
   {
      classfiles.put(className, proxyClassfile);
   }
}
