package org.test4j.generator.mybatis.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.db.IJavaType;

@Getter
@Accessors(chain = true)
public class TableColumn {
    private String columnName;

    private String fieldName;

    private IJavaType javaType;

    @Setter
    private boolean exclude = false;

    public TableColumn(String columnName, String fieldName, IJavaType javaType) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.javaType = javaType;
    }

    public TableColumn(String columnName) {
        this.columnName = columnName;
    }
}
