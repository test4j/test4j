package org.jtester.hamcrest.matcher.property.report;

import static ext.jtester.apache.commons.lang.ClassUtils.getShortClassName;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static org.jtester.hamcrest.matcher.property.reflection.HibernateUtil.getUnproxiedValue;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * A class for generating a string representation of any object, array or
 * primitive value.
 * <p/>
 * Non-primitive objects are processed recursively so that a string
 * representation of inner objects is also generated. Too avoid too much output,
 * this recursion is limited with a given maximum depth.
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class ObjectFormatter {

	/**
	 * The maximum recursion depth
	 */
	protected int maxDepth;

	/**
	 * Creates a formatter with a maximum recursion depth of 5.
	 */
	public ObjectFormatter() {
		this(5);
	}

	/**
	 * Creates a formatter with the given maximum recursion depth.
	 * <p/>
	 * NOTE: there is no cycle detection. A large max depth value can cause lots
	 * of output in case of a cycle.
	 * 
	 * @param maxDepth
	 *            The max depth > 0
	 */
	public ObjectFormatter(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Gets the string representation of the given object.
	 * 
	 * @param object
	 *            The instance
	 * @return The string representation, not null
	 */
	public String format(Object object) {
		StringBuilder result = new StringBuilder();
		formatImpl(object, 0, result);
		return result.toString();
	}

	/**
	 * Actual implementation of the formatting.
	 * 
	 * @param object
	 *            The instance
	 * @param currentDepth
	 *            The current recursion depth
	 * @param result
	 *            The builder to append the result to, not null
	 */
	protected void formatImpl(Object object, int currentDepth, StringBuilder result) {
		// get the actual value if the value is wrapped by a Hibernate proxy
		object = getUnproxiedValue(object);

		if (object == null) {
			result.append(String.valueOf(object));
			return;
		}
		if (object instanceof String) {
			result.append('"');
			result.append(object);
			result.append('"');
			return;
		}
		if (object instanceof Number || object instanceof Date) {
			result.append(String.valueOf(object));
			return;
		}
		if (object instanceof Character) {
			result.append('\'');
			result.append(String.valueOf(object));
			result.append('\'');
			return;
		}

		Class type = object.getClass();
		if (type.isPrimitive() || type.isEnum()) {
			result.append(String.valueOf(object));
			return;
		}

		if (formatProxy(object, result)) {
			return;
		}
		if (type.getName().startsWith("java.lang")) {
			result.append(String.valueOf(object));
			return;
		}
		if (type.isArray()) {
			formatArray(object, currentDepth, result);
			return;
		}
		if (object instanceof Collection) {
			formatCollection((Collection<?>) object, currentDepth, result);
			return;
		}
		if (object instanceof Map) {
			formatMap((Map<?, ?>) object, currentDepth, result);
			return;
		}
		if (currentDepth >= maxDepth) {
			result.append(getShortClassName(type));
			result.append("<...>");
			return;
		}
		formatObject(object, currentDepth, result);
	}

	/**
	 * Formats the given array.
	 * 
	 * @param array
	 *            The array, not null
	 * @param currentDepth
	 *            The current recursion depth
	 * @param result
	 *            The builder to append the result to, not null
	 */
	protected void formatArray(Object array, int currentDepth, StringBuilder result) {
		if (array instanceof byte[]) {
			result.append(Arrays.toString((byte[]) array));
			return;
		}
		if (array instanceof short[]) {
			result.append(Arrays.toString((short[]) array));
			return;
		}
		if (array instanceof int[]) {
			result.append(Arrays.toString((int[]) array));
			return;
		}
		if (array instanceof long[]) {
			result.append(Arrays.toString((long[]) array));
			return;
		}
		if (array instanceof char[]) {
			result.append(Arrays.toString((char[]) array));
			return;
		}
		if (array instanceof float[]) {
			result.append(Arrays.toString((float[]) array));
			return;
		}
		if (array instanceof double[]) {
			result.append(Arrays.toString((double[]) array));
			return;
		}
		if (array instanceof boolean[]) {
			result.append(Arrays.toString((boolean[]) array));
			return;
		}

		// format an object array
		result.append("[");
		boolean notFirst = false;
		for (Object element : (Object[]) array) {
			if (notFirst) {
				result.append(", ");
			} else {
				notFirst = true;
			}
			formatImpl(element, currentDepth + 1, result);
		}
		result.append("]");
	}

	/**
	 * Formats the given collection.
	 * 
	 * @param collection
	 *            The collection, not null
	 * @param currentDepth
	 *            The current recursion depth
	 * @param result
	 *            The builder to append the result to, not null
	 */
	protected void formatCollection(Collection<?> collection, int currentDepth, StringBuilder result) {
		result.append("[");
		boolean notFirst = false;
		for (Object element : collection) {
			if (notFirst) {
				result.append(", ");
			} else {
				notFirst = true;
			}
			formatImpl(element, currentDepth + 1, result);
		}
		result.append("]");
	}

	/**
	 * Formats the given map.
	 * 
	 * @param map
	 *            The map, not null
	 * @param currentDepth
	 *            The current recursion depth
	 * @param result
	 *            The builder to append the result to, not null
	 */
	protected void formatMap(Map<?, ?> map, int currentDepth, StringBuilder result) {
		result.append("{");
		boolean notFirst = false;
		for (Map.Entry<?, ?> element : map.entrySet()) {
			if (notFirst) {
				result.append(", ");
			} else {
				notFirst = true;
			}
			formatImpl(element.getKey(), currentDepth, result);
			result.append("=");
			formatImpl(element.getValue(), currentDepth + 1, result);
		}
		result.append("}");
	}

	/**
	 * Formats the given object by formatting the inner fields.
	 * 
	 * @param object
	 *            The object, not null
	 * @param currentDepth
	 *            The current recursion depth
	 * @param result
	 *            The builder to append the result to, not null
	 */
	protected void formatObject(Object object, int currentDepth, StringBuilder result) {
		Class type = object.getClass();
		result.append(getShortClassName(type));
		result.append("<");
		formatFields(object, type, currentDepth, result);
		result.append(">");
	}

	/**
	 * Formats the field values of the given object.
	 * 
	 * @param object
	 *            The object, not null
	 * @param clazz
	 *            The class for which to format the fields, not null
	 * @param currentDepth
	 *            The current recursion depth
	 * @param result
	 *            The builder to append the result to, not null
	 */
	protected void formatFields(Object object, Class clazz, int currentDepth, StringBuilder result) {
		Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);

		for (int i = 0; i < fields.length; i++) {
			// skip transient and static fields
			Field field = fields[i];
			if (isTransient(field.getModifiers()) || isStatic(field.getModifiers()) || field.isSynthetic()) {
				continue;
			}
			try {
				if (i > 0) {
					result.append(", ");
				}
				result.append(field.getName());
				result.append("=");
				formatImpl(field.get(object), currentDepth + 1, result);

			} catch (IllegalAccessException e) {
				// this can't happen. Would get a Security exception instead
				// throw a runtime exception in case the impossible happens.
				throw new InternalError("Unexpected IllegalAccessException");
			}
		}

		// format fields declared in superclass
		Class superclazz = clazz.getSuperclass();
		while (superclazz != null && !superclazz.getName().startsWith("java.lang")) {
			formatFields(object, superclazz, currentDepth, result);
			superclazz = superclazz.getSuperclass();
		}
	}

	protected boolean formatProxy(Object object, StringBuilder result) {
		String className = getShortClassName(object.getClass());
		int index = className.indexOf("..EnhancerByCGLIB..");
		if (index > 0) {
			result.append("Proxy<");
			result.append(className.substring(0, index));
			result.append(">");
			return true;
		}
		return false;
	}
}
