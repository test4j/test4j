package org.test4j.hamcrest.diff;

import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.commons.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * BaseDiff:比较基类
 *
 * @author:darui.wu Created by darui.wu on 2020/4/13.
 */
public abstract class BaseDiff<T> {
    /**
     * 是否忽略null值比较
     */
    protected final boolean ignoreNull;
    /**
     * 是否当作字符串比较
     */
    protected final boolean asString;
    /**
     * list比较时，是否忽略顺序关系
     */
    protected final boolean ignoreOrder;
    /**
     * 是否忽略默认值
     */
    protected boolean ignoreDefault;
    /**
     * 忽略日期类型
     */
    protected boolean ignoreDateType;
    /**
     * 比较差异点
     */
    protected final DiffMap diffMap;

    public BaseDiff(EqMode... modes) {
        List<EqMode> list = ListHelper.toList(modes);
        this.ignoreNull = list.contains(EqMode.IGNORE_DEFAULTS);
        this.asString = list.contains(EqMode.EQ_STRING);
        this.ignoreOrder = list.contains(EqMode.IGNORE_ORDER);
        this.ignoreDateType = list.contains(EqMode.IGNORE_DATES);

        this.diffMap = new DiffMap();
    }

    public BaseDiff(boolean ignoreNull, boolean asString, boolean ignoreOrder) {
        this.ignoreNull = ignoreNull;
        this.asString = asString;
        this.ignoreOrder = ignoreOrder;
        this.diffMap = new DiffMap();
    }

    public BaseDiff(BaseDiff diff) {
        this.ignoreNull = diff.ignoreNull;
        this.asString = diff.asString;
        this.ignoreOrder = diff.ignoreOrder;
        this.ignoreDefault = diff.ignoreDefault;
        this.ignoreDateType = diff.ignoreDateType;
        this.diffMap = diff.diffMap;
    }

    public BaseDiff(DiffByList diff, DiffMap diffMap) {
        this.ignoreNull = diff.ignoreNull;
        this.asString = diff.asString;
        this.ignoreOrder = diff.ignoreOrder;
        this.ignoreDefault = diff.ignoreDefault;
        this.ignoreDateType = diff.ignoreDateType;
        this.diffMap = diffMap;
    }

    /**
     * 比较对象
     *
     * @param parentKey
     * @param actual
     * @param expect
     * @return 差异
     */
    public abstract DiffMap compare(Object parentKey, Object actual, T expect);

    /**
     * 比较null对象或者判断对象相等
     *
     * @param parentKey
     * @param actual
     * @param expect
     * @return true: 无需继续比较 false:需要进一步比较
     */
    protected boolean validateNull(Object parentKey, Object actual, Object expect) {
        if (expect == null) {
            if (actual != null && !this.ignoreNull) {
                this.diffMap.add(parentKey, actual, null);
            } else {
                this.diffMap.addIgnore();
            }
            return true;
        } else if (actual == null) {
            this.diffMap.add(parentKey, null, expect);
            return true;
        } else {
            return actual.equals(expect);
        }
    }

    /**
     * 取值
     *
     * @param value
     * @return
     */
    protected Object asObject(Object value, boolean asString) {
        if (value == null || value instanceof String) {
            return value;
        } else if (value instanceof Map || ArrayHelper.isCollOrArray(value)) {
            return value;
        } else if (value.getClass().isPrimitive()) {
            return String.valueOf(value);
        } else if (asString) {
            return StringHelper.toString(value);
        } else {
            return value;
        }
    }

    public static String asString(Object value) {
        if (value == null) {
            return "<null>";
        } else if (value.getClass().isPrimitive() || value instanceof String) {
            return "(" + value.getClass().getSimpleName() + ") " + value;
        } else {
            return "(" + value.getClass().getSimpleName() + ") " + StringHelper.toString(value);
        }
    }
}