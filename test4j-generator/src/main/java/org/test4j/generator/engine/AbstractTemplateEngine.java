package org.test4j.generator.engine;

import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.model.BuildConfig;

import java.io.File;
import java.util.*;


/**
 * 模板引擎抽象类
 *
 * @author darui.wu
 */
@Slf4j
public abstract class AbstractTemplateEngine {
    /**
     * 配置信息
     */
    protected BuildConfig config;

    /**
     * 模板引擎初始化
     */
    public AbstractTemplateEngine init(BuildConfig config) {
        this.config = config;
        return this;
    }

    /**
     * 输出模板
     *
     * @param template
     */
    public void output(String template, Map<String, Object> config, String filePath) {
        try {
            File dir = new File(filePath).getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            this.writer(config, template, filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;



//    /**
//     * 渲染对象 MAP 信息
//     *
//     * @param tableInfo 表信息对象
//     * @return ignore
//     */
//    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
//        Map<String, Object> objectMap = new HashMap<>(30);
//        ConfigBuilder config = getConfigBuilder();
//
//        objectMap.put("config", config);
//        objectMap.put("package", config.getPackageInfo());
//        GlobalConfig globalConfig = config.getGlobalConfig();
//        objectMap.put("author", globalConfig.getAuthor());
//        objectMap.put("idType", globalConfig.getIdType() == null ? null : globalConfig.getIdType().toString());
//        objectMap.put("logicDeleteFieldName", config.getStrategyConfig().getLogicDeleteFieldName());
//        objectMap.put("versionFieldName", config.getStrategyConfig().getVersionFieldName());
//        objectMap.put("activeRecord", globalConfig.isActiveRecord());
//        objectMap.put("kotlin", globalConfig.isKotlin());
//        objectMap.put("swagger2", globalConfig.isSwagger2());
//        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        objectMap.put("table", tableInfo);
//        objectMap.put("enableCache", globalConfig.isEnableCache());
//        objectMap.put("baseResultMap", globalConfig.isBaseResultMap());
//        objectMap.put("baseColumnList", globalConfig.isBaseColumnList());
//        objectMap.put("entity", tableInfo.getEntityName());
//        objectMap.put("entitySerialVersionUID", config.getStrategyConfig().isEntitySerialVersionUID());
//        objectMap.put("entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant());
//        objectMap.put("entityBuilderModel", config.getStrategyConfig().isEntityBuilderModel());
//        objectMap.put("entityLombokModel", config.getStrategyConfig().isEntityLombokModel());
//        objectMap.put("entityBooleanColumnRemoveIsPrefix", config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
//        objectMap.put("superEntityClass", getSuperClassName(config.getSuperEntityClass()));
//        objectMap.put("superMapperClassPackage", config.getSuperMapperClass());
//        objectMap.put("superMapperClass", getSuperClassName(config.getSuperMapperClass()));
//        objectMap.put("superServiceClassPackage", config.getSuperServiceClass());
//        objectMap.put("superServiceClass", getSuperClassName(config.getSuperServiceClass()));
//        objectMap.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
//        objectMap.put("superServiceImplClass", getSuperClassName(config.getSuperServiceImplClass()));
//        objectMap.put("superControllerClassPackage", config.getSuperControllerClass());
//        objectMap.put("superControllerClass", getSuperClassName(config.getSuperControllerClass()));
//        return Objects.isNull(config.getInjectionConfig()) ? objectMap : config.getInjectionConfig().prepareObjectMap(objectMap);
//    }
}
