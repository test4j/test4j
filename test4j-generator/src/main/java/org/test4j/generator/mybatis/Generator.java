package org.test4j.generator.mybatis;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.TableConfig;

import java.util.function.Consumer;

/**
 * IGenerator
 *
 * @author:darui.wu Created by darui.wu on 2020/5/22.
 */
public interface Generator {
    /**
     * 生成fluent mybatis文件
     *
     * @return
     */
    static Generator fluentMybatis() {
        return MyBatisGenerator.build();
    }

    /**
     * 仅仅生成table datamap文件
     *
     * @return
     */
    static Generator tableMap() {
        return TableMapGenerator.build();
    }

    /**
     * 设置生成的全局配置
     *
     * @param consumer
     * @return
     */
    Generator globalConfig(Consumer<GlobalConfig> consumer);

    /**
     * 设置要生成的表
     *
     * @param consumer
     * @return
     */
    Generator tables(Consumer<TableConfig> consumer);


    /**
     * 执行生成
     */
    void execute();
}