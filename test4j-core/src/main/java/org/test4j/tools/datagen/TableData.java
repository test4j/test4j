package org.test4j.tools.datagen;

import lombok.experimental.Accessors;
import org.test4j.json.JSON;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.commons.ArrayHelper;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * 单个表数据
 *
 * @author darui.wu
 * @create 2020/4/15 3:11 下午
 */
@Accessors(chain = true)
public class TableData extends ArrayList<Map<String, Object>> {

    /**
     * 用来初始化所有record记录
     */
    private DataMap init;

    private DataMap data;

    public TableData() {
    }

    public TableData(List<Map<String, Object>> data) {
        this.addAll(data);
    }

    public TableData setInit(AbstractDataMap init) {
        if (this.data != null) {
            throw new RuntimeException("the init DataMap should set first.");
        }
        if (this.init == null) {
            this.init = new DataMap(1);
        }
        for (Map.Entry<String, IColData> entry : (Set<Map.Entry>) init.entrySet()) {
            this.init.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public IDataMap findDataMap() {
        if (data == null) {
            this.data = DataMap.create(this.size());

            Set<String> allKeys = this.stream().flatMap(map -> map.keySet().stream()).collect(toSet());
            for (int index = 0; index < this.size(); index++) {
                Map<String, Object> data = this.get(index);
                Map<String, Object> row = init == null ? new HashMap<>() : init.row(index);
                allKeys.forEach(key -> row.put(key, data.get(key)));
                row.entrySet().stream().forEach(entry -> addData(entry.getKey(), entry.getValue()));
            }
        }
        return this.data;
    }

    public List<Map<String, Object>> findDataList() {
        return this.findDataMap().rows();
    }


    private void addData(String key, Object value) {
        Object _value = value;
        if (value instanceof Map || ArrayHelper.isCollOrArray(value)) {
            _value = JSON.toJSON(value, false);
        }
        if (this.data.containsKey(key)) {
            this.data.get(key).add(_value);
        } else {
            this.data.kv(key, _value);
        }
    }
}
