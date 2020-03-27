package org.test4j.hamcrest.matcher.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

public class StringModeTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data_IgnoreCase")
    public void testProcess_IgnoreCase(String input, String output) {
        StringMode mode = StringMode.IgnoreCase;
        String result = mode.process(input);
        want.string(result).isEqualTo(output);
    }

    public static Object[][] data_IgnoreCase() {
        return new Object[][]{{null, null}, // <br>
                {"ab\tc", "ab\tc"}, // <br>
                {"Ab C", "ab c"}};
    }

    @ParameterizedTest
    @MethodSource("data_IgnoreSpace")
    public void testProcess_IgnoreSpace(String input, String output) {
        StringMode mode = StringMode.IgnoreSpace;
        String result = mode.process(input);
        want.string(result).isEqualTo(output);
    }

    public static Object[][] data_IgnoreSpace() {
        return new Object[][]{{null, null}, // <br>
                {" ab\tc\n", "abc"}, // <br>
                {"Abc", "Abc"}};
    }

    @ParameterizedTest
    @MethodSource("data_IgnoreQuato")
    public void testProcess_IgnoreQuato(String input, String output) {
        StringMode mode = StringMode.IgnoreQuato;
        String result = mode.process(input);
        want.string(result).isEqualTo(output);
    }

    public static Object[][] data_IgnoreQuato() {
        return new Object[][]{{null, null}, // <br>
                {"\"abc\"", "abc"}, // <br>
                {"'abc'", "abc"}};
    }

    @ParameterizedTest
    @MethodSource("data_SameAsSpace")
    public void testProcess_SameAsSpace(String input, String output) {
        StringMode mode = StringMode.SameAsSpace;
        String result = mode.process(input);
        want.string(result).isEqualTo(output);
    }

    public static Object[][] data_SameAsSpace() {
        return new Object[][]{{null, null}, // <br>
                {"\n\ra b\tc    ", " a b c "}, // <br>
                {"abc", "abc"}};
    }

    @ParameterizedTest
    @MethodSource("data_SameAsQuato")
    public void testProcess_SameAsQuato(String input, String output) {
        StringMode mode = StringMode.SameAsQuato;
        String result = mode.process(input);
        want.string(result).isEqualTo(output);
    }

    public static Object[][] data_SameAsQuato() {
        return new Object[][]{{null, null}, // <br>
                {"\"abc\"", "'abc'"}, // <br>
                {"'abc'", "'abc'"}};
    }

    @Test
    public void testGetStringByMode() {
        String input = "abc \t 'abc'\n\r\"ABC\"      ";
        String actual = StringMode.getStringByMode(input, StringMode.IgnoreQuato, StringMode.SameAsSpace,
                StringMode.IgnoreCase);
        want.string(actual).isEqualTo("abc abc abc ");
    }

    @Test
    public void testGetStringByMode_StringIsNull() {
        String result = StringMode.getStringByMode(null, StringMode.IgnoreQuato);
        want.string(result).isNull();
    }

    @Test
    public void testGetStringByMode_NoStringMode() {
        String input = "abc \t 'abc'\n\r\"ABC\"      ";
        String actual = StringMode.getStringByMode(input);
        want.string(actual).isEqualTo(input);
    }

    @Test
    public void testSameAsSlash() {
        String actual = "d:/abc\\e/1.txt";
        want.string(actual).eq("d:/abc/e/1.txt", StringMode.SameAsSlash);
    }
}
