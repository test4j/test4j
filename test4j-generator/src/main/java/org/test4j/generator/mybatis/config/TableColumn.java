package org.test4j.generator.mybatis.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.db.IJavaType;

@Getter
@Accessors(chain = true)
public class TableColumn {
    private String columnName;

    private String propertyName;

    private IJavaType columnType;

    @Setter
    private boolean exclude = false;

    public TableColumn(String columnName, String propertyName, IJavaType columnType) {
        this.columnName = columnName;
        this.propertyName = propertyName;
        this.columnType = columnType;
    }

    public TableColumn(String columnName) {
        this.columnName = columnName;
    }
}
