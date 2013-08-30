package org.jtester.tools.reflector.imposteriser;

import java.lang.reflect.Field;

import org.jtester.tools.commons.FieldHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class JTesterProxy {
	public static Imposteriser imposteriser = ClassImposteriser.INSTANCE;

	/**
	 * 构造一个代理类，将代理类的操作转移到testedObject属性 (fieldName)对象上<br>
	 * 构造一个type类型的mock spring bean
	 * 
	 * @param <T>
	 * @param name
	 *            spring bean的名称
	 * @param type
	 *            spring bean的类型
	 * @return
	 */
	public static <T> T proxy(final Class testClazz, final Field field) {
		FieldProxy handler = new FieldProxy(testClazz, field.getName());
		Class type = field.getType();
		return (T) imposteriser.imposterise(handler, type);
	}

	/**
	 * 构造一个代理类，将代理类的操作转移到testedObject属性 (fieldName)对象上<br>
	 * 通常用于实现测试类的@SpringBeanFrom的spring bean
	 * 
	 * @param <T>
	 * @param testedObject
	 *            测试对象
	 * @param fieldName
	 *            测试对象的字段名称
	 * @return
	 */
	public static <T> T proxy(final Class testClazz, final String fieldName) {
		FieldProxy handler = new FieldProxy(testClazz, fieldName);
		Field field = FieldHelper.getField(testClazz, fieldName);
		Class type = field.getType();
		return (T) imposteriser.imposterise(handler, type);
	}

	/**
	 * 实现类(className)的代理操作
	 * 
	 * @param <T>
	 * @param handler
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T> T proxy(final Invokable handler, final String className) {
		try {
			Class clazz = Class.forName(className);
			return (T) imposteriser.imposterise(handler, clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 实现类(className)的代理操作
	 * 
	 * @param <T>
	 * @param handler
	 * @param clazz
	 * @return
	 */
	public static <T> T proxy(final Invokable handler, final Class clazz) {
		return (T) imposteriser.imposterise(handler, clazz);
	}
}
