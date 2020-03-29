package org.test4j.tools.commons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.junit5.Test4J;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class ListHelperTest extends Test4J {

    @Test
    public void testToList() {
        List list = ListHelper.toList(1, 2, 3);
        want.collection(list).eqReflect(new Integer[]{1, 2, 3});
    }

    @ParameterizedTest
    @MethodSource("testToList_data")
    public void testToList_Object(Object input, List output) {
        List list = ListHelper.toList(input);
        want.collection(list).eqReflect(output);
    }

    public static Object[][] testToList_data() {
        return new Object[][]{{Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)},// <br>
                {new Integer[]{1, 2, 3}, Arrays.asList(1, 2, 3)}, // <br>
                {1, Arrays.asList(1)}, // <br>
                {null, Arrays.asList((Object) null)}, // <br>
                {new Integer[]{1, 2, 3}, Arrays.asList(1, 2, 3)} // <br>
        };
    }

    @Test
    public void testToListMulti() {
        List list = ListHelper.toList(1, 2, 3);
        want.collection(list).sizeEq(3).hasAllItems(1, 2, 3);

        list = ListHelper.toList();
        want.collection(list).sizeEq(0);
    }

    @Test
    public void testToList_WithMap() {
        List list = ListHelper.toList(new HashMap() {
            {
                this.put(1, 1);
                this.put(2, 2);
                this.put(3, 3);
            }
        }, true);
        want.collection(list).sizeEq(3).eqReflect(ArrayHelper.toArray(1, 2, 3), EqMode.IGNORE_ORDER);
    }
}
