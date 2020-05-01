package org.test4j.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
    private BuildConfig config;


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
            File dir = new File(filePath);
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


    /**
     * 输出 java xml 文件
     */
//    public AbstractTemplateEngine batchOutput() {
//        try {
//            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
//            for (TableInfo tableInfo : tableInfoList) {
//                Map<String, Object> objectMap = getObjectMap(tableInfo);
//                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
//                AbstractTableTemplate template = getConfigBuilder().getTemplate();
//                // 自定义内容
//                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
//                if (null != injectionConfig) {
//                    injectionConfig.initMap();
//                    objectMap.put("cfg", injectionConfig.getMap());
//                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
//                    if (CollectionUtils.isNotEmpty(focList)) {
//                        for (FileOutConfig foc : focList) {
//                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
//                                writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
//                            }
//                        }
//                    }
//                }
//                // Mp.java
//                String entityName = tableInfo.getEntityName();
//                if (null != entityName && null != pathInfo.get(FmGeneratorConst.ENTITY_PATH)) {
//                    String entityFile = String.format((pathInfo.get(FmGeneratorConst.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
//                    if (isCreate(FileType.ENTITY, entityFile)) {
//                        writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
//                    }
//                }
//                // MpMapper.java
//                if (null != tableInfo.getMapperName() && null != pathInfo.get(FmGeneratorConst.MAPPER_PATH)) {
//                    String mapperFile = String.format((pathInfo.get(FmGeneratorConst.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
//                    if (isCreate(FileType.MAPPER, mapperFile)) {
//                        writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            log.error("无法创建文件，请检查配置信息！", e);
//        }
//        return this;
//    }


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


    /**
     * 获取类名
     *
     * @param classPath ignore
     * @return ignore
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isEmpty(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore
     */
    public abstract String templateFilePath(String filePath);
}
