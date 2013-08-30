package org.jtester.module.database.environment;

import mockit.Mock;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class TableMetaTest implements JTester {

    @Test
    @DataFrom("dataTruncate")
    public void testTruncateString(String input, String expected) {
        TableMeta meta = reflector.newInstance(TableMeta.class);
        new MockUp<TableMeta>() {
            @Mock
            public int getCloumnSize(String column) {
                want.string(column).isEqualTo("columnName");
                return 5;
            }
        };
        String value = meta.truncateString("columnName", input);
        want.object(value).isEqualTo(expected);
    }

    public static DataIterator dataTruncate() {
        return new DataIterator() {
            {
                data("123456", "12345");
                data("123", "123");
                data("12345", "12345");
            }
        };
    }
}
