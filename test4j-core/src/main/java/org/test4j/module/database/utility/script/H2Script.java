package org.test4j.module.database.utility.script;

import org.test4j.module.database.utility.EntityScriptParser;

import java.util.List;

public class H2Script extends EntityScriptParser {
    public H2Script(DbTypeConvert typeConvert, Class klass) {
        super(typeConvert, klass);
    }

    @Override
    public String script() {
        String tableName = this.getTableName();
        List<ColumnDefine> columns = this.findColumns();
        StringBuilder buff = new StringBuilder()
            .append(String.format("drop table IF exists %s;\n", tableName))
            .append(String.format("CREATE TABLE \"%s\" (\n\t", tableName))
            .append(this.parseColumn(columns));
        String key = this.findPrimaryFieldNames(columns);
        if (key != null && !"".equals(key.trim())) {
            buff.append(NEW_LINE_JOIN)
                .append(String.format("PRIMARY KEY (%s)", key));
        }
        return buff.append(");\n").toString();
    }

    @Override
    protected String parseColumn(ColumnDefine column) {
        if (column.primary) {
            return String.format("%s %s NOT NULL %s",
                this.quotation(column.name),
                this.convertColumnType(column.type),
                column.autoIncrease ? "AUTO_INCREMENT" : "");
        } else {
            return String.format("%s %s %s",
                this.quotation(column.name),
                this.convertColumnType(column.type),
                column.notNull ? "NOT NULL" : "DEFAULT NULL"
            );
        }
    }
}