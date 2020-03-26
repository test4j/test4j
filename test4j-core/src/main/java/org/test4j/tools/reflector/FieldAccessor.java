package org.test4j.tools.reflector;

import lombok.Getter;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * 字段访问工具
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FieldAccessor {
    /**
     * The field to access.
     */
    @Getter
    private final Field field;
    @Getter
    private final String declaringClass;
    @Getter
    private final String fieldName;

    /**
     * Constructor.
     *
     * @param klass     the class from which to start searching the field
     * @param fieldName the field name
     */
    private FieldAccessor(Class klass, String fieldName) {
        if (fieldName == null || klass == null) {
            throw new NullPointerException("to get a field, the class type or field name can't be null.");
        }
        this.field = Reflector.getField(klass, fieldName);
        this.declaringClass = klass.getName();
        this.fieldName = fieldName;
    }

    private FieldAccessor(Object target, String fieldName) {
        if (fieldName == null || target == null) {
            throw new NullPointerException("to get a field, the target or field name can't be null.");
        }
        Object _target = ClazzHelper.getProxiedObject(target);
        Class klass = _target.getClass();
        this.field = Reflector.getField(klass, fieldName);
        this.declaringClass = klass.getName();
        this.fieldName = fieldName;
    }

    private FieldAccessor(Field field) {
        if (field == null) {
            throw new NullPointerException("to get a field, the target or field name can't be null.");
        }
        this.field = field;
        this.declaringClass = field.getDeclaringClass().getName();
        this.fieldName = field.getName();
    }

    /**
     * 返回字段类型
     *
     * @return
     */
    public Class getFieldType() {
        return this.field.getType();
    }

    /**
     * 返回字段类型
     *
     * @return
     */
    public Type getFieldGenericType() {
        return this.field.getGenericType();
    }

    /**
     * 返回字段值
     *
     * @param target
     * @return
     */
    public final <T> T get(Object target) {
        boolean isAccessible = this.field.isAccessible();
        try {
            this.field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            String info = String.format("to get field[%s] value from class[%s] error.", this.fieldName, this.declaringClass);
            throw new RuntimeException(info, e);
        } finally {
            this.field.setAccessible(isAccessible);
        }
    }

    /**
     * 返回静态字段值
     *
     * @return
     */
    public final <T> T getStatic() {
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("Field " + fieldName + " is not static");
        }
        return get(null);
    }

    /**
     * 设置字段值
     * <br>
     * 如果value对象是spring proxy对象，异常信息的消息作了一些包装，使提示更明显
     */
    public final void set(Object target, Object value) {
        boolean isAccessible = this.field.isAccessible();
        try {
            Object _target = ClazzHelper.getProxiedObject(target);
            this.field.setAccessible(true);
            field.set(_target, value);
        } catch (Exception e) {
            String info = String.format("to set field[%s] value into target[%s] error.", this.fieldName, this.declaringClass);
            throw new RuntimeException(info, e);
        } finally {
            this.field.setAccessible(isAccessible);
        }
    }

    /**
     * 设置静态字段值
     *
     * @param value
     */
    public final void setStatic(Object value) {
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("Field " + fieldName + " is not static");
        }
        set(null, value);
    }

    /**
     * 构造字段访问器
     *
     * @param klass
     * @param fieldName
     * @return
     */
    public static FieldAccessor field(Class klass, String fieldName) {
        return new FieldAccessor(klass, fieldName);
    }

    /**
     * 构造字段访问器
     *
     * @param target
     * @param fieldName
     * @return
     */
    public static FieldAccessor field(Object target, String fieldName) {
        return new FieldAccessor(target, fieldName);
    }

    /**
     * 构造字段访问器
     *
     * @param field
     * @return
     */
    public static FieldAccessor field(Field field) {
        return new FieldAccessor(field);
    }

    /**
     * 获取值
     *
     * @param target
     * @param field
     * @return
     */
    public static Object getValue(Object target, String field) {
        return new FieldAccessor(target, field).get(target);
    }

    /**
     * 设置值
     *
     * @param target
     * @param field
     * @param value
     */
    public static void setValue(Object target, String field, Object value) {
        new FieldAccessor(target, field).set(target, value);
    }

    /**
     * 获取静态值
     *
     * @param clazz
     * @param field
     * @return
     */
    public static Object getStaticValue(Class clazz, String field) {
        return new FieldAccessor(clazz, field).getStatic();
    }

    /**
     * 设置静态值
     *
     * @param clazz
     * @param field
     * @param value
     */
    public static void setStaticValue(Class clazz, String field, Object value) {
        new FieldAccessor(clazz, field).setStatic(value);
    }
}
