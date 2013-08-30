package org.test4j.tools.commons;

import java.util.Iterator;

import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "test4j", "utility" })
public class PrimitiveHelperTest extends Test4J {

    @Test(dataProvider = "testDoesEqualData")
    public void testDoesEqual(Number num1, Number num2, boolean result) {
        boolean actual = PrimitiveHelper.doesEqual(num1, num2);
        want.bool(actual).is(result);
    }

    @DataProvider
    public Iterator testDoesEqualData() {
        return new DataIterator() {
            {
                data(1, 1L, true);
                data(Integer.valueOf(2), 2L, true);
                data(Long.valueOf(3), Short.valueOf("3"), true);
                data(4, 4.0, false);
                data(5.0d, 5.0f, true);
            }
        };
    }
}
