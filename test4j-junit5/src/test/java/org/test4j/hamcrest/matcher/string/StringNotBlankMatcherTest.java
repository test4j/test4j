package org.test4j.hamcrest.matcher.string;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

@SuppressWarnings("rawtypes")
public class StringNotBlankMatcherTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataForNotBlank")
    public void testNotBlank(String actual) {
        want.exception(() ->
                        want.string(actual).notBlank()
                , AssertionError.class);
    }

    public static Iterator dataForNotBlank() {
        return new DataProvider() {
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
