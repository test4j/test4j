package cn.org.atool.fluent.mybatis.annotation;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.database.utility.EntityScriptParser;
import org.test4j.tools.commons.AnnotationHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TableNameCompatible: 兼容性处理, 后续版本去掉
 *
 * @author darui.wu
 * @create 2020/5/21 1:34 下午
 */
@Deprecated
public class TableNameCompatible {
    public static String ColumnDef_Klass_Name = "cn.org.atool.fluent.mybatis.annotation.ColumnDef";

    public static String TableName_Klass_Name = "com.baomidou.mybatisplus.annotation.TableName";

    /**
     * 获取老的TableName注解的表名, 后续版本去掉
     *
     * @param klass
     * @return
     */
    public static String getTableName(Class klass) {
        try {
            TableName annotation = AnnotationHelper.getClassLevelAnnotation(TableName.class, klass);
            if (annotation == null) {
                throw new RuntimeException("the entity class[" + klass.getName() + "] should be defined by @ScriptTable");
            } else {
                return annotation.value();
            }
        } catch (Throwable e) {
            throw new RuntimeException("the entity class[" + klass.getName() + "] should be defined by @ScriptTable");
        }
    }

    /**
     * 获取老的ColumnDefine注解信息, 后续版本去掉
     *
     * @param field
     */
    public static EntityScriptParser.ColumnDefine init(Field field) {
        EntityScriptParser.ColumnDefine column = new EntityScriptParser.ColumnDefine();
        ColumnDef def = field.getAnnotation(ColumnDef.class);
        {
            column.setName(field.getName());
            column.type = def.type();
            column.primary = def.primary() != ColumnDef.PrimaryType.None;
            column.autoIncrease = def.primary() == ColumnDef.PrimaryType.AutoIncrease;
            column.notNull = def.notNull();
        }
        return column;
    }

    /**
     * 获取老的ColumnDefine注解信息, 后续版本去掉
     *
     * @param klass
     * @return
     */
    public static List<EntityScriptParser.ColumnDefine> findFields(Class klass) {
        Set<Field> annotations = AnnotationHelper.getFieldsAnnotatedWith(klass, ColumnDef.class);
        if (annotations == null || annotations.isEmpty()) {
            throw new RuntimeException("the entity[" + klass.getName() + "] field should be defined by @ColumnDef");
        }
        return annotations.stream()
            .map(TableNameCompatible::init)
            .collect(Collectors.toList());
    }
}