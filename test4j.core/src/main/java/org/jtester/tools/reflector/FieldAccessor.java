package org.jtester.tools.reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.jtester.tools.commons.FieldHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FieldAccessor<T> {
    /**
     * The field to access.
     */
    private final Field  field;

    private final String clazName;

    private final String fieldName;

    /**
     * Constructor.
     * 
     * @param fieldName the field name
     * @param type the class from which to start searching the field
     */
    public FieldAccessor(Class claz, String fieldName) {
        if (fieldName == null || claz == null) {
            throw new NullPointerException("to get a field, the class type or field name can't be null.");
        }

        this.field = FieldHelper.getField(claz, fieldName);
        this.clazName = claz.getName();
        this.fieldName = fieldName;
    }

    public FieldAccessor(Object target, String fieldName) {
        if (fieldName == null || target == null) {
            throw new NullPointerException("to get a field, the target or field name can't be null.");
        }
        Class claz = target.getClass();
        this.field = FieldHelper.getField(claz, fieldName);
        this.clazName = claz.getName();
        this.fieldName = fieldName;
    }

    public FieldAccessor(Class claz, Field field) {
        if (field == null || claz == null) {
            throw new NullPointerException("to get a field, the target or field name can't be null.");
        }
        this.field = field;
        this.clazName = claz.getName();
        this.fieldName = field.getName();
    }

    public Class getFieldType() {
        return this.field.getType();
    }

    public final T get(Object target) {
        boolean isAccessible = this.field.isAccessible();
        try {
            this.field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            String info = String.format("to get field[%s] value from class[%s] error.", this.fieldName, this.clazName);
            throw new RuntimeException(info, e);
        } finally {
            this.field.setAccessible(isAccessible);
        }
    }

    public final T getStatic() {
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("Field " + fieldName + " is not static");
        }
        return get(null);
    }

    /**
     * {@inheritDoc}<br>
     * <br>
     * 如果value对象是spring proxy对象，异常信息的消息作了一些包装，使提示更明显
     */
    public final void set(Object target, Object value) {
        boolean isAccessible = this.field.isAccessible();
        try {
            this.field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            //e.printStackTrace();
            String info = String.format("to set field[%s] value into target[%s] error.", this.fieldName, this.clazName);
            throw new RuntimeException(info, e);
        } finally {
            this.field.setAccessible(isAccessible);
        }
    }

    public final void setStatic(Object value) {
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("Field " + fieldName + " is not static");
        }
        set(null, value);
    }
}
