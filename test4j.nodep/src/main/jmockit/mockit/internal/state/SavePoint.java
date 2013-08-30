/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.util.*;

public final class SavePoint
{
   private final Set<String> previousTransformedClasses;
   private final Map<Class<?>, byte[]> previousRedefinedClasses;
   private final int previousMockInstancesCount;
   private List<Runnable> rollbackActions;

   public SavePoint()
   {
      MockFixture mockFixture = TestRun.mockFixture();
      previousTransformedClasses = mockFixture.getTransformedClasses();
      previousRedefinedClasses = mockFixture.getRedefinedClasses();
      previousMockInstancesCount = TestRun.getMockClasses().getRegularMocks().getInstanceCount();
   }

   public synchronized void addRollbackAction(Runnable action)
   {
      if (action != null) {
         if (rollbackActions == null) {
            rollbackActions = new ArrayList<Runnable>();
         }

         rollbackActions.add(action);
      }
   }

   public synchronized void rollback()
   {
      if (rollbackActions != null) {
         for (Runnable action : rollbackActions) {
            action.run();
         }
      }

      MockFixture mockFixture = TestRun.mockFixture();
      mockFixture.restoreTransformedClasses(previousTransformedClasses);
      mockFixture.restoreRedefinedClasses(previousRedefinedClasses);
      TestRun.getMockClasses().getRegularMocks().removeInstances(previousMockInstancesCount);
   }

   public static void registerNewActiveSavePoint()
   {
      TestRun.setSavePointForTestClass(new SavePoint());
   }

   public static void rollbackForTestClass()
   {
      SavePoint savePoint = TestRun.getSavePointForTestClass();

      if (savePoint != null) {
         savePoint.rollback();
         TestRun.setSavePointForTestClass(null);
      }
   }
}
