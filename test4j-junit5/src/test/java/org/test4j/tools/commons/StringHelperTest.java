package org.test4j.tools.commons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

public class StringHelperTest extends Test4J {

    @ParameterizedTest
    @MethodSource("testCamel_Data")
    public void testCamel(String name, String camel) {
        String value = StringHelper.camel(name);
        want.string(value).isEqualTo(camel);
    }

    public static Object[][] testCamel_Data() {
        return new String[][]{{"is a word", "isAWord"},// <br>
                {"get an Word", "getAnWord"},// <br>
                {"get\t an wWOd", "getAnWWOd"},// <br>
                {"", ""},// <br>
                {"Get an word", "GetAnWord"} // <br>
        };
    }

    @Test
    public void testCamel() {
        String value = StringHelper.camel("is", "an", "word");
        want.string(value).isEqualTo("isAnWord");
    }

    @Test
    public void testNative2ascii() {
        String zh = "我是中文";
        String unicode = StringHelper.native2ascii(zh);
        want.string(unicode).isEqualTo("\\u6211\\u662f\\u4e2d\\u6587");
    }

    @Test
    public void testAscii2native() {
        String unicode = "\\u6211\\u662f\\u4e2d\\u6587";
        String zh = StringHelper.ascii2native(unicode);
        want.string(zh).isEqualTo("我是中文");
    }

    @ParameterizedTest
    @MethodSource("dataOfWhiteSpace")
    public void testIgnoreWhiteSpace(String input, String expected) {
        String output = StringHelper.ignoreWhiteSpace(input);
        want.string(output).isEqualTo(expected);
    }

    public static Object[][] dataOfWhiteSpace() {
        return new Object[][]{{" ", ""},// <br>
                {"", ""}, /** <br> */
                {null, null}, /** <br> */
                {" i \ram\n str \t .end", "iamstr.end"} /** <br> */
        };
    }
}
