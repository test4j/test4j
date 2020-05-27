package org.test4j.tools.commons;

import org.test4j.tools.reflector.MethodAccessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常包装器
 *
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class ExceptionWrapper {
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
