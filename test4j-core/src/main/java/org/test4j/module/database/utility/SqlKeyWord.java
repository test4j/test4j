package org.test4j.module.database.utility;

import java.util.HashMap;
import java.util.List;

import static org.test4j.tools.commons.ListHelper.toList;

/**
 * SqlKeyWord
 *
 * @author darui.wu
 * @create 2019/11/7 11:17 上午
 */
public class SqlKeyWord {
    public static List<ItemBetweenKey> Table_Between = toList(
            new ItemBetweenKey("from", "where"),
            new ItemBetweenKey("update", "set"),
            new ItemBetweenKey("into", "(")
    );

    public static List<ItemBetweenKey> Where_Between = toList(
            new ItemBetweenKey("where", "limit", "order", "group", "having", "select", "union")
    );

    public static List<ItemBetweenKey> Select_Between = toList(
            new ItemBetweenKey("select", "from")
    );

    /**
     * 是否匹配开始符
     *
     * @param betweenKeys
     * @param word
     * @return
     */
    public static List<String> isMatchBegin(List<ItemBetweenKey> betweenKeys, String word) {
        String lowerCase = word.toLowerCase();
        for (ItemBetweenKey betweenKey : betweenKeys) {
            if (betweenKey.containsKey(lowerCase)) {
                return betweenKey.get(lowerCase);
            }
        }
        return null;
    }

    /**
     * 是否匹配结尾符
     *
     * @param endKeys
     * @param word
     * @return
     */
    public static boolean isMatchEnd(List<String> endKeys, String word) {
        if (endKeys == null) {
            return false;
        }
        String lowerCase = word.toLowerCase();
        for (String endKey : endKeys) {
            if (lowerCase.startsWith(endKey)) {
                return true;
            }
        }
        return false;
    }

    public static class ItemBetweenKey extends HashMap<String, List<String>> {
        public ItemBetweenKey(String begin, String... ends) {
            this.put(begin, toList(ends));
        }
    }
}