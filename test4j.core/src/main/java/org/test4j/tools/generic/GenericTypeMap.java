package org.test4j.tools.generic;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * 泛型变量列表
 * 
 * @author darui.wudr 2013-10-30 上午11:31:47
 */
@SuppressWarnings("rawtypes")
public class GenericTypeMap extends HashMap<GenericTypeKey, Type> {
    private static final long serialVersionUID = -8181224276624369854L;

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (Map.Entry<GenericTypeKey, Type> entry : this.entrySet()) {
            buff.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return buff.toString();
    }

    public void putType(TypeVariable var, Type value) {
        this.put(new GenericTypeKey(var), value);
    }

    public void putType(Class genericDeclare, String genericName, Type type) {
        this.put(new GenericTypeKey(genericDeclare, genericName), type);
    }

    public void putGeneric(TypeVariable var, TypeVariable value) {
        Type type = this.getType(value);
        this.put(new GenericTypeKey(var), type == null ? value : type);
    }

    public void putGeneric(TypeVariable arg) {
        Type type = this.getType(arg);
        if (type != null) {
            this.put(new GenericTypeKey(arg), type);
        }
    }

    public Type getType(TypeVariable value) {
        return this.get(new GenericTypeKey(value));
    }

    public Type getType(String genericDeclare, String genericName) {
        return this.get(new GenericTypeKey(genericDeclare, genericName));
    }

    public Type getType(Class genericDeclare, String genericName) {
        return this.get(new GenericTypeKey(genericDeclare, genericName));
    }
}
