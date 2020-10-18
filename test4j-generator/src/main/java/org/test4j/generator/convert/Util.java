package org.test4j.generator.convert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void info(String info) {
        System.out.println(info);
    }

    /**
     * 未定义
     */
    public final static String NOT_DEFINED = "$$NOT_DEFINED$$";

    public static boolean isBlank(String in) {
        if (in == null) {
            return true;
        } else {
            return in.trim().isEmpty();
        }
    }

    public static String[] readLinesFromFile(File file) {
        try {
            try (InputStream stream = new FileInputStream(file);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line = null;
                List<String> list = new ArrayList<String>();
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                return list.toArray(new String[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeStringToFile(File file, String content) {
        File path = file.getParentFile();
        if (path.exists() == false) {
            path.mkdirs();
        }
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
