package org.test4j.tools.reflector;

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

    static Map<String, KlassMethod> map = new HashMap<>();

    static {
        displayName();
    }

    public static String displayName(Method method) {
        if (map.containsKey(DISPLAY_NAME)) {
            KlassMethod klassMethod = map.get(DISPLAY_NAME);
            Object o = method.getDeclaredAnnotation(klassMethod.klass);
            if (o != null) {
                return MethodAccessor.method(method).invoke(o);
            }
        }
        return method.getName();
    }


    private static void displayName() {
        try {
            if (!ClazzHelper.isClassAvailable(DISPLAY_NAME)) {
                return;
            }
            KlassMethod klassMethod = new KlassMethod();
            klassMethod.klass = ClazzHelper.getClazz(DISPLAY_NAME);
            klassMethod.method = klassMethod.klass.getMethod("value");
            map.put(DISPLAY_NAME, klassMethod);
        } catch (Exception e) {
            MessageHelper.warn(e.getMessage());
        }
    }

    static class KlassMethod {
        Class klass;

        Method method;
    }
}