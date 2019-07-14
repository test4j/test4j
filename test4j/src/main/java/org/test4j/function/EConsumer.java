package org.test4j.function;

@FunctionalInterface
public interface EConsumer<T> {
    /**
     * 带异常的Consumer
     *
     * @param t
     * @throws Exception
     */
    void accept(T t) throws Exception;
}
