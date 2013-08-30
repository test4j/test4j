package org.jtester.tools.reflector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jtester.tools.commons.ArrayHelper;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.commons.ListHelper;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;

/**
 * POJO属性值或Map访问
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PropertyAccessor {
	public static Object getPropertyByOgnl(final Object target, String ognl) {
		Object o = getPropertyByOgnl(target, ognl, true);
		return o;
	}

	/**
	 * 获得单值对象（非集合或数组,但包含Map）的单个属性值<br>
	 * 先访问get方法(is方法)，如果没有get方法再直接访问属性值
	 * 
	 * @param object
	 * @param ognlExpression
	 * @param throwException
	 * @return
	 */
	public static Object getPropertyByOgnl(final Object object, String ognl, boolean throwNoProperty) {
		String[] expressions = ognl.split("\\.");
		try {
			Object target = object;
			String key = "";
			for (String prop : expressions) {
				if (target == null && throwNoProperty == false) {
					return null;
				} else if (target instanceof Map) {
					Map map = (Map) target;
					key = key.equals("") ? prop : key + "." + prop;
					if (map.containsKey(key)) {
						target = map.get(key);
						key = "";
					}
				} else {
					target = getProperty(target, prop);
				}
			}
			if (key.equals("") == false) {
				throw new NoSuchFieldRuntimeException();
			} else {
				return target;
			}
		} catch (NoSuchFieldRuntimeException e) {
			if (throwNoProperty) {
				String clazname = object == null ? "null" : object.getClass().getName();
				throw new NoSuchFieldRuntimeException("can't find property[" + ognl + "] in object[" + clazname + "]",
						e);
			} else {
				return object;
			}
		}
	}

	/**
	 * * o 先根据get方法访问对象的属性，如果存在则返回<br>
	 * o 再根据is方法方法对象的属性，且方法值是bool型，返回<br>
	 * o 否则，直接方法对象的字段<br>
	 * o 如果对象是Map，则根据key值取
	 * 
	 * @param o
	 * @param prop
	 * @return
	 */
	public static Object getProperty(final Object o, String prop) {
		if (o == null) {
			throw new RuntimeException("can't get the property value from a null object.");
		}

		if (o instanceof Map) {
			Map map = (Map) o;
			if (map.containsKey(prop)) {
				return map.get(prop);
			} else {
				throw new NoSuchFieldRuntimeException("no key[" + prop + "] value in map.");
			}
		}

		Object target = ClazzHelper.getProxiedObject(o);
		try {
			String method = StringHelper.camel("get", prop);
			MethodAccessor accessor = new MethodAccessor(target, method);
			return accessor.invoke(target, new Object[] {});
		} catch (Throwable e) {
			try {
				String method = StringHelper.camel("is", prop);
				MethodAccessor accessor = new MethodAccessor(target, method);
				Object b = accessor.invoke(target, new Object[] {});
				if (b instanceof Boolean) {
					return b;
				}
				throw new RuntimeException();
			} catch (Throwable e1) {
				Object o2 = FieldHelper.getFieldValue(target, prop);
				return o2;
			}
		}
	}

	/**
	 * 获得单值对象（非集合或数组,但包含Map）的多个属性值
	 * 
	 * @param object
	 * @param ognl
	 * @param throwException
	 * @return
	 */
	public static Object[] getPropertyValue(Object object, String[] ognls, boolean throwException) {
		List<Object> os = new ArrayList<Object>();
		for (String ognl : ognls) {
			Object value = getPropertyByOgnl(object, ognl, throwException);
			os.add(value);
		}
		return os.toArray(new Object[0]);
	}

	/**
	 * 获得数组（集合)中各个对象的属性值列表<br>
	 * 
	 * @param arr
	 *            数组或集合,如果是单值(或Map)转为size=1的集合处理
	 * @param property
	 * @return
	 */
	public static List<?> getArrayItemProperty(Object arr, String property) {
		Collection coll = ListHelper.toList(arr);
		List values = new ArrayList();
		for (Object o : coll) {
			Object value = PropertyAccessor.getPropertyByOgnl(o, property, false);
			values.add(value);
		}
		return values;
	}

	/**
	 * 获得对象的属性列表<br>
	 * o 如果对象是集合或数组，返回集合对象的属性值的列表<br>
	 * o 如果对象是单值，且属性是集合或数组，直接返回单值对象的属性值<br>
	 * o 如果对象是单值，且属性是非集合和数组类型，构造包含这个属性值的列表
	 * 
	 * @param item
	 * @param property
	 * @return
	 */
	public static Collection getArrayOrItemProperty(Object item, String property) {
		Collection values = null;
		if (ArrayHelper.isCollOrArray(item)) {
			values = PropertyAccessor.getArrayItemProperty(item, property);
		} else {
			Object o = PropertyAccessor.getProperty(item, property);
			values = ListHelper.toList(o);
		}
		return values;
	}

	/**
	 * 获得数组（集合)中各个对象的多个属性值
	 * 
	 * @param arr
	 *            数组或集合,如果是单值(或Map)转为size=1的集合处理
	 * @param properties
	 * @return
	 */
	public static Object[][] getArrayItemProperties(Object arr, String[] properties) {
		Collection coll = ListHelper.toList(arr);

		List values = new ArrayList();
		for (Object o : coll) {
			Object[] props = PropertyAccessor.getPropertyValue(o, properties, false);
			values.add(props);
		}
		return (Object[][]) values.toArray(new Object[0][0]);
	}

	// ==========================
	/**
	 * 获得集合列表中对象的属性值列表
	 * 
	 * @param list
	 * @param properties
	 * @param throwNoProperty
	 * @return
	 */
	public static List<List> getPropertiesOfList(List list, String[] properties, boolean throwNoProperty) {
		List<List> result = new ArrayList<List>();
		for (Object target : list) {
			List items = new ArrayList();
			if (ArrayHelper.isCollOrArray(target) && throwNoProperty == false) {
				List value = ListHelper.toList(target);
				result.add(value);
				continue;
			}
			for (String ognl : properties) {
				Object value = PropertyAccessor.getPropertyByOgnl(target, ognl, throwNoProperty);
				items.add(value);
			}
			result.add(items);
		}
		return result;
	}

	/**
	 * 获得属性值列表的集合
	 * 
	 * @param list
	 * @param properties
	 * @param throwNoProperty
	 * @return
	 */
	public static List<List> getPropertySetsOfList(List list, String[] properties, boolean throwNoProperty) {
		List<List> result = new ArrayList<List>();
		for (String ognl : properties) {
			List items = new ArrayList();
			for (Object target : list) {
				if (ArrayHelper.isCollOrArray(target)) {
					List _target = ListHelper.toList(target);
					List values = getPropertyOfList(_target, ognl, throwNoProperty);
					items.add(values);
				} else {
					Object value = getPropertyByOgnl(target, ognl, throwNoProperty);
					items.add(value);
				}
			}
			result.add(items);
		}
		return result;
	}

	public static List getPropertyOfList(List list, String ognl, boolean throwNoProperty) {
		List result = new ArrayList();
		for (Object target : list) {
			Object value = PropertyAccessor.getPropertyByOgnl(target, ognl, throwNoProperty);
			result.add(value);
		}
		return result;
	}

	/**
	 * 获得PoJo对象或Map对象的属性值列表
	 * 
	 * @param target
	 * @param properties
	 * @param throwNoProperty
	 * @return
	 */
	public static List getPropertiesOfPoJo(Object target, String[] properties, boolean throwNoProperty) {
		List result = new ArrayList();
		for (String ognl : properties) {
			Object value = PropertyAccessor.getPropertyByOgnl(target, ognl, throwNoProperty);
			result.add(value);
		}
		return result;
	}
}
