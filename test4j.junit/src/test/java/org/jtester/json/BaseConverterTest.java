package org.jtester.json;

import java.util.Iterator;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class BaseConverterTest implements JTester {

    @Test
    @DataFrom("dataForBaseConverter")
    public void testBaseConvert(String input, Object expected) {
        Object value = JSONConverter.defaultConverter.convert(input);
        want.object(value).isEqualTo(expected);
    }

    public static Iterator dataForBaseConverter() {
        return new DataIterator() {

            {
                data("129", 129);
                data("200", 200);
                data("200L", 200L);
                data("200l", 200L);
                data("2.00", 2.00D);
                data("2.00D", 2.00D);
                data("2.00d", 2.00D);
                data("2F", 2F);
                data("2f", 2F);
                data("true", true);
                data("tRue", true);
                data("false", false);
                data("FALSE", false);
                data("", "");
                /**
                 * 任何字符串，只有不是true，就返回 false
                 */
                data("Anything", "Anything");
            }
        };
    }
}
