package org.test4j.json;

import java.util.Iterator;

import org.junit.Test;
import org.test4j.json.JSONConverter;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;

@SuppressWarnings("rawtypes")
public class BaseConverterTest implements Test4J {

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
