package org.test4j.module.database.environment;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.test4j.Test4J;
import org.test4j.mock.Mock;
import org.test4j.mock.MockUp;
import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;

public class TableMetaTest implements Test4J {

    @ParameterizedTest
    @MethodSource("dataTruncate")
    public void testTruncateString(String input, String expected) {
        TableMeta meta = reflector.newInstance(TableMeta.class);
        new MockUp<TableMeta>() {
            @Mock
            public int getColumnSize(String column) {
                want.string(column).isEqualTo("columnName");
                return 5;
            }
        };
        String value = meta.truncateString("columnName", input);
        want.object(value).isEqualTo(expected);
    }

    public static Iterator dataTruncate() {
        return new DataProvider() {
            {
                data("123456", "12345");
                data("123", "123");
                data("12345", "12345");
            }
        };
    }
}