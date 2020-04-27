package org.test4j.function;

/**
 * ReturnExecutor 有返回值的执行
 *
 * @author wudarui
 */
@FunctionalInterface
public interface ReturnExecutor {
    /**
     * 执行动作
     *
     * @return 返回值
     * @throws Exception
     */
    Object doIt() throws Exception;

    /**
     * 封装SExecutor为ReturnExecutor
     *
     * @param executor
     * @return
     */
    static ReturnExecutor wrap(SExecutor executor) {
        return () -> {
            executor.doIt();
            return null;
        };
    }
}
