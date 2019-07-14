package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

public interface IGiven {
    /**
     * 前置条件
     *
     * @param description
     * @param lambda
     * @return
     * @throws RuntimeException
     */
    IGiven given(String description, SExecutor lambda) throws RuntimeException;

    /**
     * 前置条件
     *
     * @param lambda
     * @return
     * @throws RuntimeException
     */
    IGiven given(SExecutor lambda) throws RuntimeException;

    /**
     * 执行测试动作
     *
     * @param description
     * @param lambda
     * @return
     * @throws RuntimeException
     */
    IThen when(String description, SExecutor lambda) throws RuntimeException;

    /**
     * 执行测试动作
     *
     * @param lambda
     * @return
     * @throws RuntimeException
     */
    IThen when(SExecutor lambda) throws RuntimeException;
}
