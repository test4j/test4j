package org.test4j.hamcrest.diff;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.TreeMap;

import static org.test4j.hamcrest.diff.BaseDiff.asString;

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
     * 忽略元素次数
     */
    int ignoreCount = 0;
    /**
     * value: String List
     */
    Map<String, String> message = new TreeMap<>();

    public DiffMap add(Object key, Object actual, Object expect) {
        this.message.put(String.valueOf(key), diffString(actual, expect));
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

    public void addIgnore() {
        this.ignoreCount++;
    }

    public String diffString(Object actual, Object expect) {
        return "\n\texpect=" + asString(expect) + "\n\tactual=" + asString(actual) + "";
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

    public boolean hasDiff() {
        return this.diff > 0;
    }

    /**
     * 差异项数量
     *
     * @return
     */
    public int diffCount() {
        return this.message.size();
    }
}