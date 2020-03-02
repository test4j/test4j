package org.test4j.module.database.utility.script;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import org.test4j.module.database.utility.EntityScriptParser;

import java.util.List;

public class H2Script extends EntityScriptParser {
    public H2Script(DbTypeConvert typeConvert, Class klass) {
        super(typeConvert, klass);
    }

    public String script() {
        String tableName = this.getTableName();
        List<EntityScriptParser.ColumnDefine> columns = this.findColumns();
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

    protected String parseColumn(ColumnDefine column) {
        if (column.primaryType == ColumnDef.PrimaryType.AutoIncrease) {
            return String.format("%s %s NOT NULL AUTO_INCREMENT",
                    this.quotation(column.name),
                    this.convertColumnType(column.type));
        } else if (column.primaryType == ColumnDef.PrimaryType.Customized) {
            return String.format("%s %s NOT NULL",
                    this.quotation(column.name),
                    this.convertColumnType(column.type)
            );
        } else {
            return String.format("%s %s %s",
                    this.quotation(column.name),
                    this.convertColumnType(column.type),
                    column.notNull ? "NOT NULL" : "DEFAULT NULL"
            );
        }
    }
}
