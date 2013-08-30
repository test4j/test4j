package org.jtester.json.helper;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.regex.Pattern;

import org.jtester.tools.commons.ClazzHelper;

@SuppressWarnings("rawtypes")
public final class ClazzMap {
	static Map<Class, String> alias = new HashMap<Class, String>() {
		private static final long serialVersionUID = 1L;
		{
			this.put(Boolean.class, "Boolean");
			this.put(boolean.class, "boolean");
			this.put(Boolean[].class, "Boolean[]");
			this.put(boolean[].class, "boolean[]");

			this.put(Byte.class, "Byte");
			this.put(byte.class, "byte");
			this.put(Byte[].class, "Byte[]");
			this.put(byte[].class, "byte[]");

			this.put(Character.class, "Character");
			this.put(char.class, "char");
			this.put(Character[].class, "Character[]");
			this.put(char[].class, "char[]");

			this.put(Double.class, "Double");
			this.put(double.class, "double");
			this.put(Double[].class, "Double[]");
			this.put(double[].class, "double[]");

			this.put(Float.class, "Float");
			this.put(float.class, "float");
			this.put(Float[].class, "Float[]");
			this.put(float[].class, "float[]");

			this.put(Integer.class, "Integer");
			this.put(int.class, "int");
			this.put(Integer[].class, "Integer[]");
			this.put(int[].class, "int[]");

			this.put(Long.class, "Long");
			this.put(long.class, "long");
			this.put(Long[].class, "Long[]");
			this.put(long[].class, "long[]");

			this.put(Short.class, "Short");
			this.put(short.class, "short");
			this.put(Short[].class, "Short[]");
			this.put(short[].class, "short[]");

			this.put(String.class, "string");
			this.put(String[].class, "string[]");

			this.put(HashMap.class, "map");
			this.put(ArrayList.class, "list");
			this.put(HashSet.class, "set");

			this.put(BigInteger.class, "BigInteger");
			this.put(BigDecimal.class, "BigDecimal");
			this.put(Charset.class, "Charset");
			this.put(Class.class, "class");
			this.put(Locale.class, "Locale");
			this.put(Pattern.class, "Pattern");
			this.put(TimeZone.class, "TimeZone");
			this.put(URI.class, "URI");
			this.put(URL.class, "URL");
			this.put(UUID.class, "UUID");
			this.put(File.class, "File");
			this.put(Date.class, "Date");
			this.put(SimpleDateFormat.class, "SimpleDateFormat");

			this.put(AtomicBoolean.class, "atomic.bool");
			this.put(AtomicInteger.class, "atomic.int");
			this.put(AtomicIntegerArray.class, "atomic.int[]");
			this.put(AtomicLong.class, "atomic.long");
			this.put(AtomicLongArray.class, "atomic.long[]");
			this.put(AtomicReference.class, "atomic.reference");
			this.put(AtomicReferenceArray.class, "atomic.reference[]");
		}
	};

	static List<Class> simples = new ArrayList<Class>() {
		private static final long serialVersionUID = 1L;
		{
			this.add(Boolean.class);
			this.add(boolean.class);

			this.add(Byte.class);
			this.add(byte.class);
			this.add(Character.class);
			this.add(char.class);
			this.add(Double.class);
			this.add(double.class);
			this.add(Float.class);
			this.add(float.class);
			this.add(Integer.class);
			this.add(int.class);
			this.add(Long.class);
			this.add(long.class);
			this.add(Short.class);
			this.add(short.class);

			this.add(String.class);

			this.add(BigInteger.class);
			this.add(BigDecimal.class);
			this.add(Charset.class);
			this.add(Class.class);
			this.add(Locale.class);
			this.add(Pattern.class);
			this.add(TimeZone.class);
			this.add(URI.class);
			this.add(URL.class);
			this.add(UUID.class);
			this.add(File.class);
			this.add(Date.class);
			this.add(SimpleDateFormat.class);

			this.add(AtomicBoolean.class);
			this.add(AtomicInteger.class);
			this.add(AtomicIntegerArray.class);
			this.add(AtomicLong.class);
			this.add(AtomicLongArray.class);
			this.add(AtomicReference.class);
			this.add(AtomicReferenceArray.class);
		}
	};

	/**
	 * 返回class的名称，如果有简称返回简称<br>
	 * 否则返回 ClassName@ObjectReference, eg: org.jtester.User@a4488
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClazzName(Object target) {
		Class clazz = ClazzHelper.getUnProxyType(target.getClass());
		String typename = clazz.getName();
		for (Class type : alias.keySet()) {
			if (type == clazz) {
				typename = alias.get(type);
				break;
			}
		}

		if (simples.contains(clazz)) {
			return typename;
		} else {
			return typename + getReferenceAddress(target);
		}
	}

	/**
	 * 已经加载的class
	 */
	private static final Map<String, Class> existedClazz = new HashMap<String, Class>();

	/**
	 * 返回class类型
	 * 
	 * @param clazzname
	 * @return
	 */
	public static Class getClazzType(String clazzname) {
		for (Map.Entry<Class, String> entry : alias.entrySet()) {
			String typename = entry.getValue();
			if (typename.equals(clazzname)) {
				return entry.getKey();
			}
		}
		if (existedClazz.containsKey(clazzname)) {
			return existedClazz.get(clazzname);
		} else {
			Class clazz = ClazzHelper.getClazz(clazzname);
			existedClazz.put(clazzname, clazz);
			return clazz;
		}
	}

	/**
	 * 返回对象的引用地址hashcode
	 * 
	 * @param o
	 * @return
	 */
	public static final String getReferenceAddress(Object o) {
		assert o != null : "the object value can't be null.";
		Class type = ClazzHelper.getUnProxyType(o.getClass());
		if (simples.contains(type)) {
			return null;
		}

		try {
			return "@" + Integer.toHexString(o.hashCode());
		} catch (NullPointerException e) {
			// NullPointerException会在代理类中出现
			return null;
		}
	}
}
