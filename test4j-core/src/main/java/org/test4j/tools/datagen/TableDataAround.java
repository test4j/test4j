package org.test4j.tools.datagen;

import lombok.Data;
import org.test4j.exception.ExtraMessageError;
import org.test4j.json.JSON;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spec.internal.StepResult;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ResourceHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
    private TableData ready;
    /**
     * 检查数据
     */
    private TableData check;

    /**
     * 将data中的json字段重新序列化为string
     *
     * @param data
     */
    public static void reset(TableData data) {
        if (data == null) {
            return;
        }
        for (List<Map<String, Object>> list : data.values()) {
            for (Map<String, Object> map : list) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof Map || ArrayHelper.isCollOrArray(value)) {
                        entry.setValue(JSON.toJSON(value, false));
                    }
                }
            }
        }
    }

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
        reset(around.getReady());
        reset(around.getCheck());
        THREAD_LOCAL_AROUND.set(around);
        return around;
    }

    public static void ready(StepResult result) throws Throwable {
        if (THREAD_LOCAL_AROUND.get() == null || THREAD_LOCAL_AROUND.get().getReady() == null) {
            return;
        }
        try {
            TableData tableData = THREAD_LOCAL_AROUND.get().getReady();
            String extra = IDatabase.db.insert(tableData, false);
            result.extraMessage(extra);
        } catch (ExtraMessageError e) {
            result.extraMessage(e.getExtra());
            TableDataAround.remove();
            throw e.getCause();
        }
    }

    public static void check(StepResult result) throws Throwable {
        if (THREAD_LOCAL_AROUND.get() == null || THREAD_LOCAL_AROUND.get().getCheck() == null) {
            return;
        }
        try {
            TableData tableData = THREAD_LOCAL_AROUND.get().getCheck();
            String extra = IDatabase.db.queryEq(tableData);
            result.extraMessage(extra);
        } catch (ExtraMessageError e) {
            result.extraMessage(e.getExtra());
            throw e.getCause();
        } finally {
            TableDataAround.remove();
        }
    }

    private static void remove() {
        THREAD_LOCAL_AROUND.remove();
    }

    public static void initReady(IDataMap data, String... tables) {
        if (THREAD_LOCAL_AROUND.get() == null || THREAD_LOCAL_AROUND.get().getReady() == null) {
            return;
        }
        initCheck(() -> THREAD_LOCAL_AROUND.get().getReady(), data, tables);
    }

    public static void initCheck(IDataMap data, String... tables) {
        if (THREAD_LOCAL_AROUND.get() == null) {
            return;
        }
        initCheck(() -> THREAD_LOCAL_AROUND.get().getCheck(), data, tables);
    }

    static void initCheck(Supplier<TableData> supplier, IDataMap data, String... tables) {
        if (THREAD_LOCAL_AROUND.get() == null) {
            return;
        }
        TableData tableData = supplier.get();
        if (tableData == null) {
            return;
        }
        if (tables == null || tables.length == 0) {
            for (String table : tableData.keySet()) {
                tableData.with(table, data);
            }
        } else {
            for (String table : tables) {
                tableData.with(table, data);
            }
        }
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
        String file1 = path + "." + method + ".json";
        if (ClassLoader.getSystemResource(file1) != null) {
            return file1;
        }
        String file2 = path + "/" + method + ".json";
        if (ClassLoader.getSystemResource(file2) != null) {
            return file2;
        }
        throw new RuntimeException(String.format("\n\tFile: %s \nor \tFile: %s \nnot found in classpath.", file1, file2));
    }
}