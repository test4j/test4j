package org.test4j.hamcrest.diff;

import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.hamcrest.iassert.interal.IAssert;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.reflector.FieldAccessor;

import java.util.*;

import static org.test4j.hamcrest.diff.DiffItem.withoutQuotaJSON;

/**
 * Map对象比较
 *
 * @author wudarui
 */
public class Diff {
    private final boolean ignoreNull;

    private final boolean asString;

    private final boolean ignoreOrder;

    private DiffMap diff = new DiffMap();

    public Diff(boolean ignoreNull, boolean asString, boolean ignoreOrder) {
        this.ignoreNull = ignoreNull;
        this.asString = asString;
        this.ignoreOrder = ignoreOrder;
    }

    public Diff copy() {
        return new Diff(this.ignoreNull, this.asString, this.ignoreOrder);
    }

    public DiffMap diff(Object actual, Object expect) {
        this.compare("$", actual, expect);
        return this.diff;
    }

    DiffMap compare(Object parentKey, Object actual, Object expect) {
        if (expect == null) {
            if (actual != null) {
                this.diff.add(parentKey, actual, null);
            }
            return this.diff;
        }
        if (ArrayHelper.isCollOrArray(actual)) {
            if (expect instanceof IAssert) {
                // TODO
                throw new RuntimeException("TODO");
            } else if (expect instanceof IDataMap) {
                List _actual = ListHelper.toList(actual, false);
                List _expect = ((IDataMap) expect).rows();
                this.compareList(parentKey, _actual, _expect);
            } else if (ArrayHelper.isCollOrArray(expect)) {
                List _actual = ListHelper.toList(actual, false);
                List _expect = ListHelper.toList(expect, false);
                this.compareList(parentKey, _actual, _expect);
            } else {
                this.diff.add(parentKey, "actual is a List", "expect should be a Map List");
            }
        } else {
            if (expect instanceof IAssert) {
                // TODO
                throw new RuntimeException("TODO");
            } else if (expect instanceof Map) {
                this.compareObject(parentKey, actual, (Map) expect);
            } else {
                this.diff.add(parentKey, "actual is an object", "expect should be a Map");
            }
        }
        return this.diff;
    }

    private void compareList(Object parentKey, List actualList, List<Map> expectList) {
        if (validateNull(parentKey, actualList, expectList)) {
            return;
        }
        int size = expectList.size();
        if (actualList.size() != size) {
            diff.add(parentKey, "the size is " + actualList.size(), "the size is " + size);
            return;
        }
        if (ignoreOrder) {
            this.compareListIgnoreOrder(parentKey, actualList, expectList);
        } else {
            for (int index = 0; index < expectList.size(); index++) {
                String key = parentKey + "[" + index + "]";
                this.compare(key, actualList.get(index), expectList.get(index));
            }
        }
    }

    /**
     * 忽略顺序比较
     *
     * @param parentKey
     * @param actualList
     * @param expectList
     * @return
     */
    private void compareListIgnoreOrder(Object parentKey, List actualList, List<Map> expectList) {
        Map<Integer, MatchItem> all = new HashMap<>();
        int size = expectList.size();
        // 已经被匹配的比较对象
        Set<Integer> matchedExpected = new HashSet<>(size);
        for (int index = 0; index < size; index++) {
            Object actual = actualList.get(index);
            MatchItem child = new MatchItem(index);
            for (int loop = 0; loop < size; loop++) {
                // 跳过已经被匹配项
                if (matchedExpected.contains(loop)) {
                    continue;
                }
                String key = parentKey + "[" + (loop + 1) + "]~[" + (index + 1) + "]";
                DiffMap childDiff = this.copy().compare(key, actual, expectList.get(loop));
                child.add(loop, childDiff);
                // 已经匹配到, 无需继续
                if (childDiff.diff == 0) {
                    matchedExpected.add(loop);
                    break;
                }
            }
            all.put(index, child);
        }
        for (Map.Entry<Integer, MatchItem> entry : all.entrySet()) {
            MatchItem item = entry.getValue();
            if (item.getExpected() != null) {
                continue;
            }
            item.remove(matchedExpected);
            for (Map.Entry<Integer, DiffMap> child : item.getDiffItem().entrySet()) {
                diff.add(child.getValue());
            }
        }
    }


    private void compareObject(Object parentKey, Object actual, Map expect) {
        if (validateNull(parentKey, actual, expect)) {
            return;
        }
        Map<String, Object> _expect = filterMap(expect);
        Map _actual = asMap(actual, _expect.keySet());
        for (Map.Entry entry : _expect.entrySet()) {
            Object key = entry.getKey();
            Object lvalue = _actual.get(key);
            Object rvalue = entry.getValue();
            if (rvalue instanceof Map || rvalue == null) {
                this.compareObject(parentKey + "." + key, lvalue, (Map) rvalue);
            } else if (ArrayHelper.isCollOrArray(rvalue)) {
                this.compare(parentKey + "." + key, lvalue, rvalue);
            } else {
                Object _lvalue = asObject(lvalue);
                if (!rvalue.equals(_lvalue)) {
                    diff.add(parentKey + "." + key, lvalue, rvalue);
                }
            }
        }
    }

    private Map asMap(Object target, Set<String> properties) {
        if (target == null || target instanceof Map) {
            return (Map) target;
        }
        Map<String, Object> map = new HashMap<>();
        for (String property : properties) {
            try {
                Object value = FieldAccessor.getValue(target, property);
                map.put(property, value);
            } catch (NoSuchFieldRuntimeException e) {
                map.put(property, property + " - NoSuchFieldRuntimeException");
            }
        }
        return map;
    }

    /**
     * 过滤处理Map
     *
     * @param map
     * @return
     */
    private Map filterMap(Map map) {
        Map filter = new HashMap<>(map.size());
        Map real = map;
        if (map instanceof IDataMap) {
            real = ((IDataMap) map).map();
        }
        for (Map.Entry entry : (Set<Map.Entry>) real.entrySet()) {
            Object value = entry.getValue();
            if (value == null && ignoreNull) {
                continue;
            }
            filter.put(entry.getKey(), asObject(value));
        }
        return filter;
    }

    private Object asObject(Object value) {
        if (value == null || value instanceof String) {
            return value;
        } else if (value instanceof Map || ArrayHelper.isCollOrArray(value)) {
            return value;
        } else if (value.getClass().isPrimitive()) {
            return String.valueOf(value);
        } else if (asString) {
            return withoutQuotaJSON(value);
        } else {
            return value;
        }
    }

    /**
     * 比较null对象
     *
     * @param parentKey
     * @param actual
     * @param expect
     * @return true: 无需继续比较 false:需要进一步比较
     */
    private boolean validateNull(Object parentKey, Object actual, Object expect) {
        if (expect == null) {
            if (actual != null && !ignoreNull) {
                this.diff.add(parentKey, actual, null);
            }
            return true;
        } else if (actual == null) {
            this.diff.add(parentKey, null, expect);
            return true;
        }
        return actual.equals(expect);
    }
}
