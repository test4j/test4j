package org.test4j.function;

import java.io.Serializable;

@FunctionalInterface
public interface SExecutor extends Serializable {
    /**
     * 执行动作
     * @throws Exception
     */
    void doIt() throws Exception;
}
