package org.test4j.hamcrest.diff;

import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.json.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 差异项列表
 *
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class DiffMap {
    int diff = 0;
    Map<String, Object> message = new HashMap<>();

    public DiffMap add(Object key, Object actual, Object expect) {
        this.message.put(String.valueOf(key), new DiffItem(actual, expect));
        this.diff++;
        return this;
    }

    public DiffMap add(Object key, DiffMap nested) {
        if (nested.diff == 0) {
            return this;
        }
        this.message.put(String.valueOf(key), nested.message);
        this.diff += nested.diff;
        return this;
    }

    /**
     * 返回差异信息
     *
     * @return
     */
    public String message() {
        return JSON.toJSON(this.message, true);
    }
}
