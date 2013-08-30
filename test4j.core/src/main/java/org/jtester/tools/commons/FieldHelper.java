package org.jtester.tools.commons;

import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.jtester.tools.reflector.FieldAccessor;

@SuppressWarnings({ "rawtypes" })
public final class FieldHelper {
	/**
	 * jTester method to search for a field starting from the specified class
	 * and going up the hierarchy until the field is found or the {@link Object}
	 * class is reached.
	 * 
	 * @param cls
	 *            the class from which to start
	 * @param name
	 *            the field name
	 * @return the {@link Field} found
	 * @throws NoSuchFieldException
	 *             if the field cannot be found within the specified class
	 *             hierarchy.
	 */
	public static Field getField(Class cls, String name) {
		while (cls != Object.class) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				cls = cls.getSuperclass();
			}
		}
		throw new NoSuchFieldRuntimeException("No such field: " + name);
	}

	/**
	 * 设置实例target的字段值
	 * 
	 * @param target
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void setFieldValue(Object target, String fieldName, Object fieldValue) {
		if (target == null) {
			throw new RuntimeException("the target object can't be null.");
		}
		FieldAccessor accessor = new FieldAccessor(target.getClass(), fieldName);
		accessor.set(target, fieldValue);
	}

	public static void setFieldValue(Object target, Field field, Object fieldValue) {
		if (target == null) {
			throw new RuntimeException("the target object can't be null.");
		}
		if (field == null) {
			throw new RuntimeException("the field can't be null.");
		}

		boolean isAccessible = field.isAccessible();
		try {
			field.setAccessible(true);
			field.set(target, fieldValue);
		} catch (Exception e) {
			String info = String.format("to set field[%s] value into target[%s] error.", field.getName(), target
					.getClass().getName());
			throw new RuntimeException(info, e);
		} finally {
			field.setAccessible(isAccessible);
		}
	}

	/**
	 * Returns the value of the given field (may be private) in the given object<br>
	 * 获得对象obj的属性名为fieldname的字段值
	 * 
	 * @param target
	 *            The object containing the field, null for static fields
	 * @param fieldName
	 *            The field, not null
	 * @return The value of the given field in the given object
	 */
	public static Object getFieldValue(Object target, String fieldName) {
		if (target == null) {
			throw new RuntimeException("the target object can't be null.");
		}
		FieldAccessor accessor = new FieldAccessor(target.getClass(), fieldName);
		Object o = accessor.get(target);
		return o;
	}

	public static Object getStaticFieldValue(Class claz, String fieldName) {
		if (claz == null) {
			throw new RuntimeException("the target class can't be null.");
		}
		FieldAccessor accessor = new FieldAccessor(claz, fieldName);
		Object o = accessor.getStatic();
		return o;
	}

	public static Object getFieldValue(Object target, Field field) {
		if (target == null) {
			throw new RuntimeException("the target object can't be null.");
		}
		if (field == null) {
			throw new RuntimeException("the field can't be null.");
		}

		boolean isAccessible = field.isAccessible();
		try {
			field.setAccessible(true);
			Object o = field.get(target);
			return o;
		} catch (Exception e) {
			String info = String.format("to get field[%s] value from target[%s] error.", field.getName(), target
					.getClass().getName());
			throw new RuntimeException(info, e);
		} finally {
			field.setAccessible(isAccessible);
		}
	}

	/**
	 * 返回类所有的字段（包括父类的）<br>
	 * Gets all fields of the given class and all its super-classes.
	 * 
	 * @param clazz
	 *            The class
	 * @return The fields, not null
	 */
	public static List<Field> getAllFields(final Class clazz) {
		List<Field> result = new ArrayList<Field>();
		if (clazz == null || clazz.equals(Object.class)) {
			return result;
		}

		Class type = clazz;
		while (type != Object.class && type != null) {
			// add all fields of this class
			Field[] declaredFields = type.getDeclaredFields();
			result.addAll(asList(declaredFields));

			type = type.getSuperclass();
		}
		return result;
	}

	/**
	 * Returns all declared fields of the given class that are assignable from
	 * the given type.
	 * 
	 * @param clazz
	 *            The class to get fields from, not null
	 * @param type
	 *            The type, not null
	 * @param isStatic
	 *            True if static fields are to be returned, false for non-static
	 * @return A list of Fields, empty list if none found
	 */
	public static Set<Field> getFieldsAssignableFrom(Class clazz, Type type) {
		Set<Field> fieldsOfType = new HashSet<Field>();
		List<Field> allFields = getAllFields(clazz);
		for (Field field : allFields) {
			boolean isAssignFrom = ClazzHelper.isAssignable(type, field.getGenericType());
			if (isAssignFrom) {
				fieldsOfType.add(field);
			}
		}
		return fieldsOfType;
	}

	/**
	 * Returns the fields in the given class that have the exact given type. The
	 * class's superclasses are also investigated.
	 * 
	 * @param clazz
	 *            The class to get the field from, not null
	 * @param type
	 *            The type, not null
	 * @param isStatic
	 *            True if static fields are to be returned, false for non-static
	 * @return The fields with the given type
	 */
	public static Set<Field> getFieldsOfType(Class clazz, Type type) {
		Set<Field> fields = new HashSet<Field>();
		List<Field> allFields = getAllFields(clazz);
		for (Field field : allFields) {
			boolean isTypeEquals = field.getType().equals(type);
			if (isTypeEquals) {
				fields.add(field);
			}
		}
		return fields;
	}
}
