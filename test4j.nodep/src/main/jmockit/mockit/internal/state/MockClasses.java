/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import mockit.internal.annotations.*;

public final class MockClasses
{
   final MockInstances regularMocks;
   final MockInstances startupMocks;
   private final AnnotatedMockStates annotatedMockStates;

   MockClasses()
   {
      regularMocks = new MockInstances();
      startupMocks = new MockInstances();
      annotatedMockStates = new AnnotatedMockStates();
   }

   public MockInstances getRegularMocks() { return regularMocks; }
   public MockInstances getMocks(boolean forStartup) { return forStartup ? startupMocks : regularMocks; }
   public AnnotatedMockStates getMockStates() { return annotatedMockStates; }
}
