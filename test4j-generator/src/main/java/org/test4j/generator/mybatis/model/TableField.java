package org.test4j.generator.mybatis.model;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AccessLevel;
import org.test4j.generator.mybatis.config.IDbQuery;
import org.test4j.generator.mybatis.config.INameConvert;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.config.StrategyConfig;
import org.test4j.generator.mybatis.rule.IColumnType;
import org.test4j.generator.mybatis.rule.Naming;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.tools.commons.StringHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表字段信息
 *
 * @author YangHu
 * @since 2016-12-03
 */
@Getter
@Accessors(chain = true)
public class TableField {
    /**
     * 是否主键
     */
    @Setter
    private boolean primary;
    /**
     * 主键是否为自增类型
     */
    @Setter
    private boolean autoIncrement;
    /**
     * 数据库字段名称
     */
    private String columnName;
    /**
     * 数据库字段类型
     */
    @Setter(AccessLevel.NONE)
    private IColumnType columnType;
    /**
     * java字段名称
     */
    @Setter
    private String fieldName;
    /**
     * java字段类型
     */
    @Setter
    private String fieldType;

    @Setter
    private String comment;

    public TableField(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getFieldType();
        }
        return null;
    }

    /**
     * 按JavaBean规则来生成get和set方法
     */
    public String getCapitalName() {
        if (fieldName.length() <= 1) {
            return fieldName.toUpperCase();
        }
        // 第一个字母小写， 第二个字母大写，特殊处理
        String firstChar = fieldName.substring(0, 1);
        if (Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(fieldName.substring(1, 2).toCharArray()[0])) {
            return firstChar.toLowerCase() + fieldName.substring(1);
        } else {
            return firstChar.toUpperCase() + fieldName.substring(1);
        }
    }

    public void initNaming(TableInfo table, ResultSet results) throws SQLException {
        this.setConfig(table.getGenerator(), table.getBuildConfig(), tableInfo);
        this.initFieldName();
        ITypeConvert typeConvert = this.generator.getDataSourceConfig().getTypeConvert();
        this.columnType = typeConvert.processTypeConvert(this.buildConfig.getDateType(), this);
        if (this.generator.isCommentSupported()) {
            IDbQuery dbQuery = this.generator.getDataSourceConfig().getDbQuery();
            this.comment = results.getString(dbQuery.fieldComment());
        }
    }

    /**
     * 是否需要去掉is前缀
     *
     * @param strategyConfig
     * @return
     */
    private boolean needRemoveIsPrefix(StrategyConfig strategyConfig) {
        if (!strategyConfig.isEntityBooleanColumnRemoveIsPrefix()) {
            return false;
        } else if (!"boolean".equalsIgnoreCase(this.fieldType)) {
            return false;
        } else {
            return this.fieldName.startsWith("is");
        }
    }

    /**
     * 处理字段名称
     */
    private void initFieldName() {
        if (!StringHelper.isBlank(this.fieldName)) {
            return;
        }
        StrategyConfig strategyConfig = this.generator.getStrategyConfig();
        INameConvert nameConvert = strategyConfig.getNameConvert();
        if (null != nameConvert) {
            this.fieldName = nameConvert.propertyNameConvert(this);
        } else {
            Naming naming = strategyConfig.getColumnNaming();
            if (naming == Naming.underline_to_camel) {
                this.fieldName = Naming.underlineToCamel(this.columnName);
            } else {
                this.fieldName = this.columnName;
            }
        }
        // Boolean类型is前缀处理
        if (this.needRemoveIsPrefix(strategyConfig)) {
            this.fieldName = this.fieldName.substring(2, 1).toLowerCase() + this.fieldName.substring(3);
        }
    }

    @Getter(AccessLevel.NONE)
    private BuildConfig buildConfig;

    @Getter(AccessLevel.NONE)
    private Generator generator;

    @Getter(AccessLevel.NONE)
    private TableInfo tableInfo;

    public TableField setConfig(Generator generator, BuildConfig buildConfig, TableInfo tableInfo) {
        this.generator = generator;
        this.buildConfig = buildConfig;
        this.tableInfo = tableInfo;
        return this;
    }
}
