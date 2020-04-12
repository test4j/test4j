package org.test4j.tools.datagen;

import lombok.Data;
import org.test4j.json.JSON;

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
public class TableData extends HashMap<String, List<Map<String, String>>> {
    /**
     * 使用data数据覆盖对应的表
     *
     * @param table
     * @param data
     * @return
     */
    TableData with(String table, IDataMap data) {
        int row = 0;
        for (Map<String, String> map : this.get(table)) {
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
    public static TableData map(String json) {
        return JSON.toObject(json, TableData.class);
    }
}
