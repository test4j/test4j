package org.jtester.hamcrest.matcher.string;

import java.util.Iterator;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class StringNotBlankMatcherTest implements JTester {

    @Test(expected = AssertionError.class)
    @DataFrom("dataForNotBlank")
    public void testNotBlank(String actual) {
        want.string(actual).notBlank();
    }

    public static Iterator dataForNotBlank() {
        return new DataIterator() {
            {
                data((String) null);
                data("");
                data(" ");
                data("\n");
            }
        };
    }

    @Test
    public void testNotBlank_Success() {
        want.string("tt").notBlank();
    }
}
