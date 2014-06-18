package org.test4j.module.spring.utility;

import java.lang.reflect.Method;

import org.junit.Test;
import org.test4j.module.ICore;
import org.test4j.tools.commons.MethodHelper;

public class SpringInitHelperTest_MethodQualifier implements ICore {
    @Test
    public void testIsSpringInitMethod_PrivateMethod() throws Exception {
        Method method = MethodHelper.getMethod(SpringInitDemo.class, "privateMethod");
        boolean isSpringInitMethod = (Boolean) reflector.invokeStatic(SpringInitInvoker.class, "isSpringInitMethod",
                method);
        want.bool(isSpringInitMethod).isEqualTo(true);
    }

    @Test
    public void testIsSpringInitMethod_ProtectedeMethod() throws Exception {
        Method method = MethodHelper.getMethod(SpringInitDemo.class, "protectedMethod");
        boolean isSpringInitMethod = (Boolean) reflector.invokeStatic(SpringInitInvoker.class, "isSpringInitMethod",
                method);
        want.bool(isSpringInitMethod).isEqualTo(true);
    }

    @Test
    public void testIsSpringInitMethod_publicMethod() throws Exception {
        Method method = MethodHelper.getMethod(SpringInitDemo.class, "publicMethod");
        reflector.invokeStatic(SpringInitInvoker.class, "isSpringInitMethod", method);
    }

    @Test
    public void testIsSpringInitMethod_HasParameter() throws Exception {
        Method method = MethodHelper.getMethod(SpringInitDemo.class, "hasParameter", int.class);
        try {
            reflector.invokeStatic(SpringInitInvoker.class, "isSpringInitMethod", method);
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("can't have any parameter");
        }
    }

    @Test
    public void testIsSpringInitMethod_NormalMethod() throws Exception {
        Method method = MethodHelper.getMethod(SpringInitDemo.class, "normalMethod");
        boolean isSpringInitMethod = (Boolean) reflector.invokeStatic(SpringInitInvoker.class, "isSpringInitMethod",
                method);
        want.bool(isSpringInitMethod).isEqualTo(false);
    }
}
