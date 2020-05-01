package org.test4j.hamcrest;

/**
 * 简单断言
 *
 * @author wudarui
 */
public class Assert {
    public static void notNull(Object obj, String message, Object... args) {
        if (obj == null) {
            throw new AssertionError(String.format(message, args));
        }
    }
}
