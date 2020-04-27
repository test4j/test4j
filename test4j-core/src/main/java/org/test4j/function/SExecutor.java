package org.test4j.function;

import java.io.Serializable;

/**
 * SExecutor 无返回值的执行
 *
 * @author wudarui
 */
@FunctionalInterface
public interface SExecutor extends Serializable {
    /**
     * 执行动作
     *
     * @throws Exception
     */
    void doIt() throws Exception;
}
