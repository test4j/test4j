package org.jtester.tools.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 将原生类型的数组转换成对象数组
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class ArrayHelper {
	/**
	 * An empty immutable <code>Class</code> array.
	 */
	public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

	// boolean
	// byte
	// char
	// short int long
	// float double
	private static Object[] toPrimitiveArray(char values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Character value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(float values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Float value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(long values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Long value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(short values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Short value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(int values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Integer value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(double values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Double value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(boolean values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Boolean value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	private static Object[] toPrimitiveArray(byte values[]) {
		List<Object> objs = new ArrayList<Object>();
		for (Byte value : values) {
			objs.add(value);
		}
		return objs.toArray();
	}

	/**
	 * 将primitive对象数组转换为object类型数组
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] convertPrimitiveArrayToObjectArray(Object value) {
		if (value instanceof int[]) {
			return toPrimitiveArray((int[]) value);
		} else if (value instanceof long[]) {
			return toPrimitiveArray((long[]) value);
		} else if (value instanceof short[]) {
			return toPrimitiveArray((short[]) value);
		} else if (value instanceof float[]) {
			return toPrimitiveArray((float[]) value);
		} else if (value instanceof double[]) {
			return toPrimitiveArray((double[]) value);
		} else if (value instanceof char[]) {
			return toPrimitiveArray((char[]) value);
		} else if (value instanceof byte[]) {
			return toPrimitiveArray((byte[]) value);
		} else if (value instanceof boolean[]) {
			return toPrimitiveArray((boolean[]) value);
		} else if (value instanceof Object[]) {
			return (Object[]) value;
		} else {
			throw new RuntimeException("object isn't an array.");
		}
	}

	/**
	 * 构造数组对象
	 * 
	 * @param objects
	 * @return
	 */
	public static Object[] toArray(Object... objects) {
		if (objects == null || objects.length == 0) {
			return new Object[0];
		}
		if (objects.length == 1) {
			Object o = objects[0];
			if (o == null) {
				return new Object[] { null };
			} else if (o.getClass().isArray()) {
				return convertPrimitiveArrayToObjectArray(o);
			} else if (o instanceof Collection) {
				Collection coll = (Collection) o;
				Object[] array = new Object[coll.size()];
				int index = 0;
				for (Object item : coll) {
					array[index++] = item;
				}
				return array;
			} else {
				return new Object[] { o };
			}
		} else {
			Object[] array = new Object[objects.length];
			int index = 0;
			for (Object o : objects) {
				array[index] = o;
				index++;
			}
			return array;
		}
	}

	public static Object[] toArray(Object o1, Object[] o2) {
		if (o2 == null || o2.length == 0) {
			return new Object[] { o1 };
		}
		Object[] arr = new Object[o2.length + 1];
		arr[0] = o1;
		for (int index = 0; index < o2.length; index++) {
			arr[index + 1] = o2[index];
		}
		return arr;
	}

	/**
	 * 判断对象是数组类型
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isArray(Object o) {
		if (o instanceof char[]) {// char
			return true;
		} else if (o instanceof boolean[]) {// boolean
			return true;
		} else if (o instanceof byte[]) {// byte
			return true;
		} else if (o instanceof short[]) {// short
			return true;
		} else if (o instanceof int[]) {// int
			return true;
		} else if (o instanceof long[]) {// long
			return true;
		} else if (o instanceof float[]) {// float
			return true;
		} else if (o instanceof double[]) {// double
			return true;
		} else {
			return o instanceof Object[];
		}
	}

	/**
	 * 判断对象是数组类型或者集合类型
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isCollOrArray(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Collection) {
			return true;
		}
		if (o.getClass().isArray()) {
			return true;
		}
		return false;
	}

	public static int sizeOf(Object o) {
		if (o == null) {
			return 0;
		}
		int size = 0;
		if (o instanceof Collection) {
			size = ((Collection) o).size();
		} else if (o instanceof Map) {
			size = ((Map) o).size();
		} else if (o instanceof char[]) {// char
			size = ((char[]) o).length;
		} else if (o instanceof boolean[]) {// boolean
			size = ((boolean[]) o).length;
		} else if (o instanceof byte[]) {// byte
			size = ((byte[]) o).length;
		} else if (o instanceof short[]) {// short
			size = ((short[]) o).length;
		} else if (o instanceof int[]) {// int
			size = ((int[]) o).length;
		} else if (o instanceof long[]) {// long
			size = ((long[]) o).length;
		} else if (o instanceof float[]) {// float
			size = ((float[]) o).length;
		} else if (o instanceof double[]) {// double
			size = ((double[]) o).length;
		} else if (o instanceof Object[]) {
			size = ((Object[]) o).length;
		} else {
			return 1;
		}

		return size;
	}

	public static String toString(long[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(int[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(short[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(char[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(byte[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(boolean[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(float[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(double[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(Object[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < a.length; i++) {
			if (i == 0)
				buf.append('[');
			else
				buf.append(", ");

			buf.append(String.valueOf(a[i]));
		}

		buf.append("]");
		return buf.toString();
	}

	/**
	 * <p>
	 * Outputs an array as a String, treating <code>null</code> as an empty
	 * array.
	 * 
	 * @param array
	 *            the array to get a toString for, may be <code>null</code>
	 * @return a String representation of the array, '{}' if null array input
	 */
	public static String toString(Object array) {
		if (array == null) {
			return "[]";
		}
		if (array instanceof long[]) {
			return toString((long[]) array);
		}
		if (array instanceof int[]) {
			return toString((int[]) array);
		}
		if (array instanceof short[]) {
			return toString((short[]) array);
		}
		if (array instanceof char[]) {
			return toString((char[]) array);
		}
		if (array instanceof byte[]) {
			return toString((byte[]) array);
		}
		if (array instanceof boolean[]) {
			return toString((boolean[]) array);
		}
		if (array instanceof float[]) {
			return toString((float[]) array);
		}
		if (array instanceof double[]) {
			return toString((double[]) array);
		}
		if (array instanceof Object[]) {
			return toString((Object[]) array);
		}
		return array.toString();
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * 
	 * <p>
	 * Any multi-dimensional aspects of the arrays are ignored.
	 * </p>
	 * 
	 * @param array1
	 *            the first array, may be <code>null</code>
	 * @param array2
	 *            the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(Object[] array1, Object[] array2) {
		if (array1 == null) {
			return array2 == null;
		}
		if (array2 == null) {
			return array1 == null;
		}
		return array1.length == array2.length;
	}
}
