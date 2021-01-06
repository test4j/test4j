package org.test4j.tools.datagen;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.test4j.tools.commons.ResourceHelper;
import org.test4j.tools.json.JSON;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toSet;
import static org.test4j.module.ICore.DataMap;

/**
 * 数据库表数据
 * key: 表名称, value: 表数据
 *
 * @author wudarui
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TableMap extends LinkedHashMap<String, TableData> {
    /**
     * json转换为TableMap
     *
     * @param json
     * @return
     */
    public static TableMap fromText(String json) {
        TableMap data = JSON.toObject(json, TableMap.class);
        return data;
    }


    /**
     * 从文件读取
     *
     * @param packClass
     * @param fileName
     * @return
     */
    public static TableMap fromFile(Class packClass, String fileName) {
        try {
            String text = ResourceHelper.readFromFile(packClass, fileName);
            return fromText(text);
        } catch (Throwable e) {
            throw new RuntimeException("read json text error:" + e.getMessage(), e);
        }
    }

    /**
     * 初始化数据
     *
     * @param init   初始化数据
     * @param skip   当表不存在时，是否跳过
     * @param tables 指定表
     * @return
     */
    public TableMap initWith(IDataMap init, boolean skip, String... tables) {
        Set<String> all = tables == null || tables.length == 0 ? this.keySet() : Arrays.stream(tables).collect(toSet());
        for (String table : all) {
            if (this.containsKey(table)) {
                this.get(table).setInit((AbstractDataMap) init);
            } else if (!skip) {
                this.put(table, new TableData().setInit((AbstractDataMap) init));
            }
        }
        return this;
    }

    /**
     * 返回table表的DataMap数据
     *
     * @param table
     * @return
     */
    public IDataMap dataMap(String table) {
        return this.get(table).findDataMap();
    }

    /**
     * 对数据执行consumer操作
     *
     * @param consumer
     * @param skip     跳过不存在的数据操作
     * @param tables
     * @return
     */
    public TableMap apply(Consumer<AbstractDataMap> consumer, boolean skip, String... tables) {
        Set<String> all = tables == null || tables.length == 0 ? this.keySet() : Arrays.stream(tables).collect(toSet());
        for (String table : all) {
            TableData data = this.get(table);
            if (data == null && skip) {
                continue;
            }
            consumer.accept((AbstractDataMap) data.findDataMap());
        }
        return this;
    }
}
