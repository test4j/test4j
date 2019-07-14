package org.test4j.function;

@FunctionalInterface
public interface EFunction<T, R> {
    /**
     * 方法可以抛出异常的Function
     *
     * @param t
     * @return
     * @throws Exception
     */
    R apply(T t) throws Exception;
}
