package org.test4j.module.database.utility;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.tools.commons.AnnotationHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class EntityScriptParser {
    private static final String NEW_LINE_JOIN = ",\n\t";

    private final Class klass;

    private final DbTypeConvert typeConvert;

    public EntityScriptParser(DbTypeConvert typeConvert, Class klass) {
        this.typeConvert = typeConvert == null ? new NonDbTypeConvert() : typeConvert;
        this.klass = klass;
    }


    /**
     * 根据实体klasses定义自动生成数据库脚本
     *
     * @param klasses
     * @return
     */
    public static String script(DbTypeConvert typeConvert, Class... klasses) {
        return Arrays.stream(klasses)
                .map(klass -> new EntityScriptParser(typeConvert, klass))
                .map(EntityScriptParser::h2sql)
                .collect(joining("\n\n"));
    }

    public String h2sql() {
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

    private String findPrimaryFieldNames(List<ColumnDefine> columns) {
        return columns.stream()
                .filter(column -> column.primaryType != PrimaryType.None)
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
        if (column.primaryType == PrimaryType.AutoIncrease) {
            return String.format("%s %s NOT NULL AUTO_INCREMENT",
                    this.quotation(column.name),
                    this.convertColumnType(column.type));
        } else if (column.primaryType == PrimaryType.Customized) {
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

    private String quotation(String column) {
        return String.format("\"%s\"", column);
    }

    private String convertColumnType(String type) {
        String _type = typeConvert.convertType(type);
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
        PrimaryType primaryType;

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
                this.primaryType = def.primary();
                this.notNull = def.notNull();
            }
        }
    }

    public interface DbTypeConvert {
        /**
         * 原生数据库字段类型转换为测试数据库（内存库）字段类型
         *
         * @param type 原生数据库字段类型
         * @return 测试数据库（内存库）字段类型
         */
        String convertType(String type);
    }

    public static class NonDbTypeConvert implements DbTypeConvert {
        /**
         * 默认， 如果没有特殊处理，返回原类型
         *
         * @param type
         * @return
         */
        @Override
        public String convertType(String type) {
            return type;
        }
    }
}
