package org.test4j.generator.mybatis.model;

import lombok.AccessLevel;
import org.test4j.generator.mybatis.config.IDbQuery;
import org.test4j.generator.mybatis.config.INameConvert;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.config.StrategyConfig;
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
     * 字段类别
     */
    @Setter
    private IFieldCategory category = IFieldCategory.Common;
    /**
     * 数据库字段名称
     */
    private final String columnName;
    /**
     * 数据库字段类型
     */
    @Setter
    private String columnType;
    /**
     * java字段名称
     */
    private String name;
    /**
     * 字段java类型
     */
    private IJavaType javaType;
    /**
     * 字段类型
     */
    private String capitalName;
    /**
     * 字段注释
     */
    private String comment;

    @Getter(AccessLevel.NONE)
    private final TableInfo tableInfo;

    public TableField(TableInfo tableInfo, String columnName) {
        this.tableInfo = tableInfo;
        this.columnName = columnName;
    }

    public String getType() {
        return javaType.getFieldType();
    }

    public void initNaming(ResultSet results) throws SQLException {
        this.initFieldName();
        ITypeConvert typeConvert = this.tableInfo.getGenerator().getDataSourceConfig().getTypeConvert();
        this.javaType = typeConvert.processTypeConvert(this.tableInfo.getBuildConfig().getDateType(), this);
        if (this.tableInfo.getGenerator().isCommentSupported()) {
            IDbQuery dbQuery = this.tableInfo.getGenerator().getDataSourceConfig().getDbQuery();
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
        } else if (!boolean.class.getSimpleName().equalsIgnoreCase(this.getType())) {
            return false;
        } else {
            return this.name.startsWith("is");
        }
    }

    /**
     * 处理字段名称
     */
    private void initFieldName() {
        if (!StringHelper.isBlank(this.name)) {
            return;
        }
        StrategyConfig strategyConfig = this.tableInfo.getGenerator().getStrategyConfig();
        INameConvert nameConvert = strategyConfig.getNameConvert();
        if (null != nameConvert) {
            this.name = nameConvert.propertyNameConvert(this);
        } else {
            Naming naming = strategyConfig.getColumnNaming();
            if (naming == Naming.underline_to_camel) {
                this.name = Naming.underlineToCamel(this.columnName);
            } else {
                this.name = this.columnName;
            }
        }
        // Boolean类型is前缀处理
        if (this.needRemoveIsPrefix(strategyConfig)) {
            this.capitalName = this.name.substring(2, 1).toLowerCase() + this.name.substring(3);
        } else if (name.length() <= 1) {
            this.capitalName = name.toUpperCase();
        } else {
            // 第一个字母小写， 第二个字母大写，特殊处理
            String firstChar = name.substring(0, 1);
            if (Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(name.substring(1, 2).toCharArray()[0])) {
                this.capitalName = firstChar.toLowerCase() + name.substring(1);
            } else {
                this.capitalName = firstChar.toUpperCase() + name.substring(1);
            }
        }
    }

    /**
     * 是否主键
     *
     * @return
     */
    public boolean isPrimary() {
        return this.category == IFieldCategory.PrimaryKey || this.category == IFieldCategory.PrimaryId;
    }
}
