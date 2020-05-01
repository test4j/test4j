package org.test4j.generator.mybatis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import org.test4j.generator.mybatis.rule.DateType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局配置
 *
 * @author hubin
 * @since 2016-12-02
 */
@Data
@Accessors(chain = true)
public class GlobalConfig {

    /**
     * 生成文件的输出目录【默认 D 盘根目录】
     */
    private String outputDir = "D://";





    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache = false;



    /**
     * 开启 Kotlin 模式
     */
    private boolean kotlin = false;

    /**
     * 开启 swagger2 模式
     */
    private boolean swagger2 = false;

    /**
     * 开启 ActiveRecord 模式
     */
    private boolean activeRecord = false;

    /**
     * 开启 BaseResultMap
     */
    private boolean baseResultMap = false;


    /**
     * 各层文件名称方式，例如： %sAction 生成 UserAction
     * %s 为占位符
     */
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    /**
     * 指定生成的主键的ID类型
     */
    private IdType idType;
}
