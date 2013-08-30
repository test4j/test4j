package org.jtester.json.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jtester.junit.JTester;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class InnerJSONMapTest implements JTester {
    /**
     * 验证JSONMap数据的有序性,JSONMap必须是LinkedHashMap而不能是HashMap
     */
    @Test
    public void testLinkedHashMap() {
        JSONMap map = new JSONMap();
        map.putJSON("a3", "aa");
        map.putJSON("a2", "bb");
        map.putJSON("b1", "cc");
        StringBuffer buff = new StringBuffer();
        for (Iterator<JSONObject> iterator = map.values().iterator(); iterator.hasNext();) {
            JSONSingle single = (JSONSingle) iterator.next();
            buff.append(single.toStringValue());
        }
        String result = buff.toString();
        want.string(result).isEqualTo("aabbcc");
    }

    @Test
    public void testHashMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a3", "aa");
        map.put("a2", "bb");
        map.put("b1", "cc");
        StringBuffer buff = new StringBuffer();
        for (Iterator iterator = map.values().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            buff.append(name);
        }
        want.string(buff.toString()).notEqualTo("aabbcc");
    }
}
