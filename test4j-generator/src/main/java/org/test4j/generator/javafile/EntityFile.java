package org.test4j.generator.javafile;

import com.squareup.javapoet.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.generator.config.impl.TableField;
import org.test4j.generator.config.impl.TableSetter;
import org.test4j.generator.db.DbType;
import org.test4j.tools.commons.StringHelper;

import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.test4j.tools.commons.StringHelper.isNotBlank;

public class EntityFile extends BaseFile {
    static ClassName IEntity = ClassName.get(
        "cn.org.atool.fluent.mybatis.base", "IEntity");

    static ClassName TableId = ClassName.get(
        "cn.org.atool.fluent.mybatis.annotation", "TableId");

    static ClassName TableField = ClassName.get(
        "cn.org.atool.fluent.mybatis.annotation", "TableField");

    static ClassName FluentMybatis = ClassName.get(
        "cn.org.atool.fluent.mybatis.annotation", "FluentMybatis");

    static ClassName FluentDbType = ClassName.get(
        "cn.org.atool.fluent.mybatis.metadata", "DbType");

    public EntityFile(TableSetter table) {
        super(table);
        this.packageName = entityPackage(table);
        this.klassName = entityClass(table);
    }

    public static TypeName entityName(TableSetter table) {
        return ClassName.get(entityPackage(table), entityClass(table));
    }

    public static String entityPackage(TableSetter table) {
        return table.getBasePackage() + ".entity";
    }

    public static String entityClass(TableSetter table) {
        return table.getEntityPrefix() + "Entity";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder
            .addAnnotation(Data.class)
            .addAnnotation(AnnotationSpec.builder(Accessors.class).addMember("chain", "true").build())
            .addAnnotation(this.fluentMybatisAnnotation());
        builder.addSuperinterface(IEntity);
        this.addSuperInterface(builder, table.getEntityInterfaces());
        builder.addField(FieldSpec.builder(long.class, "serialVersionUID",
            Modifier.STATIC, Modifier.FINAL, Modifier.PRIVATE)
            .initializer("1L")
            .build());
        TableField primary = null;
        for (TableField field : table.getFields()) {
            FieldSpec.Builder fb = FieldSpec.builder(field.getJavaType(), field.getName(), Modifier.PRIVATE);
            fb.addJavadoc(field.getComment());
            if (field.isPrimary()) {
                primary = field;
                fb.addAnnotation(this.getTableIdAnnotation(field));
            } else {
                fb.addAnnotation(this.getTableFieldAnnotation(field));
            }
            builder.addField(fb.build());
        }
        if (primary != null) {
            builder.addMethod(this.m_findPk(primary));
        }
    }

    /**
     * 构造 @TableField(...)
     *
     * @param field
     * @return
     */
    private AnnotationSpec getTableFieldAnnotation(TableField field) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(TableField)
            .addMember("value", "$S", field.getColumnName());
        if (isNotBlank(field.getInsert())) {
            builder.addMember("insert", "$S", field.getInsert());
        }
        if (isNotBlank(field.getUpdate())) {
            builder.addMember("update", "$S", field.getUpdate());
        }
        if (field.getIsLarge() != null && !field.getIsLarge()) {
            builder.addMember("notLarge", "$L", Boolean.FALSE.toString());
        }
        if (field.getTypeHandler() != null) {
            builder.addMember("typeHandler", "$T.class", field.getTypeHandler());
        }
        return builder.build();
    }

    /**
     * 构造 @TableId(...)
     *
     * @param field
     * @return
     */
    private AnnotationSpec getTableIdAnnotation(TableField field) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(TableId)
            .addMember("value", "$S", field.getColumnName());
        if (!field.isPrimaryId()) {
            builder.addMember("auto", "$L", Boolean.FALSE.toString());
        }
        if (!StringHelper.isBlank(table.getSeqName())) {
            builder.addMember("seqName", "$S", table.getSeqName());
        }
        return builder.build();
    }

    private MethodSpec m_findPk(TableField primary) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("findPk")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(Serializable.class);
        builder.addStatement("return this.$L", primary.getName());
        return builder.build();
    }

    private void addSuperInterface(TypeSpec.Builder builder, List<Class> interfaces) {
        if (interfaces == null || interfaces.size() == 0) {
            return;
        }
        for (Class _interface : interfaces) {
            if (hasEntityType(_interface.getSimpleName(), _interface.getTypeParameters())) {
                builder.addSuperinterface(parameterizedType(ClassName.get(_interface), this.className()));
            } else {
                builder.addSuperinterface(_interface);
            }
        }
    }

    /**
     * 泛型参数只有一个，且可以设置为Entity对象
     *
     * @param interfaceName 接口名称
     * @param typeVariables 接口泛型参数列表
     * @return
     */
    private boolean hasEntityType(String interfaceName, TypeVariable[] typeVariables) {
        if (typeVariables.length != 1) {
            return false;
        }
        for (Type bound : typeVariables[0].getBounds()) {
            String tn = bound.getTypeName();
            if (Objects.equals(tn, interfaceName) || Allow_Entity_Bounds.contains(tn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Entity自定义接口的泛型如果是下列类型子类型，可以使用Entity作为泛型参数
     */
    private static Set<String> Allow_Entity_Bounds = new HashSet<>();

    static {
        Allow_Entity_Bounds.add("cn.org.atool.fluent.mybatis.base.IEntity");
        Allow_Entity_Bounds.add(Object.class.getName());
        Allow_Entity_Bounds.add(Serializable.class.getName());
    }

    /**
     * 生成 @FluentMybatis注解
     *
     * @return
     */
    private AnnotationSpec fluentMybatisAnnotation() {
        AnnotationSpec.Builder builder = AnnotationSpec
            .builder(FluentMybatis);

        builder.addMember("table", "$S", table.getTableName());
        if (isNotBlank(table.getMapperBeanPrefix())) {
            builder.addMember("mapperBeanPrefix", "$S", table.getMapperBeanPrefix());
        }
        if (table.getBaseDaoInterfaces() != null && !table.getBaseDaoInterfaces().isEmpty()) {
            String format = table.getBaseDaoInterfaces().stream()
                .map(dao -> "$T")
                .collect(joining(", ", "{", "}"));
            Object[] daos = table.getBaseDaoInterfaces().stream()
                .map(dao -> ClassName.get(dao))
                .collect(toList()).toArray();
            builder.addMember("daoInterface", format, daos);
        }
        if (DbType.MYSQL != table.getGlobalConfig().getDbType()) {
            builder.addMember("dbType", "$T.$L",
                FluentDbType, table.getGlobalConfig().getDbType().name());
        }
        return builder.build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}
