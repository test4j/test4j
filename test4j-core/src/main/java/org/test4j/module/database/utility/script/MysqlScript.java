package org.test4j.module.database.utility.script;

import org.test4j.module.database.utility.EntityScriptParser;

import java.util.List;

/**
 * mysql语法生成
 */
public class MysqlScript extends EntityScriptParser {
    public MysqlScript(DbTypeConvert typeConvert, Class klass) {
        super(typeConvert, klass);
    }

    @Override
    public String script() {
        List<ColumnDefine> columns = this.findColumns();
        String table = this.getTableName();
        StringBuilder buff = new StringBuilder()
            .append(String.format("drop table IF exists `%s`;\n", table))
            .append(String.format("CREATE TABLE `%s` (\n\t", table))
            .append(this.parseColumn(columns));
        return buff.append(");\n").toString();
    }

    @Override
    protected String parseColumn(ColumnDefine column) {
        if (column.primary) {
            return String.format("%s %s not null %s primary key",
                this.quotation(column.name),
                this.convertColumnType(column.type),
                column.autoIncrease ? "auto_increment" : "");
        } else {
            return String.format("%s %s %s",
                this.quotation(column.name),
                this.convertColumnType(column.type),
                column.notNull ? "not null" : "null"
            );
        }
    }

    @Override
    protected String quotation(String column) {
        return String.format("`%s`", column);
    }
}