//package org.test4j.generator.mybatis;
//
//import java.io.Serializable;
//import java.util.List;
//
//import lombok.Data;
//import lombok.experimental.Accessors;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import org.test4j.generator.mybatis.config.*;
//import org.test4j.generator.mybatis.model.TableInfo;
//import org.test4j.generator.engine.AbstractTemplateEngine;
//import org.test4j.generator.engine.VelocityTemplateEngine;
//import org.test4j.generator.mybatis.template.AbstractTableTemplate;
//
///**
// * 生成文件
// *
// * @author YangHu, tangguo, hubin
// * @since 2016-08-30
// */
//@Data
//@Accessors(chain = true)
//public class AutoGenerator {
//
//    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);
//    /**
//     * 配置信息
//     */
//    protected ConfigBuilder config;
//    /**
//     * 注入配置
//     */
//    protected InjectionConfig injectionConfig;
//    /**
//     * 数据源配置
//     */
//    private DataSourceConfig dataSource;
//    /**
//     * 数据库表配置
//     */
//    private StrategyConfig strategy;
//    /**
//     * 包 相关配置
//     */
//    private PackageConfig packageInfo;
//    /**
//     * 模板 相关配置
//     */
//    private AbstractTableTemplate template;
//    /**
//     * 全局 相关配置
//     */
//    private GlobalConfig globalConfig;
//    /**
//     * 模板引擎
//     */
//    private AbstractTemplateEngine templateEngine;
//
////    /**
////     * 生成代码
////     */
////    public void execute() {
////        logger.debug("==========================准备生成文件...==========================");
////        // 初始化配置
////        if (null == config) {
////            config = new ConfigBuilder(packageInfo, dataSource, strategy, template, globalConfig);
////            if (null != injectionConfig) {
////                injectionConfig.setConfig(config);
////            }
////        }
////        if (null == templateEngine) {
////            // 为了兼容之前逻辑，采用 Velocity 引擎 【 默认 】
////            templateEngine = new VelocityTemplateEngine();
////        }
////        // 模板引擎初始化执行文件输出
////        templateEngine.init(this.pretreatmentConfigBuilder(config)).mkdirs().batchOutput().open();
////        logger.debug("==========================文件生成完成！！！==========================");
////    }
//
//    /**
//     * <p>
//     * 开放表信息、预留子类重写
//     * </p>
//     *
//     * @param config 配置信息
//     * @return
//     */
//    protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
//        return config.getTableInfoList();
//    }
//
//    /**
//     * <p>
//     * 预处理配置
//     * </p>
//     *
//     * @param config 总配置信息
//     * @return 解析数据结果集
//     */
////    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
////        /**
////         * 注入自定义配置
////         */
////        if (null != injectionConfig) {
////            injectionConfig.initMap();
////            config.setInjectionConfig(injectionConfig);
////        }
////        /**
////         * 表信息列表
////         */
////        List<TableInfo> tableList = this.getAllTableInfoList(config);
////        for (TableInfo tableInfo : tableList) {
////            /* ---------- 添加导入包 ---------- */
////
////            if (StringUtils.isNotEmpty(config.getSuperEntityClass())) {
////                // 父实体
////                tableInfo.addImportTypes(config.getSuperEntityClass());
////            } else {
////                tableInfo.addImportTypes(Serializable.class.getCanonicalName());
////            }
////            // Boolean类型is前缀处理
////            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()) {
////                tableInfo.getFields().stream().filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
////                    .filter(field -> field.getFieldName().startsWith("is"))
////                    .forEach(field -> field.setFieldName(config.getStrategyConfig(),
////                        StringUtils.removePrefixAfterPrefixToLower(field.getFieldName(), 2)));
////            }
////        }
////        return config.setTableInfoList(tableList);
////    }
//}
