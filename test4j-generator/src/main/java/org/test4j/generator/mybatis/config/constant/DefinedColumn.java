package org.test4j.generator.mybatis.config.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.db.ColumnJavaType;

/**
 * 预定义好的字段
 *
 * @author wudarui
 */
@Getter
@Setter
@Accessors(chain = true)
public class DefinedColumn {
    private String columnName;

    private String fieldName;

    private ColumnJavaType javaType;
    /**
     * typeHandler
     */
    private String typeHandler;
    /**
     * 默认不是大字段
     */
    private boolean notLarge = true;
    /**
     * 不生成映射字段
     */
    private boolean exclude = false;
    /**
     * update默认值
     */
    private String update;
    /**
     * insert默认值
     */
    private String insert;

    public DefinedColumn(String columnName, String fieldName, ColumnJavaType javaType) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.javaType = javaType;
    }

    public DefinedColumn(String columnName) {
        this.columnName = columnName;
    }
}