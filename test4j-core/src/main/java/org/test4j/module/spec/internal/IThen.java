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
     * @param consumer 对异常进行断言
     * @return
     * @throws RuntimeException
     */
    default <E extends Throwable> IThen want(Consumer<E> consumer) throws RuntimeException {
        return this.want("异常验证", consumer);
    }

    /**
     * 对测试中抛出的异常进行验证
     *
     * @param description 描述
     * @param consumer    对异常进行断言
     * @param <E>
     * @return
     * @throws RuntimeException
     */
    <E extends Throwable> IThen want(String description, Consumer<E> consumer) throws RuntimeException;

    /**
     * 对执行结果进行断言
     *
     * @param consumer 对执行结果进行断言
     * @return
     * @throws RuntimeException
     */
    default <T> IThen wantResult(Consumer<T> consumer) throws RuntimeException {
        return this.wantResult("对执行结果进行验证", consumer);
    }

    /**
     * 对执行结果进行断言
     *
     * @param description 描述
     * @param consumer    对执行结果进行断言
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    <T> IThen wantResult(String description, Consumer<T> consumer) throws RuntimeException;
}