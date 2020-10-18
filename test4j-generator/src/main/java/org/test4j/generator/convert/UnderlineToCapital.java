package org.test4j.generator.convert;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnderlineToCapital {
    public static void convertPath(String path) {
        convertPath(new File(path));
    }

    /**
     * 转换目录
     *
     * @param path
     */
    public static void convertPath(File path) {
        if (!path.isDirectory() || !path.exists()) {
            Util.info("文件夹不存在");
            return;
        }
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                convertFile(file);
                Util.info("转换文件[" + file.getName() + "]成功");
            } else if (file.isDirectory()) {
                convertPath(file);
            }
        }
    }

    /**
     * 转换文件
     *
     * @param file
     * @return
     */
    public static String convertFile(File file) {
        String[] lines = ResourceHelper.readLinesFromFile(file);

        StringBuilder buff = new StringBuilder();
        for (String line : lines) {
            buff.append(convertLine(line));
            buff.append("\n");
        }
        String value = buff.toString();
        ResourceHelper.writeStringToFile(file, value);
        return value;
    }

    private static String reg = "(.*\\.)(\\w+_[\\w\\d_]+)(\\.(values|formatAutoIncrease|autoIncrease)\\(.*\\)+;?(.*))";

    private static Pattern pattern = Pattern.compile(reg);

    public static String convertLine(String line) {
        Matcher m = pattern.matcher(line);
        if (!m.matches()) {
            return line;
        }
        return new StringBuffer()
            .append(m.group(1))
            .append(underlineToCapital(m.group(2)))
            .append(m.group(3))
            .toString();
    }

    public static String underlineToCapital(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder buff = new StringBuilder();
        boolean isUnderline = false;
        for (char ch : input.toCharArray()) {
            if (ch == '_') {
                isUnderline = true;
                continue;
            }
            buff.append(isUnderline ? String.valueOf(ch).toUpperCase() : ch);
            isUnderline = false;
        }
        return buff.toString();
    }
}