package org.jtester.tools.commons;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtester.tools.exception.NoSuchMethodRuntimeException;
import org.jtester.tools.reflector.MethodAccessor;
import org.jtester.tools.reflector.ReflectionException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class MethodHelper {
	/**
	 * 返回class中名称为name,参数类型为 parametersType的方法
	 * 
	 * @param cls
	 * @param name
	 * @param parametersType
	 * @return
	 */
	public static final Method getMethod(Class cls, String name, Class... parametersType) {
		while (cls != Object.class) {
			try {
				Method[] methods = cls.getDeclaredMethods();
				for (Method method : methods) {
					if (method.getName().equals(name) == false) {
						continue;
					}
					if (matchParasType(parametersType, method.getParameterTypes())) {
						method.setAccessible(true);
						return method;
					}
				}
				throw new NoSuchMethodRuntimeException();
			} catch (NoSuchMethodRuntimeException e) {
				cls = cls.getSuperclass();
			}
		}
		throw new NoSuchMethodRuntimeException("No such method: " + name + "(" + Arrays.toString(parametersType) + ")");
	}

	/**
	 * 查找名为name，参数个数为args的方法列表
	 * 
	 * @param cls
	 * @param name
	 * @param args
	 * @return
	 */
	public static final List<Method> getMethod(Class cls, String name, int args) {
		List<Method> methods = new ArrayList<Method>();
		while (cls != Object.class) {
			try {
				Method[] declares = cls.getDeclaredMethods();
				for (Method method : declares) {
					if (method.getName().equals(name) == false) {
						continue;
					}
					if (method.getParameterTypes().length == args) {
						methods.add(method);
					}
				}
				throw new NoSuchMethodRuntimeException();
			} catch (NoSuchMethodRuntimeException e) {
				cls = cls.getSuperclass();
			}
		}
		return methods;
	}

	/**
	 * Returns all declared setter methods of fields of the given class that are
	 * assignable from the given type.
	 * 
	 * @param clazz
	 *            The class to get setters from, not null
	 * @param type
	 *            The type, not null
	 * @param isStatic
	 *            True if static setters are to be returned, false for
	 *            non-static
	 * @return A list of Methods, empty list if none found
	 */
	public static Set<Method> getSettersAssignableFrom(Class clazz, Type type, boolean isStatic) {
		Set<Method> settersAssignableFrom = new HashSet<Method>();

		Set<Method> allMethods = getAllMethods(clazz);
		for (Method method : allMethods) {
			if (isSetterMethod(method) && ClazzHelper.isAssignable(type, method.getGenericParameterTypes()[0])
					&& (isStatic == isStatic(method.getModifiers()))) {
				settersAssignableFrom.add(method);
			}
		}
		return settersAssignableFrom;
	}

	/**
	 * Gets all methods of the given class and all its super-classes.
	 * 
	 * @param clazz
	 *            The class
	 * @return The methods, not null
	 */
	public static Set<Method> getAllMethods(Class clazz) {
		Set<Method> result = new HashSet<Method>();
		if (clazz == null || clazz.equals(Object.class)) {
			return result;
		}

		// add all methods of this class
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method declaredMethod : declaredMethods) {
			if (declaredMethod.isSynthetic() || declaredMethod.isBridge()) {
				// skip methods that were added by the compiler
				continue;
			}
			result.add(declaredMethod);
		}
		// add all methods of the super-classes
		result.addAll(getAllMethods(clazz.getSuperclass()));
		return result;
	}

	/**
	 * Returns the setter methods in the given class that have an argument with
	 * the exact given type. The class's superclasses are also investigated.
	 * 
	 * @param clazz
	 *            The class to get the setter from, not null
	 * @param type
	 *            The type, not null
	 * @param isStatic
	 *            True if static setters are to be returned, false for
	 *            non-static
	 * @return All setters for an object of the given type
	 */
	public static Set<Method> getSettersOfType(Class clazz, Type type) {
		Set<Method> settersOfType = new HashSet<Method>();
		Set<Method> allMethods = getAllMethods(clazz);
		for (Method method : allMethods) {
			if (isSetterMethod(method) && method.getGenericParameterTypes()[0].equals(type)) {
				settersOfType.add(method);
			}
		}
		return settersOfType;
	}

	static boolean matchParasType(Class[] expecteds, Class[] actuals) {
		if (expecteds == null && actuals == null) {
			return true;
		} else if (expecteds == null || actuals == null) {
			return false;
		} else if (expecteds.length != actuals.length) {
			return false;
		}
		for (int index = 0; index < expecteds.length; index++) {
			Class expected = expecteds[index];
			Class actual = actuals[index];
			if (expected == null || expected == actual) {
				continue;
			} else if (actual != null && actual.isAssignableFrom(expected)) {
				continue;
			} else if (PrimitiveHelper.isPrimitiveTypeEquals(expected, actual)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param method
	 *            The method to check, not null
	 * @return True if the given method is the {@link Object#equals} method
	 */
	public static boolean isEqualsMethod(Method method) {
		return "equals".equals(method.getName()) && 1 == method.getParameterTypes().length
				&& Object.class.equals(method.getParameterTypes()[0]);
	}

	/**
	 * @param method
	 *            The method to check, not null
	 * @return True if the given method is the {@link Object#hashCode} method
	 */
	public static boolean isHashCodeMethod(Method method) {
		return "hashCode".equals(method.getName()) && 0 == method.getParameterTypes().length;
	}

	/**
	 * @param method
	 *            The method to check, not null
	 * @return True if the given method is the {@link Object#toString} method
	 */
	public static boolean isToStringMethod(Method method) {
		return "toString".equals(method.getName()) && 0 == method.getParameterTypes().length;
	}

	/**
	 * @param method
	 *            The method to check, not null
	 * @return True if the given method is the {@link Object#clone} method
	 */
	public static boolean isCloneMethod(Method method) {
		return "clone".equals(method.getName()) && 0 == method.getParameterTypes().length;
	}

	public static boolean isFinalizeMethod(Method method) {
		return "finalize".equals(method.getName()) && 0 == method.getParameterTypes().length;
	}

	/**
	 * 判断方法是否是setter方法<br>
	 * For each method, check if it can be a setter for an object of the given
	 * type. A setter is a method with the following properties:
	 * <ul>
	 * <li>Method name is > 3 characters long and starts with set</li>
	 * <li>The fourth character is in uppercase</li>
	 * <li>The method has one parameter, with the type of the property to set</li>
	 * </ul>
	 * 
	 * @param method
	 *            The method to check, not null
	 * @return True if the given method is a setter, false otherwise
	 */
	public static boolean isSetterMethod(Method method) {
		String methodName = method.getName();
		if (methodName.startsWith("set") == false || method.getParameterTypes().length != 1 || methodName.length() < 4) {
			return false;
		}

		String fourthLetter = methodName.substring(3, 4);
		if (fourthLetter.toUpperCase().equals(fourthLetter)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 调用类为clazz,名称为method的方法
	 * 
	 * @param methodName
	 * @param paras
	 * @return
	 * @throws Exception
	 */
	public static <T> T invoke(Object target, String method, Object... paras) throws Exception {
		Class[] paraClazes = getParameterClazz(paras);
		MethodAccessor accessor = new MethodAccessor(target, method, paraClazes);
		Object result = accessor.invoke(target, paras);
		return (T) result;
	}

	public static <T> T invokeUnThrow(Object target, String method, Object... paras) {
		Class[] paraClazes = getParameterClazz(paras);
		MethodAccessor accessor = new MethodAccessor(target, method, paraClazes);
		Object result = accessor.invokeUnThrow(target, paras);
		return (T) result;
	}

	public static Class[] getParameterClazz(Object... paras) {
		if (paras == null) {
			return new Class[0];
		}

		List<Class> clazes = new ArrayList<Class>();
		for (Object para : paras) {
			clazes.add(para == null ? null : para.getClass());
		}
		return clazes.toArray(new Class[0]);
	}

	/**
	 * 调用静态方法
	 * 
	 * @param <T>
	 * @param targetClass
	 * @param method
	 * @param paras
	 * @return
	 */
	public static <T> T invokeStatic(Class targetClass, String method, Object... paras) {
		List<Class> paraClazz = new ArrayList<Class>();
		if (paras != null) {
			for (Object para : paras) {
				paraClazz.add(para == null ? null : para.getClass());
			}
		}
		MethodAccessor accessor = new MethodAccessor(targetClass, method, paraClazz.toArray(new Class[0]));
		return (T) accessor.invokeStaticUnThrow(paras);
	}

	/**
	 * 根据方法的名称和参数个数查找方法访问器，如果有多于1个同名且参数个数一样的方法，那么抛出异常
	 * 
	 * @param target
	 * @param methodName
	 * @param args
	 * @return
	 */
	public static <T> MethodAccessor<T> findMethodByArgCount(Class targetClazz, String methodName, int args) {
		List<Method> methods = MethodHelper.getMethod(targetClazz, methodName, args);
		if (methods.size() == 0) {
			throw new ReflectionException("No such method: " + methodName + ",parameter count:" + args);
		}
		if (methods.size() > 1) {
			throw new ReflectionException("More then one method: " + methodName + ",parameter count:" + args);
		}
		Method method = methods.get(0);
		return new MethodAccessor<T>(method);
	}

	/**
	 * 根据方法的名称和参数个数查找方法访问器，如果有多于1个同名且参数个数一样的方法，那么抛出异常
	 * 
	 * @param <T>
	 * @param target
	 * @param methodName
	 * @param args
	 * @return
	 */
	public static <T> MethodAccessor<T> findMethodByArgCount(Object target, String methodName, int args) {
		return findMethodByArgCount(target.getClass(), methodName, args);
	}

	/**
	 * 测试类是否是 public static void且是无参形式的?
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isPublicStaticVoid(Method method) {
		return method.getReturnType() == void.class && method.getParameterTypes().length == 0
				&& (method.getModifiers() & Modifier.STATIC) != 0 && (method.getModifiers() & Modifier.PUBLIC) != 0;
	}
}
