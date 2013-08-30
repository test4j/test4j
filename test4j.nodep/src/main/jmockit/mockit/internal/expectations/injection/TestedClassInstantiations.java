/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.injection;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import javax.inject.*;
import static java.lang.reflect.Modifier.*;

import mockit.*;
import mockit.internal.expectations.mocking.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

import static mockit.internal.util.Utilities.*;

public final class TestedClassInstantiations
{
   private static final Class<? extends Annotation> INJECT_CLASS;

   static
   {
      Class<? extends Annotation> injectClass;
      ClassLoader cl = TestedClassInstantiations.class.getClassLoader();

      try {
         //noinspection unchecked
         injectClass = (Class<? extends Annotation>) Class.forName("javax.inject.Inject", false, cl);
      }
      catch (ClassNotFoundException ignore) { injectClass = null; }

      INJECT_CLASS = injectClass;
   }

   private final List<TestedField> testedFields;
   private final List<MockedType> injectableFields;
   private List<MockedType> injectables;
   private final List<MockedType> consumedInjectables;
   private Object testClassInstance;
   private Type typeOfInjectionPoint;

   private final class TestedField
   {
      final Field testedField;
      private List<Field> targetFields;

      TestedField(Field field) { testedField = field; }

      void instantiateWithInjectableValues()
      {
         Object testedObject = getFieldValue(testedField, testClassInstance);
         boolean requiresJavaxInject = false;

         if (testedObject == null && !isFinal(testedField.getModifiers())) {
            TestedObjectCreation testedObjectCreation = new TestedObjectCreation();

            testedObject = testedObjectCreation.create(testedField);
            setFieldValue(testedField, testClassInstance, testedObject);

            requiresJavaxInject = testedObjectCreation.constructorAnnotatedWithJavaxInject;
         }

         if (testedObject != null) {
            FieldInjection fieldInjection = new FieldInjection(testedObject, requiresJavaxInject);

            if (targetFields == null) {
               targetFields = fieldInjection.findAllTargetInstanceFieldsInTestedClassHierarchy();
            }

            fieldInjection.injectIntoEligibleFields(targetFields);
         }
      }
   }

   public TestedClassInstantiations()
   {
      testedFields = new LinkedList<TestedField>();
      injectableFields = new ArrayList<MockedType>();
      consumedInjectables = new ArrayList<MockedType>();
   }

   public boolean findTestedAndInjectableFields(Class<?> testClass)
   {
      new ParameterNameExtractor(true).extractNames(testClass);

      Field[] fieldsInTestClass = testClass.getDeclaredFields();

      for (Field field : fieldsInTestClass) {
         if (field.isAnnotationPresent(Tested.class)) {
            testedFields.add(new TestedField(field));
         }
         else {
            MockedType mockedType = new MockedType(field, true);

            if (mockedType.injectable) {
               injectableFields.add(mockedType);
            }
         }
      }

      return !testedFields.isEmpty();
   }

   public void assignNewInstancesToTestedFields(Object testClassInstance)
   {
      this.testClassInstance = testClassInstance;

      buildListsOfInjectables();

      for (TestedField testedField : testedFields) {
         testedField.instantiateWithInjectableValues();
         consumedInjectables.clear();
      }
   }

   private void buildListsOfInjectables()
   {
      ParameterTypeRedefinitions paramTypeRedefs = TestRun.getExecutingTest().getParameterTypeRedefinitions();

      if (paramTypeRedefs == null) {
         injectables = injectableFields;
      }
      else {
         injectables = new ArrayList<MockedType>(injectableFields);
         injectables.addAll(paramTypeRedefs.getInjectableParameters());
      }
   }

   void setTypeOfInjectionPoint(Type parameterOrFieldType) { typeOfInjectionPoint = parameterOrFieldType; }

   boolean hasSameTypeAsInjectionPoint(MockedType injectable)
   {
      return isSameTypeAsInjectionPoint(injectable.declaredType);
   }

   boolean isSameTypeAsInjectionPoint(Type injectableType)
   {
      if (typeOfInjectionPoint.equals(injectableType)) return true;

      if (INJECT_CLASS != null && typeOfInjectionPoint instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType) typeOfInjectionPoint;

         if (parameterizedType.getRawType() == Provider.class) {
            Type providedType = parameterizedType.getActualTypeArguments()[0];
            return providedType.equals(injectableType);
         }
      }

      return false;
   }

   private Object getValueToInject(MockedType injectable)
   {
      if (consumedInjectables.contains(injectable)) {
         return null;
      }

      Object value = injectable.getValueToInject(testClassInstance);

      if (value != null) {
         consumedInjectables.add(injectable);
      }

      return value;
   }

   private Object wrapInProviderIfNeeded(Type type, final Object value)
   {
      if (
         INJECT_CLASS != null && type instanceof ParameterizedType && !(value instanceof Provider) &&
         ((ParameterizedType) type).getRawType() == Provider.class
      ) {
         return new Provider<Object>() { public Object get() { return value; } };
      }

      return value;
   }

   private final class TestedObjectCreation
   {
      private Class<?> testedClass;
      private Constructor<?> constructor;
      private List<MockedType> injectablesForConstructor;
      private Type[] parameterTypes;
      boolean constructorAnnotatedWithJavaxInject;

      Object create(Field testedField)
      {
         testedClass = testedField.getType();

         new ConstructorSearch().findConstructorAccordingToAccessibilityAndAvailableInjectables();

         if (constructor == null) {
            throw new IllegalArgumentException(
               "No constructor in " + testedClass + " that can be satisfied by available injectables");
         }

         return new ConstructorInjection().instantiate();
      }

      MockedType findNextInjectableForVarargsParameter()
      {
         for (MockedType injectable : injectables) {
            if (hasSameTypeAsInjectionPoint(injectable) && !consumedInjectables.contains(injectable)) {
               return injectable;
            }
         }

         return null;
      }

      private final class ConstructorSearch
      {
         private final String testedClassDesc;

         ConstructorSearch()
         {
            testedClassDesc = new ParameterNameExtractor(false).extractNames(testedClass);
            injectablesForConstructor = new ArrayList<MockedType>();
         }

         void findConstructorAccordingToAccessibilityAndAvailableInjectables()
         {
            constructor = null;

            Constructor<?>[] constructors = testedClass.getDeclaredConstructors();

            if (INJECT_CLASS != null && findSingleInjectAnnotatedConstructor(constructors)) {
               constructorAnnotatedWithJavaxInject = true;
            }
            else {
               findSatisfiedConstructorWithMostParameters(constructors);
            }
         }

         private boolean findSingleInjectAnnotatedConstructor(Constructor<?>[] constructors)
         {
            for (Constructor<?> c : constructors) {
               if (c.isAnnotationPresent(INJECT_CLASS)) {
                  List<MockedType> injectablesFound = findAvailableInjectablesForConstructor(c);

                  if (injectablesFound != null) {
                     injectablesForConstructor = injectablesFound;
                     constructor = c;
                  }

                  return true;
               }
            }

            return false;
         }

         private void findSatisfiedConstructorWithMostParameters(Constructor<?>[] constructors)
         {
            Arrays.sort(constructors, new Comparator<Constructor<?>>() {
               static final int ACCESS = PUBLIC + PROTECTED + PRIVATE;

               public int compare(Constructor<?> c1, Constructor<?> c2)
               {
                  int m1 = ACCESS & c1.getModifiers();
                  int m2 = ACCESS & c2.getModifiers();
                  if (m1 == m2) return 0;
                  if (m1 == PUBLIC) return -1;
                  if (m2 == PUBLIC) return 1;
                  if (m1 == PROTECTED) return -1;
                  if (m2 == PROTECTED) return 1;
                  if (m2 == PRIVATE) return -1;
                  return 1;
               }
            });

            for (Constructor<?> c : constructors) {
               List<MockedType> injectablesFound = findAvailableInjectablesForConstructor(c);

               if (
                  injectablesFound != null &&
                  (constructor == null ||
                   c.getModifiers() == constructor.getModifiers() &&
                   injectablesFound.size() >= injectablesForConstructor.size())
               ) {
                  injectablesForConstructor = injectablesFound;
                  constructor = c;
               }
            }
         }

         private List<MockedType> findAvailableInjectablesForConstructor(Constructor<?> candidate)
         {
            parameterTypes = candidate.getGenericParameterTypes();
            int n = parameterTypes.length;
            List<MockedType> injectablesFound = new ArrayList<MockedType>(n);
            boolean varArgs = candidate.isVarArgs();

            if (varArgs) {
               n--;
            }

            String constructorDesc = "<init>" + mockit.external.asm4.Type.getConstructorDescriptor(candidate);

            for (int i = 0; i < n; i++) {
               setTypeOfInjectionPoint(parameterTypes[i]);

               String parameterName = ParameterNames.getName(testedClassDesc, constructorDesc, i);
               MockedType injectable = parameterName == null ? null : findInjectable(parameterName);

               if (injectable == null || injectablesFound.contains(injectable)) {
                  return null;
               }

               injectablesFound.add(injectable);
            }

            if (varArgs) {
               MockedType injectable = hasInjectedValuesForVarargsParameter(n);

               if (injectable != null) {
                  injectablesFound.add(injectable);
               }
            }

            return injectablesFound;
         }

         private MockedType findInjectable(String nameOfInjectionPoint)
         {
            boolean multipleInjectablesFound = false;
            MockedType found = null;

            for (MockedType injectable : injectables) {
               if (hasSameTypeAsInjectionPoint(injectable)) {
                  if (found == null) {
                     found = injectable;
                  }
                  else {
                     if (nameOfInjectionPoint.equals(injectable.mockId)) {
                        return injectable;
                     }

                     multipleInjectablesFound = true;
                  }
               }
            }

            if (multipleInjectablesFound && !nameOfInjectionPoint.equals(found.mockId)) {
               return null;
            }

            return found;
         }

         private MockedType hasInjectedValuesForVarargsParameter(int varargsParameterIndex)
         {
            getTypeOfInjectionPointFromVarargsParameter(varargsParameterIndex);
            return findNextInjectableForVarargsParameter();
         }
      }

      private Type getTypeOfInjectionPointFromVarargsParameter(int varargsParameterIndex)
      {
         Type parameterType = parameterTypes[varargsParameterIndex];

         if (parameterType instanceof Class<?>) {
            parameterType = ((Class<?>) parameterType).getComponentType();
         }
         else {
            parameterType = ((GenericArrayType) parameterType).getGenericComponentType();
         }

         setTypeOfInjectionPoint(parameterType);
         return parameterType;
      }

      private final class ConstructorInjection
      {
         Object instantiate()
         {
            parameterTypes = constructor.getGenericParameterTypes();
            int n = parameterTypes.length;
            Object[] arguments = new Object[n];
            boolean varArgs = constructor.isVarArgs();

            if (varArgs) {
               n--;
            }

            for (int i = 0; i < n; i++) {
               MockedType injectable = injectablesForConstructor.get(i);
               Object value = getArgumentValueToInject(injectable);
               arguments[i] = wrapInProviderIfNeeded(parameterTypes[i], value);
            }

            if (varArgs) {
               arguments[n] = obtainInjectedVarargsArray(n);
            }

            return invoke(constructor, arguments);
         }

         private Object obtainInjectedVarargsArray(int varargsParameterIndex)
         {
            Type varargsElementType = getTypeOfInjectionPointFromVarargsParameter(varargsParameterIndex);

            List<Object> varargValues = new ArrayList<Object>();
            MockedType injectable;

            while ((injectable = findNextInjectableForVarargsParameter()) != null) {
               Object value = getValueToInject(injectable);

               if (value != null) {
                  value = wrapInProviderIfNeeded(varargsElementType, value);
                  varargValues.add(value);
               }
            }

            int elementCount = varargValues.size();
            Object varargArray = Array.newInstance(getClassType(varargsElementType), elementCount);

            for (int i = 0; i < elementCount; i++) {
               Array.set(varargArray, i, varargValues.get(i));
            }

            return varargArray;
         }

         private Object getArgumentValueToInject(MockedType injectable)
         {
            Object argument = getValueToInject(injectable);

            if (argument == null) {
               throw new IllegalArgumentException(
                  "No injectable value available" + missingInjectableDescription(injectable.mockId));
            }

            return argument;
         }

         private String missingInjectableDescription(String name)
         {
            String classDesc = mockit.external.asm4.Type.getInternalName(constructor.getDeclaringClass());
            String constructorDesc = "<init>" + mockit.external.asm4.Type.getConstructorDescriptor(constructor);
            String constructorDescription = new MethodFormatter(classDesc, constructorDesc).toString();

            return
               " for parameter \"" + name + "\" in constructor " +
               constructorDescription.replace("java.lang.", "");
         }
      }
   }

   private final class FieldInjection
   {
      private final Object testedObject;
      private final boolean requiresJavaxInject;
      private boolean foundJavaxInject;

      private FieldInjection(Object testedObject, boolean requiresJavaxInject)
      {
         this.testedObject = testedObject;
         this.requiresJavaxInject = requiresJavaxInject;
      }

      List<Field> findAllTargetInstanceFieldsInTestedClassHierarchy()
      {
         List<Field> targetFields = new ArrayList<Field>();
         Class<?> classWithFields = testedObject.getClass();

         do {
            Field[] fields = classWithFields.getDeclaredFields();

            for (Field field : fields) {
               if (isEligibleForInjection(field)) {
                  targetFields.add(field);
               }
            }

            classWithFields = classWithFields.getSuperclass();
         }
         while (isFromSameModuleOrSystemAsSuperClass(classWithFields));

         discardFieldsNotAnnotatedWithJavaxInjectIfAtLeastOneIsAnnotated(targetFields);

         return targetFields;
      }

      private boolean isEligibleForInjection(Field field)
      {
         if (isFinal(field.getModifiers())) return false;
         if (requiresJavaxInject) return isAnnotatedWithJavaxInject(field);
         boolean notStatic = !isStatic(field.getModifiers());
         return INJECT_CLASS == null ? notStatic : isAnnotatedWithJavaxInject(field) || notStatic;
      }

      private boolean isAnnotatedWithJavaxInject(Field field)
      {
         boolean annotated = field.isAnnotationPresent(INJECT_CLASS);
         if (annotated) foundJavaxInject = true;
         return annotated;
      }

      private void discardFieldsNotAnnotatedWithJavaxInjectIfAtLeastOneIsAnnotated(List<Field> targetFields)
      {
         if (!requiresJavaxInject && foundJavaxInject) {
            ListIterator<Field> itr = targetFields.listIterator();

            while (itr.hasNext()) {
               Field targetField = itr.next();

               if (!targetField.isAnnotationPresent(INJECT_CLASS)) {
                  itr.remove();
               }
            }
         }
      }

      private boolean isFromSameModuleOrSystemAsSuperClass(Class<?> superClass)
      {
         if (superClass.getClassLoader() == null) {
            return false;
         }

         Class<?> testedClass = testedObject.getClass();

         if (superClass.getProtectionDomain() == testedClass.getProtectionDomain()) {
            return true;
         }

         String className1 = superClass.getName();
         String className2 = testedClass.getName();
         int p1 = className1.indexOf('.');
         int p2 = className2.indexOf('.');

         if (p1 != p2 || p1 == -1) {
            return false;
         }

         p1 = className1.indexOf('.', p1 + 1);
         p2 = className2.indexOf('.', p2 + 1);

         return p1 == p2 && p1 > 0 && className1.substring(0, p1).equals(className2.substring(0, p2));
      }

      void injectIntoEligibleFields(List<Field> targetFields)
      {
         for (Field field : targetFields) {
            if (notAssignedByConstructor(field)) {
               Object injectableValue = getValueForFieldIfAvailable(targetFields, field);

               if (injectableValue != null) {
                  injectableValue = wrapInProviderIfNeeded(field.getGenericType(), injectableValue);
                  setFieldValue(field, testedObject, injectableValue);
               }
            }
         }
      }

      private boolean notAssignedByConstructor(Field field)
      {
         if (INJECT_CLASS != null && field.isAnnotationPresent(INJECT_CLASS)) {
            return true;
         }

         Object fieldValue = getFieldValue(field, testedObject);

         if (fieldValue == null) {
            return true;
         }

         Class<?> fieldType = field.getType();

         if (!fieldType.isPrimitive()) {
            return false;
         }

         Object defaultValue = DefaultValues.defaultValueForPrimitiveType(fieldType);

         return fieldValue.equals(defaultValue);
      }

      private Object getValueForFieldIfAvailable(List<Field> targetFields, Field fieldToBeInjected)
      {
         setTypeOfInjectionPoint(fieldToBeInjected.getGenericType());

         String targetFieldName = fieldToBeInjected.getName();
         MockedType mockedType;

         if (withMultipleTargetFieldsOfSameType(targetFields, fieldToBeInjected)) {
            mockedType = findInjectableByTypeAndName(targetFieldName);
         }
         else {
            mockedType = findInjectableByTypeAndOptionallyName(targetFieldName);
         }

         return mockedType == null ? null : getValueToInject(mockedType);
      }

      private boolean withMultipleTargetFieldsOfSameType(List<Field> targetFields, Field fieldToBeInjected)
      {
         for (Field targetField : targetFields) {
            if (targetField != fieldToBeInjected && isSameTypeAsInjectionPoint(targetField.getGenericType())) {
               return true;
            }
         }

         return false;
      }

      private MockedType findInjectableByTypeAndName(String targetFieldName)
      {
         for (MockedType injectable : injectables) {
            if (hasSameTypeAsInjectionPoint(injectable) && targetFieldName.equals(injectable.mockId)) {
               return injectable;
            }
         }

         return null;
      }

      private MockedType findInjectableByTypeAndOptionallyName(String targetFieldName)
      {
         MockedType found = null;

         for (MockedType injectable : injectables) {
            if (hasSameTypeAsInjectionPoint(injectable)) {
               if (targetFieldName.equals(injectable.mockId)) {
                  return injectable;
               }

               if (found == null) {
                  found = injectable;
               }
            }
         }

         return found;
      }
   }
}
