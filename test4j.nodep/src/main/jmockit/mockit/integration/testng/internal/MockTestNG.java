/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng.internal;

import org.testng.*;

import mockit.*;
import mockit.internal.util.*;

@MockClass(realClass = TestNG.class, instantiation = Instantiation.PerMockSetup)
public final class MockTestNG
{
   public TestNG it;

   @Mock(reentrant = true)
   public void init(boolean useDefaultListeners)
   {
      Utilities.invoke(TestNG.class, it, "init", useDefaultListeners);
      TestNGRunnerDecorator.registerWithTestNG(it);
   }
}
