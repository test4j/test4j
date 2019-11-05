package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

import java.util.function.Consumer;

public interface IThen {
    /**
     * 验证结果
     *
     * @param description
     * @param lambda
     * @return
     * @throws RuntimeException
     */
    IThen then(String description, SExecutor lambda) throws RuntimeException;

    /**
     * 验证结果
     *
     * @param lambda
     * @return
     * @throws RuntimeException
     */
    IThen then(SExecutor lambda) throws RuntimeException;

    /**
     * 对测试中抛出的异常进行验证
     *
     * @param consumer
     * @return
     * @throws RuntimeException
     */
    IThen want(Consumer<Throwable> consumer) throws RuntimeException;
}
