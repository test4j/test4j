package org.test4j.generator.javafile;

import com.squareup.javapoet.*;
import org.test4j.generator.config.impl.TableField;
import org.test4j.generator.config.impl.TableSetter;

import javax.lang.model.element.Modifier;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataMapFile extends BaseFile {
    static ClassName ScriptTable = ClassName.get("org.test4j.module.database.annotations", "ScriptTable");

    static ClassName DataMap = ClassName.get("org.test4j.module.ICore", "DataMap");

    static ClassName KeyValue = ClassName.get("org.test4j.tools.datagen", "KeyValue");

    static ClassName ColumnDef = ClassName.get("org.test4j.module.database.annotations", "ColumnDef");

    public DataMapFile(TableSetter table) {
        super(table);
        this.packageName = dmPackage(table);
        this.klassName = dmClassName(table);
    }

    public static ClassName dmName(TableSetter table) {
        return ClassName.get(dmPackage(table), dmClassName(table));
    }

    public static String dmPackage(TableSetter table) {
        return table.getBasePackage() + ".dm";
    }

    public static String dmClassName(TableSetter table) {
        return table.getEntityPrefix() + "DataMap";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addAnnotation(AnnotationSpec.builder(ScriptTable).addMember("value", "$S", table.getTableName()).build());
        builder.superclass(parameterizedType(DataMap, dmName(table)));
        builder.addField(FieldSpec.builder(boolean.class, "isTable", Modifier.PRIVATE).build());
        builder.addField(FieldSpec.builder(parameterizedType(Supplier.class, Boolean.class),
            "supplier", Modifier.PRIVATE)
            .initializer("() -> this.isTable")
            .build());
        for (TableField field : table.getFields()) {
            builder.addField(this.buildField(field));
        }
        builder.addMethod(this.m_constructor1());
        builder.addMethod(this.m_constructor2());
        builder.addMethod(this.m_init());
        builder.addMethod(this.m_with());
        builder.addMethod(this.m_table_0());
        builder.addMethod(this.m_table_1());
        builder.addMethod(this.m_entity_0());
        builder.addMethod(this.m_entity_1());
        builder.addType(this.clazz_Factory());
    }

    private TypeSpec clazz_Factory() {
        return TypeSpec.classBuilder("Factory")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addMethod(m_Factory_table_0())
            .addMethod(m_Factory_table_1())
            .addMethod(m_Factory_initTable_0())
            .addMethod(m_Factory_initTable_1())
            .addMethod(m_Factory_entity_0())
            .addMethod(m_Factory_entity_1())
            .build();
    }

    private MethodSpec m_Factory_entity_0() {
        return this.initPublicMethod("entity")
            .addStatement("return $T.entity()", dmName(table))
            .build();
    }

    private MethodSpec m_Factory_entity_1() {
        return this.initPublicMethod("entity")
            .addParameter(int.class, "size")
            .addStatement("return  $T.entity(size)", dmName(table))
            .build();
    }

    private MethodSpec m_Factory_initTable_0() {
        return this.initPublicMethod("initTable")
            .addStatement("return $T.table().init()", dmName(table))
            .build();
    }

    private MethodSpec m_Factory_initTable_1() {
        return this.initPublicMethod("initTable")
            .addParameter(int.class, "size")
            .addStatement("return  $T.table(size).init()", dmName(table))
            .build();
    }

    private MethodSpec m_Factory_table_0() {
        return this.initPublicMethod("table")
            .addStatement("return $T.table()", dmName(table))
            .build();
    }

    private MethodSpec m_Factory_table_1() {
        return this.initPublicMethod("table")
            .addParameter(int.class, "size")
            .addStatement("return  $T.table(size)", dmName(table))
            .build();
    }

    private MethodSpec m_entity_1() {
        return this.initStaticMethod("entity")
            .addParameter(int.class, "size")
            .addStatement("return new $T(false, size)", dmName(table))
            .build();
    }

    private MethodSpec m_entity_0() {
        return this.initStaticMethod("entity")
            .addStatement("return new $T(false, 1)", dmName(table))
            .build();
    }

    private MethodSpec m_table_1() {
        return this.initStaticMethod("table")
            .addParameter(int.class, "size")
            .addStatement("return new $T(true, size)", dmName(table))
            .build();
    }

    private MethodSpec m_table_0() {
        return this.initStaticMethod("table")
            .addStatement("return new $T(true, 1)", dmName(table))
            .build();
    }


    private MethodSpec m_with() {
        MethodSpec.Builder builder = this.initPublicMethod("with")
            .addParameter(parameterizedType(ClassName.get(Consumer.class), dmName(table)), "init")
            .addStatement("init.accept(this)")
            .addStatement("return this");
        return builder.build();
    }

    private MethodSpec m_init() {
        MethodSpec.Builder builder = this.initPublicMethod("init");
        builder.addJavadoc("创建$L\n", this.klassName);
        builder.addJavadoc("初始化主键和gmtCreate, gmtModified, isDeleted等特殊值");
        for (TableField field : table.getFields()) {
            if (field.isPrimaryId()) {
                builder.addStatement("this.$L.autoIncrease()", field.getName());
            }
            if (field.isGmt()) {
                builder.addStatement("this.$L.values(new $T())", field.getName(), Date.class);
            }
            if (field.isDeleted() && field.getJavaType() == Boolean.class) {
                builder.addStatement("this.$L.values(false)", field.getName());
            }
        }
        builder.addStatement("return this");
        return builder.build();
    }

    private FieldSpec buildField(TableField field) {
        FieldSpec.Builder builder = FieldSpec.builder(parameterizedType(KeyValue, dmName(table)),
            field.getName(), Modifier.PUBLIC, Modifier.FINAL, Modifier.TRANSIENT);
        AnnotationSpec.Builder ab = AnnotationSpec.builder(ColumnDef)
            .addMember("value", "$S", field.getColumnName())
            .addMember("type", "$S", field.getJdbcType());
        if (field.isPrimary()) {
            ab.addMember("primary", "$L", Boolean.TRUE.toString());
        }
        if (field.isPrimaryId()) {
            ab.addMember("autoIncrease", "$L", Boolean.TRUE.toString());
        }
        builder.addAnnotation(ab.build());
        builder.initializer("new KeyValue(this, $S, $S, supplier)", field.getColumnName(), field.getName());
        return builder.build();
    }

    private MethodSpec m_constructor1() {
        return MethodSpec.constructorBuilder()
            .addParameter(boolean.class, "isTable")
            .addStatement("super()")
            .addStatement("this.isTable = isTable")
            .build();
    }

    private MethodSpec m_constructor2() {
        return MethodSpec.constructorBuilder()
            .addParameter(boolean.class, "isTable")
            .addParameter(int.class, "size")
            .addStatement("super(size)")
            .addStatement("this.isTable = isTable")
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    private MethodSpec.Builder initStaticMethod(String method) {
        return MethodSpec.methodBuilder(method)
            .returns(dmName(table))
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
    }

    private MethodSpec.Builder initPublicMethod(String method) {
        return MethodSpec.methodBuilder(method)
            .returns(dmName(table))
            .addModifiers(Modifier.PUBLIC);
    }
}
