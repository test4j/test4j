package org.test4j.generator.mybatis.model;

import org.test4j.generator.mybatis.rule.IColumnType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class TableColumn {
    private String columnName;

    private String propertyName;

    private IColumnType columnType;

    @Setter
    private boolean exclude = false;

    public TableColumn(String columnName, String propertyName, IColumnType columnType) {
        this.columnName = columnName;
        this.propertyName = propertyName;
        this.columnType = columnType;
    }

    public TableColumn(String columnName) {
        this.columnName = columnName;
    }
}
