package org.test4j.function;

@FunctionalInterface
public interface ESupplier<T> {
    /**
     * 方法可以抛出异常的Supplier
     *
     * @return
     * @throws Exception
     */
    T get() throws Exception;
}
