package org.test4j.hamcrest.diff;

import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.json.JSON;

import java.util.*;

import static org.test4j.tools.commons.ListHelper.toList;

/**
 * 差异项列表
 *
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class DiffMap {
    int diff = 0;
    /**
     * value: String List
     */
    Map<String, String> message = new TreeMap<>();

    public DiffMap add(Object key, Object actual, Object expect) {
        this.message.put(String.valueOf(key), new DiffItem(actual, expect).toString());
        this.diff++;
        return this;
    }

    public DiffMap add(DiffMap nested) {
        if (nested.diff == 0) {
            return this;
        }
        for (Map.Entry<String, String> entry : nested.message.entrySet()) {
            String key = entry.getKey();
            if (this.message.containsKey(key)) {
                this.message.put(key, this.message.get(key) + "; " + entry.getValue());
            } else {
                this.message.put(key, entry.getValue());
            }
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
        StringBuffer buff = new StringBuffer("\n");
        for (Map.Entry<String, String> entry : this.message.entrySet()) {
            buff.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return buff.toString();
    }
}
