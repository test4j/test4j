package org.test4j.generator.mybatis.config.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.db.IJavaType;

/**
 * 预定义好的字段
 *
 * @author wudarui
 */
@Getter
@Accessors(chain = true)
public class DefinedColumn {
    private String columnName;

    private String fieldName;

    private IJavaType javaType;

    @Setter
    private boolean exclude = false;

    public DefinedColumn(String columnName, String fieldName, IJavaType javaType) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.javaType = javaType;
    }

    public DefinedColumn(String columnName) {
        this.columnName = columnName;
    }
}