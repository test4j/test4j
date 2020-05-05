package org.test4j.generator.mybatis.db;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.generator.mybatis.db.ColumnType;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

class ColumnTypeTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data_getFieldType")
    void getFieldType(ColumnType columnType, String typeName) {
        String type = columnType.getFieldType();
        want.string(type).eq(typeName);
    }

    public static DataProvider data_getFieldType() {
        return new DataProvider()
            .data(ColumnType.BASE_BYTE, "byte")
            .data(ColumnType.BYTE_ARRAY, "byte[]")
            ;
    }
}