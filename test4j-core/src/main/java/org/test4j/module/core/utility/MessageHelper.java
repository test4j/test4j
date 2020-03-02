package org.test4j.module.core.utility;

import org.test4j.tools.commons.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import static org.test4j.module.core.internal.IPropItem.LOG4J_XML_FILE;

public class MessageHelper {
    public static final int DEBUG   = 0;

    public static final int INFO    = 1;

    public static final int WARNING = 2;

    public static final int ERROR   = 3;

    public static int       level   = DEBUG;

    private static void mark(int level, String marker) {
        switch (level) {
            case ERROR:
                System.err.println(marker);
                break;
            case WARNING:
            default:
                System.out.println(marker);
        }
    }

    public static void debug(Object info, Throwable... throwables) {
        if (level <= DEBUG) {
            mark(DEBUG, "DEBUG: " + String.valueOf(info));
            printExceptions(throwables);
        }
    }

    public static void warn(Object warn, Throwable... throwables) {
        if (level <= WARNING) {
            mark(WARNING, "WARNING: " + warn);
            printExceptions(throwables);
        }
    }

    public static void info(Object info, Throwable... throwables) {
        if (level <= INFO) {
            mark(INFO, "INFO: " + String.valueOf(info));
            printExceptions(throwables);
        }
    }

    public static void error(Object err, Throwable... throwables) {
        mark(ERROR, "ERROR: " + String.valueOf(err));
        printExceptions(throwables);
    }

    /**
     * 重置log4j的设置
     */
    @SuppressWarnings("rawtypes")
    public static void resetLog4jLevel() {
        String log4jxml = ConfigHelper.getString(LOG4J_XML_FILE);

        boolean log4jAvailable = ClazzHelper.isClassAvailable("org.apache.log4j.xml.DOMConfigurator");
        if (StringHelper.isBlankOrNull(log4jxml) || log4jAvailable == false) {
            return;
        }
        try {
            URL url = ResourceHelper.getResourceUrl(log4jxml);
            Class domConfigurator = ClazzHelper.getClazz("org.apache.log4j.xml.DOMConfigurator");
            MethodHelper.invokeStatic(domConfigurator, "configure", url);
        } catch (Throwable e) {
            mark(ERROR, "reset log4j leve error, " + e == null ? "null" : e.getMessage());
        }
    }

    private static File debugFile = new File(System.getProperty("user.dir") + "/target/test4j.log");

    /**
     * 用于记录test4j运行时的信息，方便定位复杂的问题<br>
     * 正式发布版本中所有方法都改为protected，禁止其它类引用
     * 
     * @author darui.wudr
     */
    protected static void writerDebugInfo(String info) {
        Writer writer = null;
        try {
            writer = new FileWriter(debugFile, true);
            writer.write(info);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printExceptions(Throwable[] throwables) {
        if (throwables == null || throwables.length == 0) {
            return;
        }
        for (Throwable e : throwables) {
            e.printStackTrace();
        }
    }
}
