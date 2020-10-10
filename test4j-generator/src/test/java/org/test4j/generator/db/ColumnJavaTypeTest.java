package org.test4j.generator.db;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

class ColumnJavaTypeTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data_getFieldType")
    void getFieldType(Class javaType, String typeName) {
        String type = javaType.getSimpleName();
        want.string(type).eq(typeName);
    }

    public static DataProvider data_getFieldType() {
        return new DataProvider()
            .data(byte.class, "byte")
            .data(byte[].class, "byte[]")
            ;
    }
}