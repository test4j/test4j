package org.test4j.generator.mybatis.config.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.config.constant.Naming;
import org.test4j.generator.mybatis.db.ColumnJavaType;
import org.test4j.generator.mybatis.db.IDbQuery;
import org.test4j.generator.mybatis.db.IFieldCategory;
import org.test4j.generator.mybatis.db.ITypeConvert;
import org.test4j.tools.commons.StringHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表字段信息
 *
 * @author darui.wu
 */
@Getter
@Accessors(chain = true)
public class TableField implements Comparable<TableField> {
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
     * java字段名称
     */
    @Setter
    private String name;
    /**
     * 字段名称（首字母大写）
     */
    private String capitalName;
    /**
     * 数据库字段类型
     */
    @Setter
    private String columnType;
    /**
     * 字段java类型
     */
    @Setter
    private ColumnJavaType javaType;
    /**
     * 字段注释
     */
    private String comment;

    @Getter(AccessLevel.NONE)
    private final TableSetter tableInfo;

    public TableField(TableSetter tableInfo, String columnName) {
        this.tableInfo = tableInfo;
        this.columnName = columnName;
    }

    public String getType() {
        return javaType.getFieldType();
    }

    public void initNaming(ResultSet results) throws SQLException {
        this.initFieldJavaType(results);
        this.initFieldName();
        if (this.tableInfo.getGlobalConfig().getDbConfig().getDbType().isCommentSupported()) {
            IDbQuery dbQuery = this.tableInfo.getGlobalConfig().getDbConfig().getDbQuery();
            this.comment = results.getString(dbQuery.fieldComment());
        }
    }

    private void initFieldJavaType(ResultSet results) throws SQLException {
        if (this.javaType == null) {
            ITypeConvert typeConvert = this.tableInfo.getGlobalConfig().getDbConfig().getTypeConvert();
            this.javaType = typeConvert.processTypeConvert(this.tableInfo.getDateType(), this);
        }
    }


    /**
     * 处理字段名称
     */
    private void initFieldName() {
        GlobalConfig globalConfig = this.tableInfo.getGlobalConfig();
        // 如果字段名称没有预设
        if (StringHelper.isBlank(this.name)) {
            Naming naming = globalConfig.getColumnNaming();
            if (naming == Naming.underline_to_camel) {
                this.name = Naming.underlineToCamel(this.columnName);
            } else {
                this.name = this.columnName;
            }
        }

        String capitalName = this.removeIsIfNeed(this.name, globalConfig);
        this.capitalName = capitalName.substring(0, 1).toUpperCase() + capitalName.substring(1);
    }

    /**
     * Boolean类型is前缀处理
     * @param input
     * @param globalConfig
     * @return
     */
    private String removeIsIfNeed(String input, GlobalConfig globalConfig) {
        if (globalConfig.needRemoveIsPrefix(input, this.getType())) {
            return input.substring(2);
        } else {
            return input;
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

    /**
     * 是否自增主键
     *
     * @return
     */
    public boolean isPrimaryId() {
        return this.category == IFieldCategory.PrimaryId;
    }

    /**
     * 是否gmt字段
     *
     * @return
     */
    public boolean isGmt() {
        return this.category == IFieldCategory.GmtCreate || this.category == IFieldCategory.GmtModified;
    }

    /**
     * 是否逻辑删除字段
     *
     * @return
     */
    public boolean isDeleted() {
        return this.category == IFieldCategory.IsDeleted;
    }

    @Override
    public int compareTo(TableField field) {
        if (field == null) {
            return 1;
        }
        int order = this.category.compareTo(field.category);
        if (order == 0) {
            order = this.name.compareTo(field.name);
        }
        return order;
    }
}