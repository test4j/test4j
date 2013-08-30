package org.jtester.module.spring.utility;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jtester.module.spring.annotations.SpringInitMethod;

@SuppressWarnings("rawtypes")
public class SpringInitInvoker {
	/**
	 * 用来在jTester初始化之前工作<br>
	 * 比如spring加载前的mock工作等
	 * 
	 * @param test
	 *            测试类实例
	 */
	public static void invokeSpringInitMethod(Object test) {
		Class claz = test.getClass();
		Method[] methods = claz.getDeclaredMethods();
		for (Method method : methods) {
			boolean isSpringInitMethod = isSpringInitMethod(method);
			if (isSpringInitMethod == false) {
				continue;
			}
			boolean accessible = method.isAccessible();
			method.setAccessible(true);
			try {
				method.invoke(test);
			} catch (Exception e) {
				throw new RuntimeException("invoke @SpringInitMethod " + method.getName() + " error.", e);
			} finally {
				method.setAccessible(accessible);
			}
		}
	}

	/**
	 * 判断是否是@SpringInitMethod方法
	 * 
	 * @param method
	 * @return
	 */
	private static boolean isSpringInitMethod(Method method) {
		SpringInitMethod springInitMethod = method.getAnnotation(SpringInitMethod.class);
		if (springInitMethod == null) {
			return false;
		}
		int mod = method.getModifiers();
		if (Modifier.isPublic(mod)) {
			throw new RuntimeException("@SpringInitMethod should be private, protected or package.");
		}

		Class<?>[] paras = method.getParameterTypes();
		if (paras.length != 0) {
			throw new RuntimeException("the @SpringInitMethod can't have any parameter.");
		}
		return true;
	}
}
