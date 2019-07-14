package org.test4j.tools.generic;

import java.lang.reflect.TypeVariable;

/**
 * 泛型声明变量定位
 * 
 * @author darui.wudr 2013-10-30 上午11:28:54
 */
@SuppressWarnings("rawtypes")
public class GenericTypeKey {
    private final String clazName;

    private final String simpleClazName;

    private final String genericName;

    public GenericTypeKey(Class claz, String genericName) {
        this.clazName = claz.getName();
        this.simpleClazName = claz.getSimpleName();
        this.genericName = genericName;
    }

    public GenericTypeKey(String claz, String genericName) {
        this.clazName = claz;
        this.simpleClazName = claz.substring(claz.lastIndexOf('.') + 1);
        this.genericName = genericName;
    }

    public GenericTypeKey(TypeVariable typeVariable) {
        this((Class) typeVariable.getGenericDeclaration(), typeVariable.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clazName == null) ? 0 : clazName.hashCode());
        result = prime * result + ((genericName == null) ? 0 : genericName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenericTypeKey other = (GenericTypeKey) obj;
        if (clazName == null) {
            if (other.clazName != null)
                return false;
        } else if (!clazName.equals(other.clazName))
            return false;
        if (genericName == null) {
            if (other.genericName != null)
                return false;
        } else if (!genericName.equals(other.genericName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return simpleClazName + "<" + genericName + ">";
    }
}
