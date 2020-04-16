package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

public interface IGiven extends IAroundInitial, IWhen {
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
}
