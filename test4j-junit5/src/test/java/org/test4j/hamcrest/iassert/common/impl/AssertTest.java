package org.test4j.hamcrest.iassert.common.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.iassert.interal.IAssert;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


@SuppressWarnings({"rawtypes"})
public class AssertTest extends Test4J {
    @ParameterizedTest
    @MethodSource("assertClass")
    public void wanted(IAssert<?, ?> as, Class claz) {
        want.object(as).propertyEq("valueClaz", claz);
    }

    public static DataProvider assertClass() {
        return new DataProvider() {
            {
                data(the.bool(), Boolean.class);
                data(the.array(), Object[].class);
                data(the.bite(), Byte.class);
                data(the.calendar(), Calendar.class);
                data(the.character(), Character.class);
                data(the.collection(), Collection.class);
                data(the.date(), Date.class);
                data(the.doublenum(), Double.class);
                data(the.file(), File.class);
                data(the.floatnum(), Float.class);
                data(the.integer(), Integer.class);
                data(the.longnum(), Long.class);
                data(the.map(), Map.class);
                data(the.object(), Object.class);
                data(the.shortnum(), Short.class);
                data(the.string(), String.class);
            }
        };
    }

    @Test
    public void wantedMap() {
        want.object(the.map()).propertyEq("valueClaz", Map.class);
    }
}
