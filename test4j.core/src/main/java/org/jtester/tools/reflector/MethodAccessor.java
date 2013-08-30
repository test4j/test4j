package org.jtester.tools.reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.jtester.module.JTesterException;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.ExceptionWrapper;
import org.jtester.tools.commons.MethodHelper;
import org.jtester.tools.exception.NoSuchMethodRuntimeException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MethodAccessor<T> {
	private final Method method;
	private final Class targetClaz;

	public MethodAccessor(Class targetClaz, String methodName, Class... parametersType) {
		this.targetClaz = targetClaz;
		this.method = MethodHelper.getMethod(targetClaz, methodName, parametersType);
	}

	/**
	 * 
	 * @param targetObj
	 * @param targetClazz
	 * @param methodName
	 * @param parametersType
	 */
	public MethodAccessor(Object target, String methodName, Class... parametersType) {
		this.targetClaz = target.getClass();
		this.method = MethodHelper.getMethod(targetClaz, methodName, parametersType);
	}

	public MethodAccessor(Method method) {
		this.method = method;
		this.targetClaz = method.getDeclaringClass();
	}

	public Method getMethod() {
		return this.method;
	}

	public T invoke(Object target, Object[] args) throws Exception {
		boolean isAccessible = this.method.isAccessible();
		try {
			this.method.setAccessible(true);
			return (T) method.invoke(target, args);
		} catch (InvocationTargetException e) {
			Throwable te = e.getTargetException();
			if (te instanceof Exception) {
				throw (Exception) te;
			} else {
				throw e;
			}
		} finally {
			this.method.setAccessible(isAccessible);
		}
	}

	/**
	 * 调用方法，不显式抛出异常<br>
	 * 原方法如果有显式异常，将被封装
	 * 
	 * @param target
	 * @param args
	 * @return
	 */
	public T invokeUnThrow(Object target, Object[] args) {
		try {
			return invoke(target, args);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			throw ExceptionWrapper.wrapWithRuntimeException(targetException);
		} catch (Throwable e) {
			throw ExceptionWrapper.wrapWithRuntimeException(e);
		}
	}

	public T invokeStatic(Object[] args) throws Exception {
		if (Modifier.isStatic(method.getModifiers()) == false) {
			String methodDesc = method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")";
			throw new NoSuchMethodRuntimeException("No such static method: " + methodDesc + " in class["
					+ this.targetClaz + "]");
		} else {
			return (T) invoke(null, args);
		}
	}

	public T invokeStaticUnThrow(Object[] args) {
		try {
			return (T) invokeStatic(args);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			throw ExceptionWrapper.wrapWithRuntimeException(targetException);
		} catch (Throwable e) {
			throw ExceptionWrapper.wrapWithRuntimeException(e);
		}
	}

	/**
	 * Invokes the given method with the given parameters on the given target
	 * object
	 * 
	 * @param target
	 *            The object containing the method, not null
	 * @param method
	 *            The method, not null
	 * @param arguments
	 *            The method arguments
	 * @return The result of the invocation, null if void
	 * @throws JTesterException
	 *             if the method could not be invoked
	 */
	public static <T> T invokeMethod(Object target, Method method, Object... arguments) throws Exception {
		boolean isAccessible = method.isAccessible();
		try {
			method.setAccessible(true);
			return (T) method.invoke(target, arguments);
		} finally {
			method.setAccessible(isAccessible);
		}
	}

	public static <T> T invokeMethodUnThrow(Object target, Method method, Object... arguments) {
		if (method == null) {
			throw new RuntimeException("reflector invoke ,the argument[method] can't be null.");
		}
		try {
			return (T) invokeMethod(target, method, arguments);
		} catch (Exception e) {
			throw new JTesterException("Unable to invoke method[" + method.getName() + "].", e);
		}
	}

	public static <T> T invokeMethod(Object target, String methodNmae, Object... paras) throws Exception {
		if (target == null) {
			throw new RuntimeException("the target object can't be null!");
		}

		Object _target = ClazzHelper.getProxiedObject(target);

		Class[] paraClazes = MethodHelper.getParameterClazz(paras);
		Method method = MethodHelper.getMethod(_target.getClass(), methodNmae, paraClazes);

		Object result = invokeMethod(_target, method, paras);
		return (T) result;
	}

	/**
	 * 反射调用方法，不显式的抛出受检异常<br>
	 * 原生的受检异常会被包装成运行时异常
	 * 
	 * @param <T>
	 * @param target
	 * @param methodName
	 * @param paras
	 * @return
	 */
	public static <T> T invokeMethodUnThrow(Object target, String methodName, Object... paras) {
		try {
			return (T) invokeMethod(target, methodName, paras);
		} catch (Exception e) {
			throw new JTesterException("Unable to invoke method[" + methodName + "].", e);
		}
	}
}
