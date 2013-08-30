package org.jtester.json.helper;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class JSONScannerTest implements JTester {

    @Test
    @DataFrom("jsonKeysData")
    public void testScanMapKey(String json, String key, boolean quotationMark) {
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        JSONObject value = scanner.scanMapKey();
        want.object(value).notNull()
                .propertyEq(new String[] { "value", "quotationMark" }, new Object[] { key, quotationMark });

        char ch = scanner.nextToken();
        want.character(ch).is(':');
    }

    public static Object[][] jsonKeysData() {
        return new Object[][] { { "\"key1\":\"value1\"", "key1", true },// <br>
                { "key 1:'value1'", "key 1", false }, /** <br> */
                { "'key 1':'value1'", "key 1", true }, /** <br> */
                { "'has \"':'value1'", "has \"", true }, /** <br> */
                { "'has \\'':'value1'", "has '", true }, /** <br> */
                { "'\\u662f\u5426':'是否'", "是否", true }, /** <br> */
                { "是否:'是否'", "是否", false } /** <br> */
        };
    }

    @Test
    @DataFrom("jsonValuesData")
    public void testScanMapValue(String json, String key, boolean quotationMark) {
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        scanner.scanMapKey();
        scanner.nextToken();
        JSONObject value = scanner.scanMapValue();
        want.object(value).notNull()
                .propertyEq(new String[] { "value", "quotationMark" }, new Object[] { key, quotationMark });
    }

    public static Object[][] jsonValuesData() {
        return new Object[][] { { "\"key1\":\"value1\"}", "value1", true },// <br>
                { "key 1:value 1}", "value 1", false }, /** <br> */
                { "'key 1':'value1'}", "value1", true }, /** <br> */
                { "'has \"':'has \"'}", "has \"", true }, /** <br> */
                { "'has \\'':'has \\''}", "has '", true }, /** <br> */
                { "'是否':'\\u662f\u5426'}", "是否", true } /** <br> */
        };
    }

    @Test
    public void testScanJSONMap_Simple() {
        String json = "{'key1':'value1'}";
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        char ch = scanner.nextToken();
        want.character(ch).is('{');
        JSONMap map = scanner.scanJSONMap();
        want.map(map).notNull().hasEntry(JSONSingle.newInstance("key1"), JSONSingle.newInstance("value1"));
    }

    @Test
    public void testScanJSONArray_Simple() {
        String json = "['value1','value2']";
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        char ch = scanner.nextToken();
        want.character(ch).is('[');
        JSONArray array = scanner.scanJSONArray();
        want.collection(array).notNull()
                .hasAllItems(JSONSingle.newInstance("value1"), JSONSingle.newInstance("value2"));
    }

    @Test
    public void testScanJSONMap_complex() {
        String json = "{['value1','value2']:{'key1':'value1'}}";
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        char ch = scanner.nextToken();
        want.character(ch).is('{');
        JSONMap map = scanner.scanJSONMap();
        want.map(map).sizeEq(1);
        JSONObject key = map.keySet().iterator().next();
        want.string(key.toString()).eqIgnoreSpace("[value1,value2]");
    }

    @Test
    public void testScanJSONArray_complex() {
        String json = "[{'key1':'value1'},{'key2':'value2'}]";
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        char ch = scanner.nextToken();
        want.character(ch).is('[');
        JSONArray array = scanner.scanJSONArray();
        want.collection(array).notNull().sizeEq(2);
    }

    @Test
    public void testScanJSONValue() {
        String json = "-0.3e1234";
        JSONScanner scanner = new JSONScanner(json.toCharArray());

        JSONSingle value = scanner.scanJSONValue();
        want.object(value).propertyEq(new String[] { "value", "quotationMark" }, new Object[] { "-0.3e1234", false });
    }

    @Test
    @DataFrom("errorJson")
    public void testScan_error(String json) {
        JSONScanner scanner = new JSONScanner(json.toCharArray());
        try {
            scanner.scan();
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("syntax error");
        }
    }

    public static Object[][] errorJson() {
        return new Object[][] { { "'-0.3e1234' }" },// <br>
                { "['key1','key2'] 1" },// <br>
                { "{key1:'value1'} 1" },// <br>
        };
    }

    @Test
    public void testScanString() {
        String json = "{#class:'[I@123',#value:[1,2]}";
        JSONScanner scanner = new JSONScanner(json.toCharArray());
        JSONMap map = (JSONMap) scanner.scan();
        want.map(map).sizeEq(2);
        String clazzname = ((JSONSingle) map.get("#class")).toStringValue();
        want.string(clazzname).isEqualTo("[I@123");
    }

    /**
     * 测试以[开头的字符串
     */
    @Test
    public void testGetEscapedChar1() {
        String value = JSON.toObject("\\[xxx]");
        want.string(value).isEqualTo("[xxx]");
    }

    /**
     * 测试以{开头的字符串
     */
    @Test
    public void testGetEscapedChar2() {
        String value = JSON.toObject("\\{xxx}");
        want.string(value).isEqualTo("{xxx}");
    }

}
