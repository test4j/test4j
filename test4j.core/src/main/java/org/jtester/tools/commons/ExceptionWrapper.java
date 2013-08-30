package org.jtester.tools.commons;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.jtester.tools.reflector.MethodAccessor;

/**
 * 异常包装器
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public class ExceptionWrapper {
	/**
	 * 把Throwable包装为运行时异常抛出<br>
	 * 若throwable==null，则什么事都不干
	 * 
	 * @param throwable
	 */
	public static void throwRuntimeException(Throwable throwable) {
		if (throwable == null) {
			return;
		}
		if (throwable instanceof RuntimeException) {
			throw (RuntimeException) throwable;
		} else if (throwable instanceof Error) {
			throw (Error) throwable;
		} else {
			throw new RuntimeException(throwable);
		}
	}

	/**
	 * 把Throwable包装为运行时异常抛出<br>
	 * 若throwable==null，则什么事都不干
	 * 
	 * @param error
	 *            附加的异常消息，使异常信息更明确
	 * @param throwable
	 */
	public static void throwRuntimeException(String error, Throwable throwable) {
		if (throwable == null) {
			return;
		}
		throw new RuntimeException(error, throwable);
	}

	/**
	 * 包装advised的异常信息，使提示更明显
	 * 
	 * @param advised
	 * @param e
	 */
	public static RuntimeException wrapdAdvisedException(Object object, Exception e) {
		try {
			Class advisedClaz = Class.forName("org.springframework.aop.framework.Advised");
			if (!(object.getClass().isAssignableFrom(advisedClaz))) {
				return new RuntimeException(e);
			}
			StringBuilder sb = new StringBuilder();

			sb.append("value[" + object + "] is org.springframework.aop.framework.Advised\n");
			if ((Boolean) new MethodAccessor(advisedClaz, "isProxyTargetClass").invoke(object, new Object[] {})) {
				sb.append("proxied by the full target class");
				return new RuntimeException(sb.toString(), e);
			} else {
				Class[] clazzes = (Class[]) new MethodAccessor(advisedClaz, "getProxiedInterfaces").invoke(object,
						new Object[] {});
				sb.append("proxyed by the interfaces:\n");
				for (Class clazz : clazzes) {
					sb.append("\t" + clazz.getName() + "\n");
				}
				return new RuntimeException(sb.toString(), e);
			}
		} catch (Exception e1) {
			return new RuntimeException(e);
		}
	}

	/**
	 * 返回反射代理的本源异常
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException getUndeclaredThrowableExceptionCaused(Throwable e) {
		if (!(e instanceof UndeclaredThrowableException)) {
			return wrapWithRuntimeException(e);
		}
		Throwable e1 = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
		if (!(e1 instanceof InvocationTargetException)) {
			return wrapWithRuntimeException(e1);
		}
		Throwable e2 = ((InvocationTargetException) e1).getTargetException();
		return wrapWithRuntimeException(e2);
	}

	public static RuntimeException wrapWithRuntimeException(Throwable e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	public static String toString(Throwable e) {
		if (e == null) {
			return null;
		}
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}
