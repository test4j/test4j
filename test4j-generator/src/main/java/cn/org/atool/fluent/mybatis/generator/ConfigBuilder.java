//package org.test4j.generator.mybatis.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import org.test4j.generator.mybatis.model.BuildConfig;
//import org.test4j.generator.mybatis.model.FmGeneratorConst;
//import org.test4j.generator.mybatis.model.TableInfo;
//import org.test4j.generator.mybatis.rule.Naming;
//import org.test4j.generator.mybatis.template.AbstractTableTemplate;
//
//import java.io.File;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
///**
// * 配置汇总 传递给文件生成工具
// *
// * @author YangHu, tangguo, hubin
// * @since 2016-08-30
// */
//public class ConfigBuilder {
//
//
//    /**
//     * 数据库表信息
//     */
//    private List<TableInfo> tableInfoList;
//
//    /**
//     * 注入配置信息
//     */
//    private InjectionConfig injectionConfig;
//
//
//    // ************************ 曝露方法 BEGIN*****************************
//
////    /**
////     * 所有包配置信息
////     *
////     * @return 包配置
////     */
////    public Map<String, String> getPackageInfo() {
////        return packageInfo;
////    }
////
////
////    /**
////     * 所有路径配置
////     *
////     * @return 路径配置
////     */
////    public Map<String, String> getPathInfo() {
////        return pathInfo;
////    }
//
//    /**
//     * 表信息
//     *
//     * @return 所有表信息
//     */
//    public List<TableInfo> getTableInfoList() {
//        return tableInfoList;
//    }
//
//    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
//        this.tableInfoList = tableInfoList;
//        return this;
//    }
//
//    // ****************************** 曝露方法 END**********************************
//
//    /**
//     * 处理包配置
//     *
//     * @param template  TemplateConfig
//     * @param outputDir
//     * @param config    PackageConfig
//     */
////    private void handlerPackage(AbstractTableTemplate template, String outputDir, PackageConfig config) {
////        // 包信息
////        packageInfo = new HashMap<>(8);
////        packageInfo.put(FmGeneratorConst.MODULE_NAME, config.getModuleName());
////        packageInfo.put(FmGeneratorConst.ENTITY, joinPackage(config.getParent(), config.getEntity()));
////        packageInfo.put(FmGeneratorConst.MAPPER, joinPackage(config.getParent(), config.getMapper()));
////        packageInfo.put(FmGeneratorConst.XML, joinPackage(config.getParent(), config.getXml()));
////        packageInfo.put(FmGeneratorConst.SERVICE, joinPackage(config.getParent(), config.getService()));
////        packageInfo.put(FmGeneratorConst.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
////        packageInfo.put(FmGeneratorConst.CONTROLLER, joinPackage(config.getParent(), config.getController()));
////
////        // 自定义路径
////        Map<String, String> configPathInfo = config.getPathInfo();
////        if (null != configPathInfo) {
////            pathInfo = configPathInfo;
////        } else {
////            // 生成路径信息
////            pathInfo = new HashMap<>(6);
////            setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), outputDir, FmGeneratorConst.ENTITY_PATH, FmGeneratorConst.ENTITY);
////            setPathInfo(pathInfo, template.getMapper(), outputDir, FmGeneratorConst.MAPPER_PATH, FmGeneratorConst.MAPPER);
////            setPathInfo(pathInfo, template.getXml(), outputDir, FmGeneratorConst.XML_PATH, FmGeneratorConst.XML);
////            setPathInfo(pathInfo, template.getService(), outputDir, FmGeneratorConst.SERVICE_PATH, FmGeneratorConst.SERVICE);
////            setPathInfo(pathInfo, template.getServiceImpl(), outputDir, FmGeneratorConst.SERVICE_IMPL_PATH, FmGeneratorConst.SERVICE_IMPL);
////            setPathInfo(pathInfo, template.getController(), outputDir, FmGeneratorConst.CONTROLLER_PATH, FmGeneratorConst.CONTROLLER);
////        }
////    }
//
////    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
////        if (StringUtils.isNotEmpty(template)) {
////            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
////        }
////    }
//
//    /**
//     * 处理数据库表 加载数据库表、列、注释相关数据集
//     *
//     * @param config StrategyConfig
//     */
////    private void handlerStrategy(StrategyConfig config) {
////        processTypes(config);
////        tableInfoList = getTablesInfo(config);
////    }
//
//
//    /**
//     * 处理superClassName,IdClassType,IdStrategy配置
//     *
//     * @param config 策略配置
//     */
////    private void processTypes(StrategyConfig config) {
////        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
////            superServiceClass = FmGeneratorConst.SUPER_SERVICE_CLASS;
////        } else {
////            superServiceClass = config.getSuperServiceClass();
////        }
////        if (StringUtils.isEmpty(config.getSuperServiceImplClass())) {
////            superServiceImplClass = FmGeneratorConst.SUPER_SERVICE_IMPL_CLASS;
////        } else {
////            superServiceImplClass = config.getSuperServiceImplClass();
////        }
////        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
////            superMapperClass = FmGeneratorConst.SUPER_MAPPER_CLASS;
////        } else {
////            superMapperClass = config.getSuperMapperClass();
////        }
////        superEntityClass = config.getSuperEntityClass();
////        superControllerClass = config.getSuperControllerClass();
////    }
//
//
//
//    /**
//     * 检测导入包
//     *
//     * @param tableInfo ignore
//     */
////    private void checkImportPackages(TableInfo tableInfo) {
////        if (StringUtils.isNotEmpty(strategyConfig.getSuperEntityClass())) {
////            // 自定义父类
////            tableInfo.getImportTypes().add(strategyConfig.getSuperEntityClass());
////        } else if (globalConfig.isActiveRecord()) {
////            // 无父类开启 AR 模式
////            tableInfo.getImportTypes().add(com.baomidou.mybatisplus.extension.activerecord.Model.class.getCanonicalName());
////        }
////        if (null != globalConfig.getIdType()) {
////            // 指定需要 IdType 场景
////            tableInfo.getImportTypes().add(com.baomidou.mybatisplus.annotation.IdType.class.getCanonicalName());
////            tableInfo.getImportTypes().add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
////        }
////        if (StringUtils.isNotEmpty(strategyConfig.getVersionFieldName())) {
////            tableInfo.getFields().forEach(f -> {
////                if (strategyConfig.getVersionFieldName().equals(f.getColumnName())) {
////                    tableInfo.getImportTypes().add(com.baomidou.mybatisplus.annotation.Version.class.getCanonicalName());
////                }
////            });
////        }
////    }
//
//
////    /**
////     * 获取所有的数据库表信息
////     */
////    private List<TableInfo> getTablesInfo(BuildConfig config) {
////        boolean isExclude = (null != config.getExclude() && config.getExclude().size() > 0);
////
////        //所有的表信息
////        List<TableInfo> tableList = new ArrayList<>();
////
////        //需要反向生成或排除的表信息
////        List<TableInfo> includeTableList = new ArrayList<>();
////        List<TableInfo> excludeTableList = new ArrayList<>();
////
////        //不存在的表名
////        Set<String> notExistTables = new HashSet<>();
////        try {
////            String tablesSql = dataSourceConfig.getDbQuery().tablesSql();
////            if (DbType.POSTGRE_SQL == dataSourceConfig.getDbQuery().dbType()) {
////                String schema = dataSourceConfig.getSchemaName();
////                if (schema == null) {
////                    //pg 默认 schema=public
////                    schema = "public";
////                    dataSourceConfig.setSchemaName(schema);
////                }
////                tablesSql = String.format(tablesSql, schema);
////            }
////            if (DbType.DB2 == dataSourceConfig.getDbQuery().dbType()) {
////                String schema = dataSourceConfig.getSchemaName();
////                if (schema == null) {
////                    //db2 默认 schema=current schema
////                    schema = "current schema";
////                    dataSourceConfig.setSchemaName(schema);
////                }
////                tablesSql = String.format(tablesSql, schema);
////            }
////            //oracle数据库表太多，出现最大游标错误
////            else if (DbType.ORACLE == dataSourceConfig.getDbQuery().dbType()) {
////                String schema = dataSourceConfig.getSchemaName();
////                //oracle 默认 schema=username
////                if (schema == null) {
////                    schema = dataSourceConfig.getUsername().toUpperCase();
////                    dataSourceConfig.setSchemaName(schema);
////                }
////                tablesSql = String.format(tablesSql, schema);
////                if (isInclude) {
////                    StringBuilder sb = new StringBuilder(tablesSql);
////                    sb.append(" AND ").append(dataSourceConfig.getDbQuery().tableName()).append(" IN (");
////                    Arrays.stream(config.getInclude()).forEach(tbname -> sb.append(StringPool.SINGLE_QUOTE).append(tbname.toUpperCase()).append("',"));
////                    sb.replace(sb.length() - 1, sb.length(), StringPool.RIGHT_BRACKET);
////                    tablesSql = sb.toString();
////                } else if (isExclude) {
////                    StringBuilder sb = new StringBuilder(tablesSql);
////                    sb.append(" AND ").append(dataSourceConfig.getDbQuery().tableName()).append(" NOT IN (");
////                    Arrays.stream(config.getExclude()).forEach(tbname -> sb.append(StringPool.SINGLE_QUOTE).append(tbname.toUpperCase()).append("',"));
////                    sb.replace(sb.length() - 1, sb.length(), StringPool.RIGHT_BRACKET);
////                    tablesSql = sb.toString();
////                }
////            }
////            TableInfo tableInfo;
////            try (PreparedStatement preparedStatement = dataSourceConfig.getConn().prepareStatement(tablesSql);
////                 ResultSet results = preparedStatement.executeQuery()) {
////                while (results.next()) {
////                    String tableName = results.getString(dataSourceConfig.getDbQuery().tableName());
////                    if (StringUtils.isNotEmpty(tableName)) {
////                        tableInfo = new TableInfo(tableName);
////
////                        if (commentSupported) {
////                            String tableComment = results.getString(dataSourceConfig.getDbQuery().tableComment());
////                            if (config.isSkipView() && "VIEW".equals(tableComment)) {
////                                // 跳过视图
////                                continue;
////                            }
////                            tableInfo.setComment(tableComment);
////                        }
////
////                        if (isInclude) {
////                            for (String includeTable : config.getInclude()) {
////                                // 忽略大小写等于 或 正则 true
////                                if (tableNameMatches(includeTable, tableName)) {
////                                    includeTableList.add(tableInfo);
////                                } else {
////                                    notExistTables.add(includeTable);
////                                }
////                            }
////                        } else if (isExclude) {
////                            for (String excludeTable : config.getExclude()) {
////                                // 忽略大小写等于 或 正则 true
////                                if (tableNameMatches(excludeTable, tableName)) {
////                                    excludeTableList.add(tableInfo);
////                                } else {
////                                    notExistTables.add(excludeTable);
////                                }
////                            }
////                        }
////                        tableList.add(tableInfo);
////                    } else {
////                        System.err.println("当前数据库为空！！！");
////                    }
////                }
////            }
////            // 将已经存在的表移除，获取配置中数据库不存在的表
////            for (TableInfo tabInfo : tableList) {
////                notExistTables.remove(tabInfo.getTableName());
////            }
////            if (notExistTables.size() > 0) {
////                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
////            }
////
////            // 需要反向生成的表信息
////            if (isExclude) {
////                tableList.removeAll(excludeTableList);
////                includeTableList = tableList;
////            }
////            if (!isInclude && !isExclude) {
////                includeTableList = tableList;
////            }
////            // 性能优化，只处理需执行表字段 github issues/219
////            includeTableList.forEach(ti -> ti.initTableFields(config));
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        return processTable(includeTableList, config.getTableNaming(), config);
////    }
//
//
////    /**
////     * 表名匹配
////     *
////     * @param setTableName 设置表名
////     * @param dbTableName  数据库表单
////     * @return ignore
////     */
////    private boolean tableNameMatches(String setTableName, String dbTableName) {
////        return setTableName.equalsIgnoreCase(dbTableName)
////            || StringUtils.matches(setTableName, dbTableName);
////    }
//
//    /**
//     * 连接路径字符串
//     *
//     * @param parentDir   路径常量字符串
//     * @param packageName 包名
//     * @return 连接后的路径
//     */
//    private String joinPath(String parentDir, String packageName) {
//        if (StringUtils.isEmpty(parentDir)) {
//            parentDir = System.getProperty(FmGeneratorConst.JAVA_TMPDIR);
//        }
//        if (!StringUtils.endsWith(parentDir, File.separator)) {
//            parentDir += File.separator;
//        }
//        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
//        return parentDir + packageName;
//    }
//
//
//    /**
//     * 连接父子包名
//     *
//     * @param parent     父包名
//     * @param subPackage 子包名
//     * @return 连接后的包名
//     */
//    private String joinPackage(String parent, String subPackage) {
//        if (StringUtils.isEmpty(parent)) {
//            return subPackage;
//        }
//        return parent + StringPool.DOT + subPackage;
//    }
//}
//package org.test4j.generator.mybatis.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import org.test4j.generator.mybatis.model.BuildConfig;
//import org.test4j.generator.mybatis.model.FmGeneratorConst;
//import org.test4j.generator.mybatis.model.TableInfo;
//import org.test4j.generator.mybatis.rule.Naming;
//import org.test4j.generator.mybatis.template.AbstractTableTemplate;
//
//import java.io.File;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
///**
// * 配置汇总 传递给文件生成工具
// *
// * @author YangHu, tangguo, hubin
// * @since 2016-08-30
// */
//public class ConfigBuilder {
//
//
//    /**
//     * 数据库表信息
//     */
//    private List<TableInfo> tableInfoList;
//
//    /**
//     * 注入配置信息
//     */
//    private InjectionConfig injectionConfig;
//
//
//    // ************************ 曝露方法 BEGIN*****************************
//
////    /**
////     * 所有包配置信息
////     *
////     * @return 包配置
////     */
////    public Map<String, String> getPackageInfo() {
////        return packageInfo;
////    }
////
////
////    /**
////     * 所有路径配置
////     *
////     * @return 路径配置
////     */
////    public Map<String, String> getPathInfo() {
////        return pathInfo;
////    }
//
//    /**
//     * 表信息
//     *
//     * @return 所有表信息
//     */
//    public List<TableInfo> getTableInfoList() {
//        return tableInfoList;
//    }
//
//    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
//        this.tableInfoList = tableInfoList;
//        return this;
//    }
//
//    // ****************************** 曝露方法 END**********************************
//
//    /**
//     * 处理包配置
//     *
//     * @param template  TemplateConfig
//     * @param outputDir
//     * @param config    PackageConfig
//     */
////    private void handlerPackage(AbstractTableTemplate template, String outputDir, PackageConfig config) {
////        // 包信息
////        packageInfo = new HashMap<>(8);
////        packageInfo.put(FmGeneratorConst.MODULE_NAME, config.getModuleName());
////        packageInfo.put(FmGeneratorConst.ENTITY, joinPackage(config.getParent(), config.getEntity()));
////        packageInfo.put(FmGeneratorConst.MAPPER, joinPackage(config.getParent(), config.getMapper()));
////        packageInfo.put(FmGeneratorConst.XML, joinPackage(config.getParent(), config.getXml()));
////        packageInfo.put(FmGeneratorConst.SERVICE, joinPackage(config.getParent(), config.getService()));
////        packageInfo.put(FmGeneratorConst.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
////        packageInfo.put(FmGeneratorConst.CONTROLLER, joinPackage(config.getParent(), config.getController()));
////
////        // 自定义路径
////        Map<String, String> configPathInfo = config.getPathInfo();
////        if (null != configPathInfo) {
////            pathInfo = configPathInfo;
////        } else {
////            // 生成路径信息
////            pathInfo = new HashMap<>(6);
////            setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), outputDir, FmGeneratorConst.ENTITY_PATH, FmGeneratorConst.ENTITY);
////            setPathInfo(pathInfo, template.getMapper(), outputDir, FmGeneratorConst.MAPPER_PATH, FmGeneratorConst.MAPPER);
////            setPathInfo(pathInfo, template.getXml(), outputDir, FmGeneratorConst.XML_PATH, FmGeneratorConst.XML);
////            setPathInfo(pathInfo, template.getService(), outputDir, FmGeneratorConst.SERVICE_PATH, FmGeneratorConst.SERVICE);
////            setPathInfo(pathInfo, template.getServiceImpl(), outputDir, FmGeneratorConst.SERVICE_IMPL_PATH, FmGeneratorConst.SERVICE_IMPL);
////            setPathInfo(pathInfo, template.getController(), outputDir, FmGeneratorConst.CONTROLLER_PATH, FmGeneratorConst.CONTROLLER);
////        }
////    }
//
////    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
////        if (StringUtils.isNotEmpty(template)) {
////            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
////        }
////    }
//
//    /**
//     * 处理数据库表 加载数据库表、列、注释相关数据集
//     *
//     * @param config StrategyConfig
//     */
////    private void handlerStrategy(StrategyConfig config) {
////        processTypes(config);
////        tableInfoList = getTablesInfo(config);
////    }
//
//
//    /**
//     * 处理superClassName,IdClassType,IdStrategy配置
//     *
//     * @param config 策略配置
//     */
////    private void processTypes(StrategyConfig config) {
////        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
////            superServiceClass = FmGeneratorConst.SUPER_SERVICE_CLASS;
////        } else {
////            superServiceClass = config.getSuperServiceClass();
////        }
////        if (StringUtils.isEmpty(config.getSuperServiceImplClass())) {
////            superServiceImplClass = FmGeneratorConst.SUPER_SERVICE_IMPL_CLASS;
////        } else {
////            superServiceImplClass = config.getSuperServiceImplClass();
////        }
////        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
////            superMapperClass = FmGeneratorConst.SUPER_MAPPER_CLASS;
////        } else {
////            superMapperClass = config.getSuperMapperClass();
////        }
////        superEntityClass = config.getSuperEntityClass();
////        superControllerClass = config.getSuperControllerClass();
////    }
//
//
//
//    /**
//     * 检测导入包
//     *
//     * @param tableInfo ignore
//     */
////    private void checkImportPackages(TableInfo tableInfo) {
////        if (StringUtils.isNotEmpty(strategyConfig.getSuperEntityClass())) {
////            // 自定义父类
////            tableInfo.getImportTypes().add(strategyConfig.getSuperEntityClass());
////        } else if (globalConfig.isActiveRecord()) {
////            // 无父类开启 AR 模式
////            tableInfo.getImportTypes().add(com.baomidou.mybatisplus.extension.activerecord.Model.class.getCanonicalName());
////        }
////        if (null != globalConfig.getIdType()) {
////            // 指定需要 IdType 场景
////            tableInfo.getImportTypes().add(com.baomidou.mybatisplus.annotation.IdType.class.getCanonicalName());
////            tableInfo.getImportTypes().add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
////        }
////        if (StringUtils.isNotEmpty(strategyConfig.getVersionFieldName())) {
////            tableInfo.getFields().forEach(f -> {
////                if (strategyConfig.getVersionFieldName().equals(f.getColumnName())) {
////                    tableInfo.getImportTypes().add(com.baomidou.mybatisplus.annotation.Version.class.getCanonicalName());
////                }
////            });
////        }
////    }
//
//
////    /**
////     * 获取所有的数据库表信息
////     */
////    private List<TableInfo> getTablesInfo(BuildConfig config) {
////        boolean isExclude = (null != config.getExclude() && config.getExclude().size() > 0);
////
////        //所有的表信息
////        List<TableInfo> tableList = new ArrayList<>();
////
////        //需要反向生成或排除的表信息
////        List<TableInfo> includeTableList = new ArrayList<>();
////        List<TableInfo> excludeTableList = new ArrayList<>();
////
////        //不存在的表名
////        Set<String> notExistTables = new HashSet<>();
////        try {
////            String tablesSql = dataSourceConfig.getDbQuery().tablesSql();
////            if (DbType.POSTGRE_SQL == dataSourceConfig.getDbQuery().dbType()) {
////                String schema = dataSourceConfig.getSchemaName();
////                if (schema == null) {
////                    //pg 默认 schema=public
////                    schema = "public";
////                    dataSourceConfig.setSchemaName(schema);
////                }
////                tablesSql = String.format(tablesSql, schema);
////            }
////            if (DbType.DB2 == dataSourceConfig.getDbQuery().dbType()) {
////                String schema = dataSourceConfig.getSchemaName();
////                if (schema == null) {
////                    //db2 默认 schema=current schema
////                    schema = "current schema";
////                    dataSourceConfig.setSchemaName(schema);
////                }
////                tablesSql = String.format(tablesSql, schema);
////            }
////            //oracle数据库表太多，出现最大游标错误
////            else if (DbType.ORACLE == dataSourceConfig.getDbQuery().dbType()) {
////                String schema = dataSourceConfig.getSchemaName();
////                //oracle 默认 schema=username
////                if (schema == null) {
////                    schema = dataSourceConfig.getUsername().toUpperCase();
////                    dataSourceConfig.setSchemaName(schema);
////                }
////                tablesSql = String.format(tablesSql, schema);
////                if (isInclude) {
////                    StringBuilder sb = new StringBuilder(tablesSql);
////                    sb.append(" AND ").append(dataSourceConfig.getDbQuery().tableName()).append(" IN (");
////                    Arrays.stream(config.getInclude()).forEach(tbname -> sb.append(StringPool.SINGLE_QUOTE).append(tbname.toUpperCase()).append("',"));
////                    sb.replace(sb.length() - 1, sb.length(), StringPool.RIGHT_BRACKET);
////                    tablesSql = sb.toString();
////                } else if (isExclude) {
////                    StringBuilder sb = new StringBuilder(tablesSql);
////                    sb.append(" AND ").append(dataSourceConfig.getDbQuery().tableName()).append(" NOT IN (");
////                    Arrays.stream(config.getExclude()).forEach(tbname -> sb.append(StringPool.SINGLE_QUOTE).append(tbname.toUpperCase()).append("',"));
////                    sb.replace(sb.length() - 1, sb.length(), StringPool.RIGHT_BRACKET);
////                    tablesSql = sb.toString();
////                }
////            }
////            TableInfo tableInfo;
////            try (PreparedStatement preparedStatement = dataSourceConfig.getConn().prepareStatement(tablesSql);
////                 ResultSet results = preparedStatement.executeQuery()) {
////                while (results.next()) {
////                    String tableName = results.getString(dataSourceConfig.getDbQuery().tableName());
////                    if (StringUtils.isNotEmpty(tableName)) {
////                        tableInfo = new TableInfo(tableName);
////
////                        if (commentSupported) {
////                            String tableComment = results.getString(dataSourceConfig.getDbQuery().tableComment());
////                            if (config.isSkipView() && "VIEW".equals(tableComment)) {
////                                // 跳过视图
////                                continue;
////                            }
////                            tableInfo.setComment(tableComment);
////                        }
////
////                        if (isInclude) {
////                            for (String includeTable : config.getInclude()) {
////                                // 忽略大小写等于 或 正则 true
////                                if (tableNameMatches(includeTable, tableName)) {
////                                    includeTableList.add(tableInfo);
////                                } else {
////                                    notExistTables.add(includeTable);
////                                }
////                            }
////                        } else if (isExclude) {
////                            for (String excludeTable : config.getExclude()) {
////                                // 忽略大小写等于 或 正则 true
////                                if (tableNameMatches(excludeTable, tableName)) {
////                                    excludeTableList.add(tableInfo);
////                                } else {
////                                    notExistTables.add(excludeTable);
////                                }
////                            }
////                        }
////                        tableList.add(tableInfo);
////                    } else {
////                        System.err.println("当前数据库为空！！！");
////                    }
////                }
////            }
////            // 将已经存在的表移除，获取配置中数据库不存在的表
////            for (TableInfo tabInfo : tableList) {
////                notExistTables.remove(tabInfo.getTableName());
////            }
////            if (notExistTables.size() > 0) {
////                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
////            }
////
////            // 需要反向生成的表信息
////            if (isExclude) {
////                tableList.removeAll(excludeTableList);
////                includeTableList = tableList;
////            }
////            if (!isInclude && !isExclude) {
////                includeTableList = tableList;
////            }
////            // 性能优化，只处理需执行表字段 github issues/219
////            includeTableList.forEach(ti -> ti.initTableFields(config));
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        return processTable(includeTableList, config.getTableNaming(), config);
////    }
//
//
////    /**
////     * 表名匹配
////     *
////     * @param setTableName 设置表名
////     * @param dbTableName  数据库表单
////     * @return ignore
////     */
////    private boolean tableNameMatches(String setTableName, String dbTableName) {
////        return setTableName.equalsIgnoreCase(dbTableName)
////            || StringUtils.matches(setTableName, dbTableName);
////    }
//
//    /**
//     * 连接路径字符串
//     *
//     * @param parentDir   路径常量字符串
//     * @param packageName 包名
//     * @return 连接后的路径
//     */
//    private String joinPath(String parentDir, String packageName) {
//        if (StringUtils.isEmpty(parentDir)) {
//            parentDir = System.getProperty(FmGeneratorConst.JAVA_TMPDIR);
//        }
//        if (!StringUtils.endsWith(parentDir, File.separator)) {
//            parentDir += File.separator;
//        }
//        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
//        return parentDir + packageName;
//    }
//
//
//    /**
//     * 连接父子包名
//     *
//     * @param parent     父包名
//     * @param subPackage 子包名
//     * @return 连接后的包名
//     */
//    private String joinPackage(String parent, String subPackage) {
//        if (StringUtils.isEmpty(parent)) {
//            return subPackage;
//        }
//        return parent + StringPool.DOT + subPackage;
//    }
//}
