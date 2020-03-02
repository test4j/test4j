package org.test4j.module.database.utility.script;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import org.test4j.module.database.utility.EntityScriptParser;

import java.util.List;

/**
 * mysql语法生成
 */
public class MysqlScript extends EntityScriptParser {
    public MysqlScript(DbTypeConvert typeConvert, Class klass) {
        super(typeConvert, klass);
    }

    public String script() {
        List<ColumnDefine> columns = this.findColumns();
        String table = this.getTableName();
        StringBuilder buff = new StringBuilder()
                .append(String.format("drop table IF exists `%s`;\n", table))
                .append(String.format("CREATE TABLE `%s` (\n\t", table))
                .append(this.parseColumn(columns));
        return buff.append(");\n").toString();
    }

    protected String parseColumn(ColumnDefine column) {
        if (column.primaryType == ColumnDef.PrimaryType.AutoIncrease) {
            return String.format("%s %s not null auto_increment primary key",
                    this.quotation(column.name),
                    this.convertColumnType(column.type));
        } else if (column.primaryType == ColumnDef.PrimaryType.Customized) {
            return String.format("%s %s not null primary key",
                    this.quotation(column.name),
                    this.convertColumnType(column.type)
            );
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
