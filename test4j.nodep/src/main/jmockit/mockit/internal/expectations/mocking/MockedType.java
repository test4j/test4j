/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.annotation.*;
import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;
import static mockit.internal.util.Utilities.*;

import mockit.*;
import mockit.internal.filtering.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

@SuppressWarnings({"ClassWithTooManyFields", "EqualsAndHashcode"})
public final class MockedType
{
   private static final boolean ANNOTATED_MOCK_PARAMETERS_ONLY =
      "annotated".equals(System.getProperty("jmockit-mockParameters"));

   @SuppressWarnings("UnusedDeclaration")
   @Mocked private static final Object DUMMY = null;
   private static final int DUMMY_HASHCODE;

   static
   {
      int h = 0;

      try {
         h = MockedType.class.getDeclaredField("DUMMY").getAnnotation(Mocked.class).hashCode();
      }
      catch (NoSuchFieldException ignore) {}

      DUMMY_HASHCODE = h;
   }

   public final Field field;
   public final boolean fieldFromTestClass;
   private final int accessModifiers;
   private final Mocked mocked;
   final Capturing capturing;
   final Cascading cascading;
   public final boolean nonStrict;
   public final boolean injectable;
   public final Type declaredType;
   public final String mockId;
   MockingConfiguration mockingCfg;
   Object providedValue;

   public MockedType(Field field, boolean fromTestClass)
   {
      this.field = field;
      fieldFromTestClass = fromTestClass;
      accessModifiers = field.getModifiers();
      mocked = field.getAnnotation(Mocked.class);
      capturing = field.getAnnotation(Capturing.class);
      cascading = field.getAnnotation(Cascading.class);
      nonStrict = field.isAnnotationPresent(NonStrict.class);
      Injectable injectableAnnotation = field.getAnnotation(Injectable.class);
      injectable = injectableAnnotation != null;
      declaredType = field.getGenericType();
      mockId = field.getName();
      providedValue = getDefaultInjectableValue(injectableAnnotation);
      registerCascadingIfSpecified();
   }

   private Object getDefaultInjectableValue(Injectable annotation)
   {
      if (annotation != null) {
         String value = annotation.value();

         if (value.length() > 0) {
            Class<?> injectableClass = getClassType();

            if (injectableClass == char.class) {
               return value.charAt(0);
            }
            else if (injectableClass == String.class) {
               return value;
            }
            else if (injectableClass.isPrimitive()) {
               Class<?> wrapperClass = PRIMITIVE_TO_WRAPPER.get(injectableClass);
               Class<?>[] constructorParameters = {String.class};
               return newInstance(wrapperClass, constructorParameters, value);
            }
            else if (injectableClass.isEnum()) {
               @SuppressWarnings({"rawtypes", "unchecked"})
               Class<? extends Enum> enumType = (Class<? extends Enum>) injectableClass;
               return Enum.valueOf(enumType, value);
            }
         }
      }

      return null;
   }
 
   private void registerCascadingIfSpecified()
   {
      if (cascading != null) {
         String mockedTypeDesc = getClassType().getName().replace('.', '/');
         TestRun.getExecutingTest().addCascadingType(mockedTypeDesc, this);
      }
   }

   MockedType(
      String testClassDesc, String testMethodDesc, int paramIndex, Type parameterType,
      Annotation[] annotationsOnParameter)
   {
      field = null;
      fieldFromTestClass = false;
      accessModifiers = 0;
      mocked = getAnnotation(annotationsOnParameter, Mocked.class);
      capturing = getAnnotation(annotationsOnParameter, Capturing.class);
      cascading = getAnnotation(annotationsOnParameter, Cascading.class);
      nonStrict = getAnnotation(annotationsOnParameter, NonStrict.class) != null;
      Injectable injectableAnnotation = getAnnotation(annotationsOnParameter, Injectable.class);
      injectable = injectableAnnotation != null;
      declaredType = parameterType;
      mockId = ParameterNames.getName(testClassDesc, testMethodDesc, paramIndex);
      providedValue = getDefaultInjectableValue(injectableAnnotation);
      registerCascadingIfSpecified();
   }

   MockedType(Class<?> cascadedType)
   {
      field = null;
      fieldFromTestClass = false;
      accessModifiers = 0;
      mocked = null;
      capturing = null;
      cascading = null;
      nonStrict = true;
      injectable = true;
      declaredType = cascadedType;
      mockId = "cascaded_" + cascadedType.getName();
   }

   public Class<?> getClassType()
   {
      if (declaredType instanceof Class) {
         return (Class<?>) declaredType;
      }

      if (declaredType instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType) declaredType;
         return (Class<?>) parameterizedType.getRawType();
      }

      return null;
   }

   boolean isMockField()
   {
      return (isAnnotated() || !fieldFromTestClass && !isPrivate(accessModifiers)) && isMockableType();
   }

   boolean isAnnotated()
   {
      return mocked != null || capturing != null || cascading != null || nonStrict || injectable;
   }

   boolean isMockableType()
   {
      if (ANNOTATED_MOCK_PARAMETERS_ONLY && field == null && !isAnnotated()) {
         return false;
      }

      if (!(declaredType instanceof Class)) {
         return true;
      }
      
      Class<?> classType = (Class<?>) declaredType;

      if (classType.isPrimitive() || classType.isArray() || classType == Integer.class) {
         return false;
      }
      else if (injectable && providedValue != null) {
         if (classType == String.class || classType.isEnum()) {
            return false;
         }
      }

      return true;
   }

   boolean isFinalFieldOrParameter() { return field == null || isFinal(accessModifiers); }

   void buildMockingConfiguration()
   {
      if (mocked == null) {
         return;
      }

      String[] filters = getFilters();

      if (filters.length > 0) {
         mockingCfg = new MockingConfiguration(filters, !mocked.inverse());
      }
   }

   private String[] getFilters()
   {
      String[] filters = mocked.methods();

      if (filters.length == 0) {
         filters = mocked.value();
      }

      return filters;
   }

   boolean isClassInitializationToBeStubbedOut() { return mocked != null && mocked.stubOutClassInitialization(); }

   boolean withInstancesToCapture() { return getMaxInstancesToCapture() > 0; }

   int getMaxInstancesToCapture()
   {
      return capturing == null ? 0 : capturing.maxInstances();
   }

   String getRealClassName() { return mocked == null ? "" : mocked.realClassName(); }

   public Object getValueToInject(Object objectWithFields)
   {
      if (field == null) {
         return providedValue;
      }

      Object value = getFieldValue(field, objectWithFields);

      if (!injectable) {
         return value;
      }

      if (value == null) {
         return providedValue;
      }

      Class<?> fieldType = field.getType();

      if (!fieldType.isPrimitive()) {
         return value;
      }

      Object defaultValue = DefaultValues.defaultValueForPrimitiveType(fieldType);

      return value.equals(defaultValue) ? providedValue : value;
   }

   @Override
   public int hashCode()
   {
      int result = declaredType.hashCode();

      if (isFinal(accessModifiers)) {
         result *= 31;
      }

      if (injectable) {
         result *= 37;
      }

      if (mocked != null) {
         int h = mocked.hashCode();

         if (h != DUMMY_HASHCODE) {
            result = 31 * result + h;
         }
      }

      return result;
   }
}
