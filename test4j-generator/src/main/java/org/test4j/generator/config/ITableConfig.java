package org.test4j.generator.config;

import java.util.function.Consumer;

/**
 * ITableConfig
 *
 * @author:darui.wu Created by darui.wu on 2020/5/27.
 */
public interface ITableConfig {
    /**
     * 设置要生成的表
     *
     * @param consumer
     * @return
     */
    ITableConfig tables(Consumer<ITableConfigSet> consumer);


    /**
     * 执行生成
     */
    void execute();
}