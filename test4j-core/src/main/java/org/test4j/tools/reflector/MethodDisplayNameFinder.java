package org.test4j.tools.reflector;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.ClazzHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * MethodDisplayNameFinder 查找测试方法显示名称
 *
 * @author darui.wu
 * @create 2019/11/11 5:54 下午
 */
public class MethodDisplayNameFinder {

    static String DISPLAY_NAME = "org.junit.jupiter.api.DisplayName";

    static DisplayNameMethod displayNameMethod = null;

    static {
        displayName();
    }

    public static String displayName(Method method) {
        if (displayNameMethod != null) {
            Object o = method.getDeclaredAnnotation(displayNameMethod.klass);
            if (o != null) {
                return MethodAccessor.method(displayNameMethod.method).invoke(o);
            }
        }
        return method.getName();
    }


    private static void displayName() {
        try {
            if (ClazzHelper.isClassAvailable(DISPLAY_NAME)) {
                Class klass = ClazzHelper.getClazz(DISPLAY_NAME);
                displayNameMethod = new DisplayNameMethod(klass);
            }
        } catch (Exception e) {
            MessageHelper.warn(e.getMessage());
        }
    }

    @Accessors(chain = true)
    static class DisplayNameMethod {
        Class klass;

        Method method;

        public DisplayNameMethod(Class klass) throws Exception {
            this.klass = klass;
            this.method = this.klass.getMethod("value");
        }
    }
}