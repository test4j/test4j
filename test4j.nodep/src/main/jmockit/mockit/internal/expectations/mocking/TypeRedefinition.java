/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import mockit.external.asm4.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

final class TypeRedefinition extends BaseTypeRedefinition
{
   private final Object parentObject;

   TypeRedefinition(Object parentObject, MockedType typeMetadata)
   {
      super(typeMetadata.getClassType());
      this.parentObject = parentObject;
      this.typeMetadata = typeMetadata;
   }

   void redefineTypeForFinalField()
   {
      if (targetClass == null || !typeMetadata.injectable && targetClass.isInterface()) {
         throw new IllegalArgumentException("Final mock field \"" + typeMetadata.mockId + "\" must be of a class type");
      }

      Integer mockedClassId = redefineClassesFromCache();

      if (mockedClassId != null) {
         typeMetadata.buildMockingConfiguration();
         redefineMethodsAndConstructorsInTargetType();
         storeRedefinedClassesInCache(mockedClassId);
      }

      TestRun.mockFixture().registerMockedClass(targetClass);
   }

   InstanceFactory redefineType()
   {
      typeMetadata.buildMockingConfiguration();

      return redefineType(typeMetadata.declaredType);
   }

   @Override
   ExpectationsModifier createModifier(Class<?> realClass, ClassReader classReader)
   {
      ExpectationsModifier modifier = new ExpectationsModifier(realClass.getClassLoader(), classReader, typeMetadata);

      if (typeMetadata.injectable) {
         modifier.useDynamicMockingForInstanceMethods(typeMetadata);
      }

      return modifier;
   }

   @Override
   String getNameForConcreteSubclassToCreate()
   {
      return GeneratedClasses.getNameForGeneratedClass(parentObject.getClass(), typeMetadata.mockId);
   }
}
