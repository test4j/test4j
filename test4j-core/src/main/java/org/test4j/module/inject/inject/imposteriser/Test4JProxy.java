package org.test4j.module.inject.inject.imposteriser;

import java.lang.reflect.Field;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class Test4JProxy {
    public static Imposteriser imposteriser = ClassImposteriser.INSTANCE;

    /**
     * 构造一个代理类，将代理类的操作转移到testedObject属性 (fieldName)对象上<br>
     * 构造一个type类型的mock spring bean
     *
     * @param <T>
     * @param testClazz 测试类
     * @param field     属性
     * @return
     */
    public static <T> T proxy(final Class testClazz, final Field field) {
        FieldProxy handler = new FieldProxy(testClazz, field.getName());
        Class type = field.getType();
        return (T) imposteriser.imposterise(handler, type);
    }

}