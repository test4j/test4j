/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import mockit.*;
import mockit.internal.mockups.*;

public final class MockClasses
{
   final MockInstances regularMocks;
   final MockInstances startupMocks;
   private final MockStates mockStates;

   MockClasses()
   {
      regularMocks = new MockInstances();
      startupMocks = new MockInstances();
      mockStates = new MockStates();
   }

   public MockInstances getRegularMocks() { return regularMocks; }
   public MockInstances getMocks(boolean forStartup) { return forStartup ? startupMocks : regularMocks; }
   public MockStates getMockStates() { return mockStates; }

   public MockUp<?> findMock(Class<?> mockClass)
   {
      MockUp<?> mock = regularMocks.findMock(mockClass);
      if (mock == null) mock = startupMocks.findMock(mockClass);
      return mock;
   }

   public void removeMock(MockUp<?> mock)
   {
      regularMocks.removeInstance(mock);
      startupMocks.removeInstance(mock);
   }
}
