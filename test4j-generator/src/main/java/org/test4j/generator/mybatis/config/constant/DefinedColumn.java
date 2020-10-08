package org.test4j.generator.mybatis.config.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.test4j.generator.mybatis.config.impl.TableField;

import java.util.Objects;

import static org.test4j.tools.commons.StringHelper.isNotBlank;

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

    private Class javaType;
    /**
     * typeHandler
     */
    private Class<? extends TypeHandler> typeHandler;
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

    public DefinedColumn(String columnName, String fieldName, Class javaType) {
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

    /**
     * 根据预设的字段设置初始化映射关系
     *
     * @param field 字段映射
     */
    public void initField(TableField field) {
        if (isNotBlank(this.fieldName)) {
            field.setName(this.fieldName);
        }
        if (this.javaType != null) {
            field.setJavaType(this.javaType);
        }
        if (this.typeHandler != null && !Objects.equals(UnknownTypeHandler.class, this.typeHandler)) {
            field.setTypeHandler(this.typeHandler);
        }
        if (this.notLarge == false) {
            field.setIsLarge(false);
        }
        if (isNotBlank(this.insert)) {
            field.setInsert(this.insert);
        }
        if (isNotBlank(this.update)) {
            field.setUpdate(this.update);
        }
    }
}