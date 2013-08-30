/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import mockit.external.asm4.*;
import mockit.internal.util.*;

public final class CascadingTypeRedefinition extends BaseTypeRedefinition
{
   public CascadingTypeRedefinition(Class<?> mockedType)
   {
      super(mockedType);
      typeMetadata = new MockedType(mockedType);
   }

   public InstanceFactory redefineType()
   {
      return redefineType(targetClass);
   }

   @Override
   ExpectationsModifier createModifier(Class<?> realClass, ClassReader classReader)
   {
      ExpectationsModifier modifier = new ExpectationsModifier(realClass.getClassLoader(), classReader, null);
      modifier.useDynamicMockingForInstanceMethods(null);
      return modifier;
   }

   @Override
   String getNameForConcreteSubclassToCreate()
   {
      return Utilities.GENERATED_SUBCLASS_PREFIX + targetClass.getSimpleName();
   }
}