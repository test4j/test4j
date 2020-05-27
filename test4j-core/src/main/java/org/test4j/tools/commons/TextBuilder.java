package org.test4j.tools.commons;

import org.test4j.function.SExecutor;

/**
 * TextBuilder
 *
 * @author darui.wu
 * @create 2020/4/24 6:05 下午
 */
public class TextBuilder {
    public static final String NEWLINE = "\n";

    private final StringBuilder buff = new StringBuilder();

    private boolean endNewLine = false;

    public static TextBuilder build() {
        return new TextBuilder();
    }

    public TextBuilder append(TextBuilder appender) {
        buff.append(appender.toString());
        return this;
    }

    /**
     * 追加字符串
     *
     * @param format
     * @param args
     * @return
     */
    public TextBuilder append(String format, Object... args) {
        if (args == null || args.length == 0) {
            buff.append(format);
        } else {
            buff.append(String.format(format, args));
        }
        this.endNewLine = false;
        return this;
    }

    public TextBuilder append(SExecutor executor) {
        try {
            executor.doIt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * 替换单引号为双引号
     *
     * @param input
     * @return
     */
    private static String replace(String input) {
        return input.replace('\'', '"');
    }

    /**
     * 追加字符串, 但把format中单引号替换为双引号
     *
     * @param format
     * @param args
     * @return
     */
    public TextBuilder quotas(String format, Object... args) {
        if (args == null || args.length == 0) {
            buff.append(replace(format));
        } else {
            buff.append(String.format(replace(format), args));
        }
        this.endNewLine = false;
        return this;
    }

    /**
     * 追加换行符
     *
     * @return
     */
    public TextBuilder newLine() {
        if (!this.endNewLine) {
            buff.append(NEWLINE);
        }
        this.endNewLine = true;
        return this;
    }

    /**
     * 追加换行符
     *
     * @return
     */
    public TextBuilder newLine(int count) {
        for (int loop = 0; loop < count; loop++) {
            buff.append(NEWLINE);
            this.endNewLine = true;
        }
        return this;
    }

    @Override
    public String toString() {
        return this.buff.toString();
    }
}