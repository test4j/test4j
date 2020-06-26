package org.test4j.generator.mybatis.config.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.config.IGlobalConfigSet;
import org.test4j.generator.mybatis.config.constant.Naming;
import org.test4j.generator.mybatis.db.DbType;
import org.test4j.generator.mybatis.db.ITypeConvert;
import org.test4j.tools.commons.StringHelper;

/**
 * 策略配置项
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class GlobalConfig implements IGlobalConfigSet {
    /**
     * 数据库表映射到实体的命名策略
     */
    private Naming tableNaming = Naming.underline_to_camel;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    @Getter(AccessLevel.NONE)
    private Naming columnNaming;
    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean removeIsPrefix = false;
    /**
     * 代码package前缀
     */
    @Getter(AccessLevel.NONE)
    private String basePackage;

    @Setter(AccessLevel.NONE)
    private String packageDir;
    /**
     * 代码生成路径
     */
    @Setter(AccessLevel.NONE)
    private String outputDir = System.getProperty("user.dir") + "/target/generate/base";
    /**
     * 测试代码生成路径
     */
    @Setter(AccessLevel.NONE)
    private String testOutputDir = System.getProperty("user.dir") + "/target/generate/test";
    /**
     * dao代码生成路径
     */
    @Setter(AccessLevel.NONE)
    private String daoOutputDir = System.getProperty("user.dir") + "/target/generate/dao";
    /**
     * 开发人员
     */
    private String author = "generate code";
    /**
     * 是否打开输出目录
     */
    private boolean open = true;

    public Naming getColumnNaming() {
        return columnNaming == null ? tableNaming : columnNaming;
    }

    @Override
    public IGlobalConfigSet setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        this.packageDir = '/' + basePackage.replace('.', '/') + '/';
        return this;
    }

    public String getBasePackage() {
        if (StringHelper.isBlank(basePackage)) {
            throw new RuntimeException("the base package should be set.");
        }
        return basePackage;
    }

    @Override
    public IGlobalConfigSet setOutputDir(String outputDir) {
        return this.setOutputDir(outputDir, outputDir, outputDir);
    }

    @Override
    public IGlobalConfigSet setOutputDir(String outputDir, String testOutputDir, String daoOutputDir) {
        this.outputDir = outputDir;
        this.testOutputDir = testOutputDir;
        this.daoOutputDir = daoOutputDir;
        return this;
    }

    /**
     * 数据源配置
     */
    private DbConfig dbConfig;

    @Override
    public IGlobalConfigSet setDataSource(String url, String username, String password) {
        return this.setDataSource(DbType.MYSQL, "com.mysql.jdbc.Driver", url, username, password, null);
    }

    @Override
    public IGlobalConfigSet setDataSource(DbType dbType, String driver, String url, String username, String password) {
        return this.setDataSource(dbType, driver, url, username, password, null);
    }

    @Override
    public IGlobalConfigSet setDataSource(DbType dbType, String driver, String url, String username, String password, ITypeConvert typeConvert) {
        this.dbConfig = new DbConfig(dbType, driver, url, username, password)
            .setTypeConvert(typeConvert);
        return this;
    }

    public DbType getDbType() {
        return this.dbConfig.getDbType();
    }

    /**
     * 是否需要去掉is前缀
     *
     * @param fieldName
     * @param fieldType
     * @return
     */
    public boolean needRemoveIsPrefix(String fieldName, String fieldType) {
        if (!this.isRemoveIsPrefix()) {
            return false;
        } else if (!boolean.class.getSimpleName().equalsIgnoreCase(fieldType)) {
            return false;
        } else {
            return fieldName.startsWith("is");
        }
    }
}