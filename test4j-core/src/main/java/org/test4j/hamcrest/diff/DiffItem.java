package org.test4j.hamcrest.diff;

import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.json.JSON;

/**
 * 差异项
 *
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class DiffItem {
    /**
     * 实际值
     */
    private String actual;
    /**
     * 期望值
     */
    private String expect;

    public DiffItem(Object actual, Object expect) {
        this.actual = asString(actual);
        this.expect = asString(expect);
    }

    private String asString(Object value) {
        if (value == null) {
            return "<null>";
        } else if (value.getClass().isPrimitive() || value instanceof String) {
            return "(" + value.getClass().getSimpleName() + ") " + value;
        } else {
            return "(" + value.getClass().getSimpleName() + ") " + withoutQuotaJSON(value);
        }
    }

    /**
     * json化处理，同时去掉双引号
     *
     * @param value
     * @return
     */
    public static String withoutQuotaJSON(Object value) {
        String text = JSON.toJSON(value, true);
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        } else {
            return text;
        }
    }
}
