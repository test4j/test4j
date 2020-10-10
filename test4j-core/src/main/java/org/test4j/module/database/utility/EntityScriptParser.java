package org.test4j.module.database.utility;

import cn.org.atool.fluent.mybatis.annotation.TableNameCompatible;
import javafx.animation.KeyValue;
import lombok.Setter;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.utility.script.H2Script;
import org.test4j.module.database.utility.script.MysqlScript;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.StringHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.test4j.tools.commons.StringHelper.isNotBlank;

/**
 * 实体对应数据表脚本生成
 *
 * @author darui.wu
 */
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
            .filter(column -> column.primary)
            .map(column -> column.name)
            .collect(joining(","));
    }

    protected List<ColumnDefine> findColumns() {
        Set<Field> annotations = AnnotationHelper.getFieldsAnnotatedWith(klass, ColumnDef.class);
        if (annotations != null && !annotations.isEmpty()) {
            return annotations.stream().map(ColumnDefine::new).collect(toList());
        } else if (ClazzHelper.isClassAvailable(TableNameCompatible.ColumnDef_Klass_Name)) {
            return TableNameCompatible.findFields(klass);
        } else {
            throw new RuntimeException("the entity[" + klass.getName() + "] field should be defined by @ColumnDef");
        }
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
        ScriptTable annotation = AnnotationHelper.getClassLevelAnnotation(ScriptTable.class, klass);
        if (annotation == null) {
            if (ClazzHelper.isClassAvailable(TableNameCompatible.TableName_Klass_Name)) {
                return TableNameCompatible.getTableName(klass);
            }
            throw new RuntimeException("the entity class[" + klass.getName() + "] should be defined by @ScriptTable");
        } else {
            return annotation.value();
        }
    }

    @Setter
    public static class ColumnDefine {
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
        public boolean primary;
        /**
         * 自增
         */
        public boolean autoIncrease;
        /**
         * 允许字段为null
         *
         * @return
         */
        public boolean notNull;
        /**
         * 默认值
         */
        public String defaultValue;

        public ColumnDefine() {
        }

        public ColumnDefine(Field field) {
            this.name = StringHelper.camel(field.getName());
            ColumnDef def = field.getAnnotation(ColumnDef.class);
            if (def != null) {
                this.init(def);
            } else {
                throw new RuntimeException("the field[" + field.getName() + "] should be defined by @ColumnDef");
            }
        }

        private void init(ColumnDef def) {
            if (isNotBlank(def.value())) {
                this.name = def.value();
            }
            this.type = def.type();
            this.primary = def.primary();
            this.autoIncrease = def.autoIncrease();
            this.notNull = def.notNull();
            this.defaultValue = def.defaultValue();
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