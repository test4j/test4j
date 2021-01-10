package org.test4j.module.spec.internal;

import org.test4j.function.ReturnExecutor;
import org.test4j.function.SExecutor;

/**
 * IWhen
 *
 * @author:darui.wu Created by darui.wu on 2020/4/14.
 */
public interface IWhen {
    /**
     * 执行测试动作
     *
     * @param description 步骤描述
     * @param lambda      执行动作
     * @return
     * @throws RuntimeException
     */
    default IThen when(String description, SExecutor lambda) throws RuntimeException {
        return this.when(description, ReturnExecutor.wrap(lambda));
    }

    /**
     * 执行测试动作
     *
     * @param description 步骤描述
     * @param lambda      执行动作
     * @return
     * @throws RuntimeException
     */
    IThen when(String description, ReturnExecutor lambda) throws RuntimeException;

    /**
     * 执行测试动作
     *
     * @param lambda 执行动作
     * @return
     * @throws RuntimeException
     */
    default IThen when(SExecutor lambda) throws RuntimeException {
        return this.when(ReturnExecutor.wrap(lambda));
    }

    /**
     * 执行测试动作
     *
     * @param lambda 执行动作
     * @return
     * @throws RuntimeException
     */
    IThen when(ReturnExecutor lambda) throws RuntimeException;

    /**
     * 执行测试方法，并预计执行会出错
     *
     * @param description 描述
     * @param lambda      执行动作
     * @param eKlass      期望抛出的异常类型
     * @return
     * @throws RuntimeException
     */
    default IThen when(String description, SExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException {
        return this.when(description, ReturnExecutor.wrap(lambda), eKlass);
    }

    /**
     * 执行测试方法，并预计执行会出错
     *
     * @param description 描述
     * @param lambda      执行动作
     * @param eKlass      期望抛出的异常类型
     * @return
     * @throws RuntimeException
     */
    IThen when(String description, ReturnExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException;

    /**
     * 执行测试方法，并预计执行会出错
     *
     * @param lambda 执行动作
     * @param eKlass 期望抛出的异常类型
     * @return
     * @throws RuntimeException
     */
    default IThen when(SExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException {
        return this.when(ReturnExecutor.wrap(lambda), eKlass);
    }

    /**
     * 执行测试方法，并预计执行会出错
     *
     * @param lambda 执行动作
     * @param eKlass 期望抛出的异常类型
     * @return
     * @throws RuntimeException
     */
    IThen when(ReturnExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException;
}