package org.test4j.generator.convert;

public class Util {
    public static void info(String info) {
        System.out.println(info);
    }

    /**
     * 未定义
     */
    public final static String NOT_DEFINED = "$$NOT_DEFINED$$";

    public static boolean isBlank(String in) {
        if (in == null) {
            return true;
        } else {
            return in.trim().isEmpty();
        }
    }
}
