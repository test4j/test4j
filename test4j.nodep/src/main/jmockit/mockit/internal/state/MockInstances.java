/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.util.*;

import mockit.*;
import mockit.internal.util.*;

/**
 * Holds a list of instances of mock classes (either regular classes provided by client code, or startup mock classes
 * provided internally by JMockit or by external jars).
 * <p/>
 * This is needed to allow each redefined real method to call the corresponding mock method on the instance of the mock
 * class (unless the mock method is {@code static}).
 */
public final class MockInstances
{
   private final List<MockUp<?>> mocks = new ArrayList<MockUp<?>>();

   int getInstanceCount() { return mocks.size(); }
   MockUp<?> getMock(int index) { return mocks.get(index); }

   public int addMock(MockUp<?> mock)
   {
      int i = Utilities.indexOfReference(mocks, mock);

      if (i < 0) {
         i = mocks.size();
         mocks.add(mock);
      }

      return i;
   }

   void removeInstance(MockUp<?> mock)
   {
      int i = Utilities.indexOfReference(mocks, mock);

      if (i >= 0) {
         mocks.set(i, null);
      }
   }

   void removeInstances(int fromIndex)
   {
      for (int i = mocks.size() - 1; i >= fromIndex; i--) {
         mocks.remove(i);
      }
   }

   MockUp<?> findMock(Class<?> mockClass)
   {
      int n = mocks.size();

      for (int i = 0; i < n; i++) {
         MockUp<?> mock = mocks.get(i);

         if (mock != null && mock.getClass() == mockClass) {
            return mock;
         }
      }

      return null;
   }
}
