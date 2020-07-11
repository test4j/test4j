package org.test4j.generator.mybatis.config;

import java.util.function.Consumer;

/**
 * ITableConfig
 *
 * @author:darui.wu Created by darui.wu on 2020/6/1.
 */
public interface ITableConfigSet {
    /**
     * 增加表tableName映射关系
     *
     * @param tableName
     * @return
     */
    ITableConfigSet table(String tableName);

    /**
     * 增加表tableName映射关系
     *
     * @param tableName
     * @param consumer
     * @return
     */
    ITableConfigSet table(String tableName, Consumer<ITableSetter> consumer);

    /**
     * 对所有表统一处理
     *
     * @param consumer
     * @return
     */
    void foreach(Consumer<ITableSetter> consumer);
}