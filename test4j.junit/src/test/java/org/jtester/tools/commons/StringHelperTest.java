package org.jtester.tools.commons;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class StringHelperTest implements JTester {

    @Test
    public void testJoin_Null() {
        String str = StringHelper.join(",", null);
        want.string(str).isEqualTo("");
    }

    @Test
    public void testJoin_Empty() {
        String str = StringHelper.join(",", new String[] {});
        want.string(str).isEqualTo("");
    }

    @Test
    public void testJoin_OneItem() {
        String str = StringHelper.join(",", new String[] { "one" });
        want.string(str).isEqualTo("one");
    }

    @Test
    public void testJoin_MultiItem() {
        String str = StringHelper.join(",", new String[] { "one", null });
        want.string(str).isEqualTo("one,null");
    }

    @Test
    @DataFrom("testCamel_Data")
    public void testCamel(String name, String camel) {
        String value = StringHelper.camel(name);
        want.string(value).isEqualTo(camel);
    }

    public static Object[][] testCamel_Data() {
        return new String[][] { { "is a word", "isAWord" },// <br>
                { "get an Word", "getAnWord" },// <br>
                { "get\t an wWOd", "getAnWWOd" },// <br>
                { "", "" },// <br>
                { "Get an word", "GetAnWord" } // <br>
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

    @Test
    @DataFrom("dataOfWhiteSpace")
    public void testIgnoreWhiteSpace(String input, String expected) {
        String output = StringHelper.ignoreWhiteSpace(input);
        want.string(output).isEqualTo(expected);
    }

    public static Object[][] dataOfWhiteSpace() {
        return new Object[][] { { " ", "" },// <br>
                { "", "" }, /** <br> */
                { null, null }, /** <br> */
                { " i \ram\n str \t .end", "iamstr.end" } /** <br> */
        };
    }
}
