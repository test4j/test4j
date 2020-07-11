package org.test4j.generator.mybatis.config.constant;

import lombok.AccessLevel;
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
    @Setter(AccessLevel.NONE)
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
    @Setter(AccessLevel.NONE)
    private boolean notLarge = true;
    /**
     * 不生成映射字段
     */
    @Setter(AccessLevel.NONE)
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

    /**
     * 设置为大字段
     *
     * @return
     */
    public DefinedColumn setLarge() {
        this.notLarge = false;
        return this;
    }

    /**
     * 设置为排除字段
     *
     * @return
     */
    public DefinedColumn setExclude() {
        this.exclude = true;
        return this;
    }
}