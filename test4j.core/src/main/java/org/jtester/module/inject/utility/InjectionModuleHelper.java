package org.jtester.module.inject.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

import org.jtester.module.JTesterException;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.reflector.FieldAccessor;

/**
 * Class containing static methods that implement explicit injection using OGNL
 * expressions, and auto-injection by type.
 */
@SuppressWarnings("rawtypes")
public class InjectionModuleHelper {

	/**
	 * Explicit injection of the objectToInject into the specified property of
	 * the target. The property should be a correct OGNL expression.
	 * 
	 * @param objectToInject
	 *            The object that is injected
	 * @param target
	 *            The target object
	 * @param property
	 *            The OGNL expression that defines where the object will be
	 *            injected, not null
	 * @return The object that was replaced by the injection
	 */
	public static Object injectInto(Object objectToInject, Object target, String property) {
		if (target == null) {
			throw new JTesterException("Target for injection should not be null");
		}
		try {
			FieldAccessor fieldAccessor = new FieldAccessor(target.getClass(), property);
			Object oldValue = fieldAccessor.get(target);
			fieldAccessor.set(target, objectToInject);

			return oldValue;
		} catch (Exception e) {
			throw new JTesterException("Failed to set value using OGNL expression " + property, e);
		}
	}

	/**
	 * Performs auto-injection by type of the objectToInject on the target
	 * object.
	 * 
	 * @param objectToInject
	 *            The object that is injected
	 * @param objectToInjectType
	 *            The type of the object. This should be the type of the object
	 *            or one of his super-types or implemented interfaces. This type
	 *            is used for property type matching on the target object
	 * @param target
	 *            The object into which the objectToInject is injected
	 * @param propertyAccess
	 *            Defines if field or setter injection is used
	 * @return The object that was replaced by the injection
	 */
	public static Object injectIntoByType(Object objectToInject, Type objectToInjectType, Object target) {
		if (target == null) {
			throw new JTesterException("Target for injection should not be null");
		}
		return injectIntoFieldByType(objectToInject, objectToInjectType, target, target.getClass());
	}

	public static void injectIntoAnnotated(Object objectToInject, Object target, Class<? extends Annotation> annotation) {
		injectIntoAnnotatedFields(objectToInject, target, annotation);
		injectIntoAnnotatedMethods(objectToInject, target, annotation);
	}

	public static void injectIntoAnnotatedMethods(Object objectToInject, Object target,
			Class<? extends Annotation> annotation) {
		Set<Method> annotatedMethods = AnnotationHelper.getMethodsAnnotatedWith(target.getClass(), annotation);
		for (Method annotatedMethod : annotatedMethods) {
			try {
				annotatedMethod.invoke(target, objectToInject);
			} catch (IllegalArgumentException e) {
				throw new JTesterException(
						"Method "
								+ annotatedMethod.getName()
								+ " annotated with "
								+ annotation.getName()
								+ " must have exactly one argument with a type equal to or a superclass / implemented interface of "
								+ objectToInject.getClass().getSimpleName());
			} catch (IllegalAccessException e) {
				throw new JTesterException("Unable to inject value into following method annotated with "
						+ annotation.getName() + ": " + annotatedMethod.getName(), e);
			} catch (InvocationTargetException e) {
				throw new JTesterException("Unable to inject value into following method annotated with "
						+ annotation.getName() + ": " + annotatedMethod.getName(), e);
			}
		}
	}

	public static void injectIntoAnnotatedFields(Object objectToInject, Object target,
			Class<? extends Annotation> annotation) {
		Set<Field> annotatedFields = AnnotationHelper.getFieldsAnnotatedWith(target.getClass(), annotation);
		for (Field annotatedField : annotatedFields) {
			FieldHelper.setFieldValue(target, annotatedField, objectToInject);
		}
	}

	/**
	 * Performs auto-injection on a field by type of the objectToInject into the
	 * given target object or targetClass, depending on the value of isStatic.
	 * The object is injected on one single field, if there is more than one
	 * candidate field, a {@link JTesterException} is thrown. We try to inject
	 * the object on the most specific field, this means that when there are
	 * muliple fields of one of the super-types or implemented interfaces of the
	 * field, the one that is lowest in the hierarchy is chosen (if possible,
	 * otherwise, a {@link JTesterException} is thrown.
	 * 
	 * @param objectToInject
	 *            The object that is injected
	 * @param objectToInjectType
	 *            The type of the object that is injected
	 * @param target
	 *            The target object (only used when isStatic is false)
	 * @param targetClass
	 *            The target class (only used when isStatis is true)
	 * @param isStatic
	 *            Indicates wether injection should be performed on the target
	 *            object or on the target class
	 * @return The object that was replaced by the injection
	 */
	private static Object injectIntoFieldByType(Object objectToInject, Type objectToInjectType, Object target,
			Class targetClass) {
		// Try to find a field with an exact matching type
		Field fieldToInjectTo = null;
		Set<Field> fieldsWithExactType = FieldHelper.getFieldsOfType(targetClass, objectToInjectType);
		if (fieldsWithExactType.size() > 1) {
			StringBuilder message = new StringBuilder("More than one field with type " + objectToInjectType
					+ " found in " + targetClass.getSimpleName() + ".");
			if (objectToInjectType instanceof Class) {
				message.append(" If the target is a generic type, this can be caused by type erasure.");
			}
			message.append(" Specify the target field explicitly instead of injecting into by type.");
			throw new JTesterException(message.toString());

		} else if (fieldsWithExactType.size() == 1) {
			fieldToInjectTo = fieldsWithExactType.iterator().next();

		} else {
			// Try to find a supertype field:
			// If one field exist that has a type which is more specific than
			// all other fields of the given type,
			// this one is taken. Otherwise, an exception is thrown
			Set<Field> fieldsOfType = FieldHelper.getFieldsAssignableFrom(targetClass, objectToInjectType);
			if (fieldsOfType.size() == 0) {
				throw new JTesterException("No field with (super)type " + objectToInjectType + " found in "
						+ targetClass.getSimpleName());
			}
			for (Field field : fieldsOfType) {
				boolean moreSpecific = true;
				for (Field compareToField : fieldsOfType) {
					if (field != compareToField) {
						if (field.getClass().isAssignableFrom(compareToField.getClass())) {
							moreSpecific = false;
							break;
						}
					}
				}
				if (moreSpecific) {
					fieldToInjectTo = field;
					break;
				}
			}
			if (fieldToInjectTo == null) {
				throw new JTesterException("Multiple candidate target fields found in " + targetClass.getSimpleName()
						+ ", with none of them more specific than all others.");
			}
		}

		// Field to inject into found, inject the object and return old value
		Object oldValue = null;
		try {
			oldValue = FieldHelper.getFieldValue(target, fieldToInjectTo);

		} catch (Throwable e) {
			MessageHelper
					.warn("Unable to retrieve current value of field to inject into. Will not be able to restore value after injection.",
							e);
		}
		FieldHelper.setFieldValue(target, fieldToInjectTo, objectToInject);
		return oldValue;
	}
}
