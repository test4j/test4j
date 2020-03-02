package org.test4j.module.database.utility;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static java.util.stream.Collectors.toList;
import static org.test4j.tools.commons.StringHelper.isSpace;

/**
 * SQL语法高亮显示器
 *
 * @author darui.wu
 */
public class SQLUtility {

    /**
     * 获取sql语句中的table
     *
     * @param sql
     * @return
     */
    public static String parseTable(String sql) {
        return split(sql, SqlKeyWord.Table_Between);
    }

    /**
     * 解析sql语句where之后的语句
     *
     * @param sql
     * @return
     */
    public static String parseWhere(String sql) {
        return split(sql, SqlKeyWord.Where_Between);
    }

    /**
     * 解析select 字段列表
     *
     * @param sql
     * @return
     */
    public static String[] parseSelect(String sql) {
        String fields = split(sql, SqlKeyWord.Select_Between);
        return Arrays.stream(fields.split(","))
                .map(String::trim)
                .collect(toList())
                .toArray(new String[0]);
    }

    public static String split(String sql, List<SqlKeyWord.ItemBetweenKey> betweenKeys) {
        StringTokenizer tokenizer = new StringTokenizer(sql);
        StringBuilder buff = new StringBuilder();
        boolean hasMatchBegin = false;
        List<String> ends = null;
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if (!hasMatchBegin) {
                ends = SqlKeyWord.isMatchBegin(betweenKeys, word);
                if (ends != null) {
                    hasMatchBegin = true;
                    continue;
                }
            }
            if (SqlKeyWord.isMatchEnd(ends, word)) {
                break;
            }
            if (hasMatchBegin) {
                buff.append(word).append(" ");
            }
        }
        return buff.toString().trim();
    }

    /**
     * 过滤sql语句中多余的空格
     *
     * @param sql
     * @return
     */
    public static String filterSpace(String sql) {
        boolean inSingleQuotation = false;
        boolean inDoubleQuotation = false;
        StringBuffer buff = new StringBuffer();
        char preChar = 0;
        for (char ch : sql.toCharArray()) {
            if (isSpace(ch)) {
                if (inDoubleQuotation || inDoubleQuotation) {
                    buff.append(ch);
                } else {
                    buff.append(isSpace(preChar) ? "" : " ");
                }
            } else {
                buff.append(ch);
            }
            if (ch == '"' && preChar != '\\') {
                inDoubleQuotation = inDoubleQuotation ? false : !inSingleQuotation;
            }
            if (ch == '\'' && preChar != '\\') {
                inSingleQuotation = inSingleQuotation ? false : !inDoubleQuotation;
            }
            preChar = ch;
        }
        return buff.toString();
    }
}