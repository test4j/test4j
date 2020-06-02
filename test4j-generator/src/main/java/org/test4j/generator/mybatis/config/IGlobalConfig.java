package org.test4j.generator.mybatis.config;

import java.util.function.Consumer;

/**
 * IGenerator
 *
 * @author:darui.wu Created by darui.wu on 2020/5/22.
 */
public interface IGlobalConfig {
    /**
     * 设置生成的全局配置
     *
     * @param consumer
     * @return
     */
    ITableConfig globalConfig(Consumer<IGlobalConfigSet> consumer);
}