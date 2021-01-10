package org.test4j.module.spec.internal;

import lombok.Data;
import org.test4j.module.database.IDatabase;
import org.test4j.module.database.datagen.TableMap;
import org.test4j.tools.commons.ResourceHelper;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.exception.ExtraMessageError;
import org.test4j.tools.json.JSON;

import java.util.function.Consumer;

/**
 * 准备和校验数据库数据
 *
 * @author darui.wu
 * @create 2020/4/14 3:15 下午
 */
@Data
public class TableDataAround {
    /**
     * 初始化数据
     */
    private TableMap ready;
    /**
     * 检查数据
     */
    private TableMap check;

    static ThreadLocal<TableDataAround> THREAD_LOCAL_AROUND = new ThreadLocal<>();

    public static TableDataAround around(String file) {
        THREAD_LOCAL_AROUND.remove();
        String text = ResourceHelper.readFromClasspath(file);
        TableDataAround around = JSON.toObject(text, TableDataAround.class);
        if (around.getReady() == null) {
            throw new RuntimeException("not found ready data in file:" + file);
        }
        if (around.getCheck() == null) {
            throw new RuntimeException("not found check data in file:" + file);
        }
        THREAD_LOCAL_AROUND.set(around);
        return around;
    }

    public static void ready(StepResult result) throws Throwable {
        if (THREAD_LOCAL_AROUND.get() == null || THREAD_LOCAL_AROUND.get().getReady() == null) {
            return;
        }
        try {
            TableMap tableMap = THREAD_LOCAL_AROUND.get().getReady();
            String extra = IDatabase.db.insert(tableMap, false);
            result.extraMessage(extra);
        } catch (ExtraMessageError e) {
            result.extraMessage(e.getExtra());
            throw e.getCause();
        }
    }

    public static void check(StepResult result) throws Throwable {
        if (THREAD_LOCAL_AROUND.get() == null || THREAD_LOCAL_AROUND.get().getCheck() == null) {
            return;
        }
        try {
            TableMap tableMap = THREAD_LOCAL_AROUND.get().getCheck();
            String extra = IDatabase.db.queryEq(tableMap);
            result.extraMessage(extra);
        } catch (ExtraMessageError e) {
            result.extraMessage(e.getExtra());
            throw e.getCause();
        }
    }

    public static void remove() {
        try {
            THREAD_LOCAL_AROUND.remove();
        } catch (Throwable e) {
        }
    }

    public static void initReady(Consumer<TableMap> handler) {
        if (THREAD_LOCAL_AROUND.get() == null || handler == null) {
            return;
        }
        handler.accept(THREAD_LOCAL_AROUND.get().getReady());
    }

    public static void initCheck(Consumer<TableMap> handler) {
        if (THREAD_LOCAL_AROUND.get() == null || handler == null) {
            return;
        }
        handler.accept(THREAD_LOCAL_AROUND.get().getCheck());
    }

    public static void initReady(IDataMap data, String... tables) {
        if (THREAD_LOCAL_AROUND.get() == null || data == null) {
            return;
        }
        THREAD_LOCAL_AROUND.get().getReady().initWith(data, false, tables);
    }

    public static void initCheck(IDataMap data, String... tables) {
        if (THREAD_LOCAL_AROUND.get() == null || data == null) {
            return;
        }
        THREAD_LOCAL_AROUND.get().getCheck().initWith(data, true, tables);
    }

    /**
     * 在classpath下查找同名json文件
     *
     * @param klass
     * @param method
     * @return
     * @throws Exception
     */
    public static String findFile(Class klass, String method) {
        String path = klass.getName().replace('.', '/');
        String file1 = path + "/" + method + ".json";
        if (ClassLoader.getSystemResource(file1) != null) {
            return file1;
        }
        String file2 = path + "." + method + ".json";
        if (ClassLoader.getSystemResource(file2) != null) {
            return file2;
        }
        throw new RuntimeException(String.format("\n\tFile: %s \nor \tFile: %s \nnot found in classpath.", file1, file2));
    }
}