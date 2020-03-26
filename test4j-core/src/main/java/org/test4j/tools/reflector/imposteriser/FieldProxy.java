package org.test4j.tools.reflector.imposteriser;

import org.test4j.module.core.internal.Test4JContext;
import org.test4j.tools.reflector.FieldAccessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 目标对象字段的代理<br>
 * 用于运行时动态获得目标对象字段的实际值调用<br>
 *
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class FieldProxy implements Invokable {
    private final String fieldName;

    private final FieldAccessor accessor;

    private final Class testClazz;

    public FieldProxy(final Class testClazz, final String fieldName) {
        this.fieldName = fieldName;
        this.accessor = FieldAccessor.field(testClazz, fieldName);
        this.testClazz = testClazz;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Object test = Test4JContext.currTestedObject();
        Object fieldValue = accessor.get(test);
        if (fieldValue == null) {
            throw new NullPointerException(String.format("field[%s] value of object[%s] is null.", fieldName,
                    testClazz.getName()));
        }
        try {
            Method method = invocation.getInvokedMethod();
            Object[] paras = invocation.getParametersAsArray();
            boolean accessible = method.isAccessible();
            if (accessible == false) {
                method.setAccessible(true);
            }
            Object o = method.invoke(fieldValue, paras);
            if (accessible == false) {
                method.setAccessible(false);
            }
            return o;
        } catch (Throwable e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            } else {
                throw e;
            }
        }
    }
}
