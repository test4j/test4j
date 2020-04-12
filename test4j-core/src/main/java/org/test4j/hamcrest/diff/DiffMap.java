package org.test4j.hamcrest.diff;

import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.json.JSON;

import java.util.*;

/**
 * 差异项列表
 *
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class DiffMap {
    int diff = 0;
    Map<String, Object> message = new TreeMap<>();

    public DiffMap add(Object key, Object actual, Object expect) {
        this.message.put(String.valueOf(key), new DiffItem(actual, expect));
        this.diff++;
        return this;
    }

    public DiffMap add(Object key, DiffMap nested) {
        if (nested.diff == 0) {
            return this;
        }
        String _key = String.valueOf(key);
        Object value = message.get(_key);
        if (value == null) {
            this.message.put(_key, nested.message);
        } else {
            if (!(value instanceof Collection)) {
                List list = new ArrayList();
                list.add(value);
                this.message.put(_key, list);
                value = list;
            }
            ((Collection) value).add(nested.message);
        }
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
