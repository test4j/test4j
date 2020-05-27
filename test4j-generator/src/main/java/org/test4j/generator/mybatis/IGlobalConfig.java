package org.test4j.generator.mybatis;

import org.test4j.generator.mybatis.config.GlobalConfig;

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
    ITableConfig globalConfig(Consumer<GlobalConfig> consumer);
}