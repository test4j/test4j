package org.test4j.module.database.environment.typesmap;

import lombok.Getter;
import org.test4j.module.database.environment.typesmap.TypeMap.JavaSQLType;

import java.util.HashMap;

public class TypeMap extends HashMap<String, JavaSQLType> {
    private static final long serialVersionUID = -8446876368817445261L;

    public void put(String type, Class javaType, int sqlType) {
        this.put(type, new JavaSQLType(javaType, sqlType));
    }

    @Getter
    public static class JavaSQLType {
        Class javaType;

        int sqlType;

        public JavaSQLType(Class javaType, int sqlType) {
            this.javaType = javaType;
            this.sqlType = sqlType;
        }
    }
}