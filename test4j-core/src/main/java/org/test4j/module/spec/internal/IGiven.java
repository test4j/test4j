package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

public interface IGiven extends IAround, IWhen {
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
     * 执行 前置数据准备 和 后置数据检查
     * 文件名形式为 testClassName.testMethodName.json
     *
     * @return
     * @throws RuntimeException
     */
    IAround aroundDb() throws RuntimeException;

    /**
     * 执行 前置数据准备 和 后置数据检查
     *
     * @param file json文件
     * @return
     * @throws RuntimeException
     */
    IAround aroundDb(String file) throws RuntimeException;
}
