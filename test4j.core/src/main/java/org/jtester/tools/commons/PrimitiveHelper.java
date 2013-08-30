package org.jtester.tools.commons;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class PrimitiveHelper {
	private static Map<Class, Object> map = new HashMap<Class, Object>();
	static {
		map.put(String.class, "");
		map.put(Integer.class, 0);
		map.put(Short.class, (short) 0);
		map.put(Long.class, (long) 0);
		map.put(Byte.class, (byte) 0);
		map.put(Float.class, 0.0f);
		map.put(Double.class, 0.0d);
		map.put(Character.class, '\0');
		map.put(Boolean.class, false);

		map.put(int.class, 0);
		map.put(short.class, 0);
		map.put(long.class, 0);
		map.put(byte.class, 0);
		map.put(float.class, 0.0f);
		map.put(double.class, 0.0d);
		map.put(char.class, '\0');
		map.put(boolean.class, false);
	}

	public static Class getPrimitiveBoxType(Class type) {
		if (int.class.equals(type)) {
			return Integer.class;
		} else if (long.class.equals(type)) {
			return Long.class;
		} else if (short.class.equals(type)) {
			return Short.class;
		} else if (double.class.equals(type)) {
			return Double.class;
		} else if (float.class.equals(type)) {
			return Float.class;
		} else if (char.class.equals(type)) {
			return Character.class;
		} else if (boolean.class.equals(type)) {
			return Boolean.class;
		} else if (byte.class.equals(type)) {
			return Byte.class;
		} else {
			return type;
		}
	}

	/**
	 * 返回有对应primitive类型的默认值
	 * 
	 * @param claz
	 * @return
	 */
	public static Object getPrimitiveDefaultValue(Class claz) {
		if (map.containsKey(claz)) {
			return map.get(claz);
		} else {
			return null;
		}
	}

	/**
	 * 判断2个类型的primitive类型是否一致
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	public static boolean isPrimitiveTypeEquals(final Class expected, final Class actual) {
		Class _expected = expected;
		if (couples.containsKey(expected)) {
			_expected = couples.get(expected);
		}
		Class _actual = actual;
		if (couples.containsKey(actual)) {
			_actual = couples.get(actual);
		}
		return _expected == _actual;
	}

	private static Map<Class, Class> couples = new HashMap<Class, Class>();
	static {
		couples.put(Integer.class, int.class);
		couples.put(Short.class, short.class);
		couples.put(Long.class, long.class);
		couples.put(Byte.class, byte.class);
		couples.put(Float.class, float.class);
		couples.put(Double.class, double.class);
		couples.put(Character.class, char.class);
		couples.put(Boolean.class, boolean.class);
	}

	/**
	 * 判断2个数字是否相等
	 * 
	 * @param num_1
	 * @param num_2
	 * @return
	 */
	public static boolean doesEqual(Number num_1, Number num_2) {
		if (num_1 == null) {
			return num_2 == null;
		} else if (num_2 == null) {
			return false;
		}
		if (canLongNumber(num_1) && canLongNumber(num_2)) {
			long l1 = num_1.longValue();
			long l2 = num_2.longValue();
			return l1 == l2;
		}
		if (canDoubleNumber(num_1) && canDoubleNumber(num_2)) {
			double d1 = num_1.doubleValue();
			double d2 = num_2.doubleValue();
			return d1 == d2;
		}
		return num_1.equals(num_2);
	}

	/**
	 * 是否可以转为Long类型<br>
	 * Integer Long Short
	 * 
	 * @param number
	 * @return
	 */
	private static boolean canLongNumber(Number number) {
		if (number instanceof Long || number instanceof Integer || number instanceof Short
				|| number instanceof BigInteger) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean canDoubleNumber(Number number) {
		if (number instanceof Double || number instanceof Float || number instanceof BigDecimal) {
			return true;
		} else {
			return false;
		}
	}

	private static Set<Class<?>> primitiveClassSet = new HashSet<Class<?>>();
	static {
		primitiveClassSet.add(boolean.class);
		primitiveClassSet.add(byte.class);
		primitiveClassSet.add(short.class);
		primitiveClassSet.add(int.class);
		primitiveClassSet.add(long.class);
		primitiveClassSet.add(float.class);
		primitiveClassSet.add(double.class);
		primitiveClassSet.add(char.class);
	}

	/**
	 * 是否primitive类型
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isPrimitiveType(Class type) {
		return primitiveClassSet.contains(type);
	}

	private static Set<Class<?>> primitiveRelativeTypes = new HashSet<Class<?>>();
	static {
		primitiveRelativeTypes.add(Boolean.class);
		primitiveRelativeTypes.add(Byte.class);
		primitiveRelativeTypes.add(Short.class);
		primitiveRelativeTypes.add(Integer.class);
		primitiveRelativeTypes.add(Long.class);
		primitiveRelativeTypes.add(Float.class);
		primitiveRelativeTypes.add(Double.class);
		primitiveRelativeTypes.add(Character.class);
		// primitiveRelativeTypes.add(String.class);
	}

	/**
	 * 是否是primitive类型或对应的Object对象(包括String类型)
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isPrimitiveTypeOrRelative(Class type) {
		if (isPrimitiveType(type)) {
			return true;
		}
		return primitiveRelativeTypes.contains(type);
	}
}
