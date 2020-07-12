package org.test4j.tools.commons;

import org.test4j.json.JSON;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * 字符串处理工具类
 *
 * @author wudarui
 */
public class StringHelper {
    public static final String EMPTY = "";
    /**
     * 双引号
     */
    public static final String DOUBLE_QUOTATION = "\"";

    /**
     * 判断string是否为null或空字符串
     *
     * @param in 输入字符
     * @return true: 空白串
     */
    public static boolean isBlank(String in) {
        if (in == null) {
            return true;
        } else {
            return in.trim().isEmpty();
        }
    }

    /**
     * 非空白串判断
     *
     * @param in 输入字符
     * @return true: 非空白串
     */
    public static boolean isNotBlank(String in) {
        return !isBlank(in);
    }

    public static String trim(String source) {
        if (source == null) {
            return null;
        } else {
            return source.trim();
        }
    }

    /**
     * 返回驼峰命名处理结果字符串
     *
     * @param name
     * @return
     */
    public static String camel(String name) {
        if (StringHelper.isBlank(name)) {
            return "";
        }
        StringBuffer b = new StringBuffer(name.length());
        StringTokenizer t = new StringTokenizer(name);
        b.append(t.nextToken());
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            b.append(token.substring(0, 1).toUpperCase());
            // replace spaces with camelCase
            b.append(token.substring(1));
        }
        return b.toString();
    }

    /**
     * 返回驼峰命名处理结果字符串
     *
     * @param name
     * @param strings
     * @return
     */
    public static String camel(String name, String... strings) {
        StringBuffer b = new StringBuffer(name);
        for (String s : strings) {
            b.append(" ");
            b.append(s);
        }
        return camel(b.toString());
    }

    /**
     * 将原始字符串转义为ascII字符串<br>
     * 例如，原始字符串为：我是中文 <br>
     * 转义后就是:\u6211\u662f\u4e2d\u6587
     *
     * @param nativeStr
     * @return
     */
    public static String native2ascii(final String nativeStr) {
        StringBuffer ret = new StringBuffer();
        if (nativeStr == null) {
            return null;
        }
        int maxLoop = nativeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            char character = nativeStr.charAt(i);
            final int n127 = 127;
            final int n4 = 4;
            if (character <= n127) {
                ret.append(character);
            } else {
                ret.append("\\u");
                String hexStr = Integer.toHexString(character);
                int zeroCount = n4 - hexStr.length();
                for (int j = 0; j < zeroCount; j++) {
                    ret.append('0');
                }
                ret.append(hexStr);
            }
        }
        return ret.toString();
    }

    /**
     * 将转义后的ascII字符串恢复成原始的是字符串<br>
     * 例如，转义后就是:\u6211\u662f\u4e2d\u6587<br>
     * 原始字符串为：我是中文 <br>
     *
     * @param asciiStr
     * @return
     */
    public static String ascii2native(final String asciiStr) {
        if (asciiStr == null) {
            return null;
        }

        StringBuffer retBuf = new StringBuffer();
        int maxLoop = asciiStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (asciiStr.charAt(i) == '\\') {
                final int n5 = 5;
                final int n6 = 6;
                final int n16 = 16;
                if (i < maxLoop - n5 && (asciiStr.charAt(i + 1) == 'u' || asciiStr.charAt(i + 1) == 'U')) {
                    try {
                        retBuf.append((char) Integer.parseInt(asciiStr.substring(i + 2, i + n6), n16));
                        i += n5;
                    } catch (NumberFormatException e) {
                        retBuf.append(asciiStr.charAt(i));
                    }
                } else {
                    retBuf.append(asciiStr.charAt(i));
                }
            } else {
                retBuf.append(asciiStr.charAt(i));
            }
        }

        return retBuf.toString();
    }

    /**
     * <p>
     * Deletes all whitespaces from a String as defined by
     * {@link Character#isWhitespace(char)}.
     * </p>
     *
     * <pre>
     * StringHelper.deleteWhitespace(null)         = null
     * StringHelper.deleteWhitespace("")           = ""
     * StringHelper.deleteWhitespace("abc")        = "abc"
     * StringHelper.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str the String to delete whitespace from, may be null
     * @return the String without whitespaces, <code>null</code> if null String
     * input
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     *
     * <pre>
     * StringHelper.isEmpty(null)      = true
     * StringHelper.isEmpty("")        = true
     * StringHelper.isEmpty(" ")       = false
     * StringHelper.isEmpty("bob")     = false
     * StringHelper.isEmpty("  bob  ") = false
     * </pre>
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * String. That functionality is available in isBlank().
     * </p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 忽略字符串中所有的空白符
     *
     * @param str
     * @return
     */
    public static String ignoreWhiteSpace(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private final static boolean[] Space_Chars = new boolean[256];

    static {
        Space_Chars[' '] = true;
        Space_Chars['\n'] = true;
        Space_Chars['\r'] = true;
        Space_Chars['\t'] = true;
        Space_Chars['\f'] = true;
        Space_Chars['\b'] = true;
    }

    /**
     * 是否是空白字符串
     *
     * @param ch
     * @return
     */
    public final static boolean isSpace(char ch) {
        return ch > 255 ? false : Space_Chars[ch];
    }

    /**
     * 将exception转为string输出
     *
     * @param e
     * @return
     */
    public static String toString(Throwable e) {
        return toString(e, null);
    }

    /**
     * 把exception的trace信息写到string中,同时过滤掉filters中指定的信息
     *
     * @param e
     * @param filters
     * @return 返回异常信息的序列化字符串
     */
    public static String toString(Throwable e, List<String> filters) {
        if (e == null) {
            return "<error is null>";
        }
        StringWriter w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        String tracer = w.toString();
        if (filters != null) {
            for (String regex : filters) {
                tracer = tracer.replaceAll(regex, "");
            }
        }
        return tracer;
    }

    /**
     * 将换行符替换为空格
     *
     * @param original
     * @return
     */
    public static String removeBreakingWhiteSpace(String original) {
        StringTokenizer whiteSpaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whiteSpaceStripper.hasMoreTokens()) {
            builder.append(whiteSpaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * 按照分隔符split把strings拼接起来
     *
     * @param split
     * @param strings
     * @return
     */
    public static String join(String start, String split, String[] strings, String end) {
        return new StringBuilder(start)
            .append(Arrays.stream(strings).collect(joining(split)))
            .append(end)
            .toString();
    }

    /**
     * json化处理，同时去掉双引号
     *
     * @param value
     * @return
     */
    public static String toJsonString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number || value instanceof String) {
            return String.valueOf(value);
        }
        String text = JSON.toJSON(value, false);
        if (text.length() > 2 && text.startsWith(DOUBLE_QUOTATION) && text.endsWith(DOUBLE_QUOTATION)) {
            return text.substring(1, text.length() - 1);
        } else {
            return text;
        }
    }
}
