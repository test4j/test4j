package org.test4j.hamcrest.diff;

import org.test4j.exception.NoSuchFieldRuntimeException;
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
public class DiffUtil {
    private final boolean ignoreNull;

    private final boolean asString;

    private final boolean ignoreOrder;

    public DiffUtil(boolean ignoreNull, boolean asString, boolean ignoreOrder) {
        this.ignoreNull = ignoreNull;
        this.asString = asString;
        this.ignoreOrder = ignoreOrder;
    }

    public DiffMap diff(Object actual, Object expect) {
        if (expect == null) {
            return actual == null ? new DiffMap() : new DiffMap().add("root", actual, null);
        }
        if (ArrayHelper.isCollOrArray(actual)) {
            if (ArrayHelper.isCollOrArray(expect)) {
                List _actual = ListHelper.toList(actual, false);
                List _expect = ListHelper.toList(expect, false);
                return compareList("root", _actual, _expect);
            } else {
                return new DiffMap().add("root", "actual is a List", "expect should be a Map List");
            }
        } else {
            if (expect instanceof Map) {
                return compareObject("root", actual, (Map) expect);
            } else {
                return new DiffMap().add("root", "actual is an object", "expect should be a Map");
            }
        }
    }

    private DiffMap compareList(Object parentKey, List actualList, List<Map> expectList) {
        DiffMap diff = validateNull(parentKey, actualList, expectList);
        if (diff != null) {
            return diff;
        }
        diff = new DiffMap();
        int size = expectList.size();
        if (actualList.size() != size) {
            diff.add(parentKey, "the size is " + actualList.size(), "the size is " + size);
            return diff;
        }
        if (ignoreOrder) {
            DiffMap listDiff = compareListIgnoreOrder(parentKey, actualList, expectList);
            diff.add(parentKey, listDiff);
        } else {
            DiffMap listDiff = compareListWithOrder(parentKey, actualList, expectList);
            diff.add(parentKey, listDiff);
        }
        return diff;
    }

    /**
     * 忽略顺序比较
     *
     * @param parentKey
     * @param actualList
     * @param expectList
     * @return
     */
    private DiffMap compareListIgnoreOrder(Object parentKey, List actualList, List<Map> expectList) {
        DiffMap diff = new DiffMap();
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
                String key = "[" + loop + ":" + index + "]";
                DiffMap itemDiff = compareObject(key, actual, expectList.get(loop));
                child.add(loop, itemDiff);
                if (itemDiff.diff == 0) {
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
            DiffMap nest = new DiffMap();
            for (Map.Entry<Integer, DiffMap> child : item.getDiffItem().entrySet()) {
                nest.add("expect [" + child.getKey() + "]", child.getValue());
            }
            diff.add("actual [" + entry.getKey() + "]", nest);
        }
        return diff;
    }

    /**
     * 有序比较
     *
     * @param parentKey
     * @param actualList
     * @param expectList
     * @return
     */
    private DiffMap compareListWithOrder(Object parentKey, List actualList, List<Map> expectList) {
        DiffMap diff = new DiffMap();
        for (int index = 0; index < expectList.size(); index++) {
            Object actual = actualList.get(index);
            Map expect = expectList.get(index);
            String key = "[" + index + "]";
            DiffMap itemDiff = compareObject(key, actual, expect);
            diff.add(key, itemDiff);
        }
        return diff;
    }

    private DiffMap compareObject(Object parentKey, Object actual, Map expect) {
        DiffMap diff = validateNull(parentKey, actual, expect);
        if (diff != null) {
            return diff;
        }
        diff = new DiffMap();
        Map<String, Object> _expect = filterMap(expect);
        Map _actual = asMap(actual, _expect.keySet());
        for (Map.Entry entry : _expect.entrySet()) {
            Object key = entry.getKey();
            Object lvalue = _actual.get(entry.getKey());
            Object rvalue = entry.getValue();
            if (rvalue instanceof Map || rvalue == null) {
                DiffMap nested = compareObject(key, lvalue, (Map) rvalue);
                diff.add(key, nested);
            } else {
                Object _lvalue = asObject(lvalue);
                if (!rvalue.equals(_lvalue)) {
                    diff.add(key, lvalue, rvalue);
                }
            }
        }
        return diff;
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
        } else if (value instanceof Map) {
            return filterMap((Map) value);
        } else if (value.getClass().isPrimitive()) {
            return String.valueOf(value);
        } else if (asString) {
            return withoutQuotaJSON(value);
        } else {
            return value;
        }
    }

    private DiffMap validateNull(Object parentKey, Object actual, Object expect) {
        if (expect == null) {
            return actual == null || ignoreNull ? new DiffMap() : new DiffMap().add(parentKey, actual, null);
        } else if (actual == null) {
            return new DiffMap().add(parentKey, null, expect);
        } else if (actual.equals(expect)) {
            return new DiffMap();
        } else {
            return null;
        }
    }
}
