package org.test4j.module.database.utility;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.database.utility.script.H2Script;
import org.test4j.module.database.utility.script.MysqlScript;
import org.test4j.tools.commons.AnnotationHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public abstract class EntityScriptParser {
    protected static final String NEW_LINE_JOIN = ",\n\t";

    protected final Class klass;

    protected final DbTypeConvert typeConvert;

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
    public static String script(DataSourceType type, DbTypeConvert typeConvert, List<Class> klasses) {
        return klasses.stream()
                .map(klass -> EntityScriptParser.newScriptParser(type, typeConvert, klass))
                .map(EntityScriptParser::script)
                .collect(joining("\n\n"));
    }

    protected static EntityScriptParser newScriptParser(DataSourceType type, DbTypeConvert typeConvert, Class klass) {
        switch (type) {
            case MySql:
            case MariaDB4J:
                return new MysqlScript(typeConvert, klass);
            default:
                return new H2Script(typeConvert, klass);
        }
    }

    /**
     * 构造具体的脚步语句
     *
     * @return
     */
    public abstract String script();

    protected String findPrimaryFieldNames(List<ColumnDefine> columns) {
        return columns.stream()
                .filter(column -> column.primaryType != PrimaryType.None)
                .map(column -> column.name)
                .collect(joining(","));
    }

    protected List<ColumnDefine> findColumns() {
        Set<Field> annotations = AnnotationHelper.getFieldsAnnotatedWith(klass, ColumnDef.class);
        if (annotations == null || annotations.isEmpty()) {
            return null;
        }
        return annotations.stream().map(ColumnDefine::new).collect(toList());
    }

    protected String parseColumn(List<ColumnDefine> columns) {
        return columns.stream()
                .map(this::parseColumn)
                .collect(joining(NEW_LINE_JOIN));
    }

    protected abstract String parseColumn(ColumnDefine column);

    protected String quotation(String column) {
        return String.format("\"%s\"", column);
    }

    protected String convertColumnType(String type) {
        String _type = typeConvert.convertType(type);
        return _type == null ? type : _type;
    }

    protected String getTableName() {
        TableName annotation = AnnotationHelper.getClassLevelAnnotation(TableName.class, klass);
        if (annotation == null) {
            throw new RuntimeException("the entity class[" + klass.getName() + "] should be defined by @TableName");
        } else {
            return annotation.value();
        }
    }

    protected static class ColumnDefine {
        public String name;

        /**
         * 数据库字段类型
         *
         * @return
         */
        public String type;

        /**
         * 是否主键
         *
         * @return
         */
        public PrimaryType primaryType;

        /**
         * 允许字段为null
         *
         * @return
         */
        public boolean notNull;

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
