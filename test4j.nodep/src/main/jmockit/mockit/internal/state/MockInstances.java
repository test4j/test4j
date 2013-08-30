/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.util.*;

import mockit.internal.util.*;

/**
 * Holds a list of instances of mock classes (either regular classes provided by client code, or
 * startup mock classes provided internally by JMockit or by external jars).
 * <p/>
 * This is needed to allow each redefined real method to call the corresponding mock method on the
 * single global instance for the mock class.
 */
public final class MockInstances
{
   private final List<Object> mocks = new ArrayList<Object>();
   private final Map<Object, Object> mockedInstancesToMocks = new HashMap<Object, Object>();

   public boolean containsInstance(Object mock)
   {
      return mocks.contains(mock);
   }

   public int getInstanceCount()
   {
      return mocks.size();
   }

   public Object getMock(int index)
   {
      return mocks.get(index);
   }

   public Object getMock(Class<?> mockClass, Object mockedInstance)
   {
      Object mock = mockedInstancesToMocks.get(mockedInstance);

      if (mock == null) {
         mock = Utilities.newInstance(mockClass);
         mockedInstancesToMocks.put(mockedInstance, mock);
      }

      return mock;
   }

   public int addMock(Object mock)
   {
      mocks.add(mock);
      return mocks.size() - 1;
   }

   void removeInstances(int fromIndex)
   {
      for (int i = mocks.size() - 1; i >= fromIndex; i--) {
         mocks.remove(i);
      }
   }

   public void discardInstances()
   {
      mocks.clear();
   }
}
