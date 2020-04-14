package org.test4j.tools.datagen;

import lombok.Data;
import org.test4j.json.JSON;
import org.test4j.tools.commons.ResourceHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库表数据
 * key: 表名称, value: 表数据
 *
 * @author wudarui
 */
@Data
public class TableData extends HashMap<String, List<Map<String, Object>>> {
    /**
     * 使用data数据覆盖对应的表
     *
     * @param table
     * @param data
     * @return
     */
    TableData with(String table, IDataMap data) {
        int row = 0;
        for (Map<String, Object> map : this.get(table)) {
            map.putAll(data.row(row));
            row++;
        }
        return this;
    }

    /**
     * json转换为TableMap
     *
     * @param json
     * @return
     */
    public static TableData fromText(String json) {
        TableData data = JSON.toObject(json, TableData.class);
        TableDataAround.reset(data);
        return data;
    }


    /**
     * 从文件读取
     *
     * @param packClass
     * @param fileName
     * @return
     */
    public static TableData fromFile(Class packClass, String fileName) {
        try {
            String text = ResourceHelper.readFromFile(packClass, fileName);
            return fromText(text);
        } catch (Throwable e) {
            throw new RuntimeException("read json text error:" + e.getMessage(), e);
        }
    }

    /**
     * 从文件读取
     *
     * @param fileName
     * @return
     */
    public static TableData fromFile(String fileName) {
        try {
            String text = ResourceHelper.readFromFile(fileName);
            return fromText(text);
        } catch (Throwable e) {
            throw new RuntimeException("read json text error:" + e.getMessage(), e);
        }
    }
}
