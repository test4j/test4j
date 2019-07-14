package org.test4j.module.database.utility;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.org.atool.mbplus.annotation.ColumnDef;
import org.test4j.tools.commons.AnnotationHelper;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class EntityScriptParser {
    private static final String NEW_LINE_JOIN = ",\n\t" ;

    private Class klass;

    public EntityScriptParser(Class klass) {
        this.klass = klass;
    }

    public static String convertType(String type) {
        // 默认， 如果没有特殊处理，返回原类型
        return type;
    }

    /**
     * 根据实体klasses定义自动生成数据库脚本
     *
     * @param klasses
     * @return
     */
    public static String script(Class... klasses) {
        return Arrays.stream(klasses)
                .map(EntityScriptParser::new)
                .map(EntityScriptParser::h2sql)
                .collect(joining("\n\n"));
    }

    public String h2sql() {
        String tableName = this.getTableName();
        List<ColumnDefine> columns = this.findColumns();
        return new StringBuilder()
                .append(String.format("drop table IF exists %s;\n", tableName))
                .append(String.format("CREATE TABLE \"%s\" (\n\t", tableName))
                .append(this.parseColumn(columns))
                .append(NEW_LINE_JOIN)
                .append(String.format("PRIMARY KEY (%s)", this.findPrimaryFieldNames(columns)))
                .append(");\n")
                .toString();
    }


    private String findPrimaryFieldNames(List<ColumnDefine> columns) {
        return columns.stream()
                .filter(column -> column.primary)
                .map(column -> column.name)
                .collect(joining(","));
    }

    private List<ColumnDefine> findColumns() {
        Set<Field> annotations = AnnotationHelper.getFieldsAnnotatedWith(klass, ColumnDef.class);
        if (annotations == null || annotations.isEmpty()) {
            return null;
        }
        return annotations.stream().map(ColumnDefine::new).collect(toList());
    }

    private String parseColumn(List<ColumnDefine> columns) {
        return columns.stream()
                .map(this::parseColumn)
                .collect(joining(NEW_LINE_JOIN));
    }

    private String parseColumn(ColumnDefine column) {
        if (column.primary) {
            return String.format("%s %s NOT NULL AUTO_INCREMENT",
                    this.quotation(column.name),
                    this.convertColumnType(column.type));
        } else {
            return String.format("%s %s %s",
                    this.quotation(column.name),
                    this.convertColumnType(column.type),
                    column.notNull ? "NOT NULL" : "DEFAULT NULL"
            );
        }
    }

    private String quotation(String column) {
        return String.format("\"%s\"", column);
    }

    private String convertColumnType(String type) {
        String _type = convertType(type.toLowerCase());
        return _type == null ? type : _type;
    }

    private String getTableName() {
        TableName annotation = AnnotationHelper.getClassLevelAnnotation(TableName.class, klass);
        if (annotation == null) {
            throw new RuntimeException("the entity class[" + klass.getName() + "] should be defined by @TableName");
        } else {
            return annotation.value();
        }
    }

    private static class ColumnDefine {
        String name;

        /**
         * 数据库字段类型
         *
         * @return
         */
        String type;

        /**
         * 是否主键
         *
         * @return
         */
        boolean primary;

        /**
         * 允许字段为null
         *
         * @return
         */
        boolean notNull;

        public ColumnDefine(Field field) {
            this.name = field.getName();
            ColumnDef def = field.getAnnotation(ColumnDef.class);
            if (def != null) {
                this.type = def.type();
                this.primary = def.primary();
                this.notNull = def.notNull();
            }
        }
    }
}
