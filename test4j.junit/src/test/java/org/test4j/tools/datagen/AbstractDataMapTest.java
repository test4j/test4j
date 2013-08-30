package org.test4j.tools.datagen;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;
import org.test4j.json.ITypeConverter;
import org.test4j.junit.JTester;
import org.test4j.junit.annotations.DataFrom;

@SuppressWarnings({ "serial", "rawtypes" })
public class AbstractDataMapTest implements JTester {

    @Test
    @DataFrom("dataForConvert")
    public void testConvert(final Object value, Object expected) {
        final String key = "my-test-key";
        DataMap map = new DataMap() {

            {
                this.put(key, value);
            }
        };
        map.convert(key, ITypeConverter.defaultConverter);
        Object list = map.get(key);
        want.object(list).reflectionEq(expected);
    }

    public static Iterator dataForConvert() {
        return new DataIterator() {

            {
                data("true", true);
                data("[1,2,3]", Arrays.asList(1, 2, 3));
                data("[true,true,false]", new Boolean[] { true, true, false });
            }
        };
    }
}
