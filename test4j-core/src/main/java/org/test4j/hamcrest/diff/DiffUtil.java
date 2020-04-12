package org.test4j.hamcrest.diff;

import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.reflector.FieldAccessor;

import java.util.*;

import static java.util.stream.Collectors.toMap;
import static org.test4j.hamcrest.diff.DiffItem.withoutQuotaJSON;

/**
 * Map对象比较
 *
 * @author wudarui
 */
public class DiffUtil {

    public static DiffMap diff(Object actual, Map expect, boolean ignoreNull, boolean asString) {
        if (expect == null) {
            return actual == null ? new DiffMap() : new DiffMap().add("root", actual, null);
        }
        return compare("root", actual, expect, ignoreNull, asString);
    }

    public static DiffMap diff(List actual, List<Map> expect, boolean ignoreNull, boolean asString, boolean ignoreOrder) {
        if (expect == null) {
            return actual == null ? new DiffMap() : new DiffMap().add("root", actual, null);
        }
        return compareList("root", actual, expect, ignoreNull, asString, ignoreOrder);
    }

    private static DiffMap compareList(Object parentKey, List actualList, List<Map> expectList, boolean ignoreNull, boolean asString, boolean ignoreOrder) {
        DiffMap diff = validateNull(parentKey, actualList, expectList, ignoreNull);
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
            DiffMap listDiff = compareListIgnoreOrder(parentKey, actualList, expectList, ignoreNull, asString);
            diff.add(parentKey, listDiff);
        } else {
            DiffMap listDiff = compareListOrder(parentKey, actualList, expectList, ignoreNull, asString);
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
     * @param ignoreNull
     * @param asString
     * @return
     */
    private static DiffMap compareListIgnoreOrder(Object parentKey, List actualList, List<Map> expectList, boolean ignoreNull, boolean asString) {
        DiffMap diff = new DiffMap();
        Map<Integer, MatchItem> all = new HashMap<>();
        int size = expectList.size();
        // 已经被匹配的比较对象
        Set<Integer> matchedExpected = new HashSet<>(size);
        for (int index = 0; index < size; index++) {
            String key1 = "[" + index + "]";
            Object actual = actualList.get(index);
            MatchItem child = new MatchItem(index);
            for (int loop = 0; loop < size; loop++) {
                // 跳过已经被匹配项
                if (matchedExpected.contains(loop)) {
                    continue;
                }
                String key2 = "[" + loop + "]";
                DiffMap itemDiff = compare(key1 + key2, actual, expectList.get(loop), ignoreNull, asString);
                child.add(loop, itemDiff);
                if (itemDiff.diff == 0) {
                    matchedExpected.add(loop);
                    break;
                }
            }
            all.put(index, child);
        }
        for (Map.Entry<Integer, MatchItem> entry : all.entrySet()) {
            int key = entry.getKey();
            MatchItem item = entry.getValue();
            if (item.getExpected() != null) {
                continue;
            }
            item.remove(matchedExpected);
            for (DiffMap child : item.getDiffMap()) {
                diff.add(parentKey, child);
            }
        }
        return diff;
    }

    /**
     * 有序比较
     *
     * @param parentKey
     * @param actualList
     * @param expectList
     * @param ignoreNull
     * @param asString
     * @return
     */
    private static DiffMap compareListOrder(Object parentKey, List actualList, List<Map> expectList, boolean ignoreNull, boolean asString) {
        DiffMap diff = new DiffMap();
        for (int index = 0; index < expectList.size(); index++) {
            Object actual = actualList.get(index);
            Map expect = expectList.get(index);
            String key = "[" + index + "]";
            DiffMap itemDiff = compare(key, actual, expect, ignoreNull, asString);
            diff.add(key, itemDiff);
        }
        return diff;
    }

    private static DiffMap compare(Object parentKey, Object actual, Map expect, boolean ignoreNull, boolean asString) {
        DiffMap diff = validateNull(parentKey, actual, expect, ignoreNull);
        if (diff != null) {
            return diff;
        }
        diff = new DiffMap();
        Map<String, Object> _expect = filterMap(expect, ignoreNull, asString);
        Map _actual = asMap(actual, _expect.keySet());
        for (Map.Entry entry : _expect.entrySet()) {
            Object key = entry.getKey();
            Object lvalue = _actual.get(key);
            Object rvalue = entry.getValue();
            if (rvalue instanceof Map || rvalue == null) {
                DiffMap nested = compare(key, lvalue, (Map) rvalue, ignoreNull, asString);
                diff.add(key, nested);
            } else {
                Object _lvalue = asObject(lvalue, false, asString);
                if (!rvalue.equals(_lvalue)) {
                    diff.add(key, lvalue, rvalue);
                }
            }
        }
        return diff;
    }

    private static Map asMap(Object target, Set<String> properties) {
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
     * @param ignoreNull
     * @param asString
     * @return
     */
    private static Map filterMap(Map map, boolean ignoreNull, boolean asString) {
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
            filter.put(entry.getKey(), asObject(value, ignoreNull, asString));
        }
        return filter;
    }

    private static Object asObject(Object value, boolean ignoreNull, boolean asString) {
        if (value == null || value instanceof String) {
            return value;
        } else if (value instanceof Map) {
            return filterMap((Map) value, ignoreNull, asString);
        } else if (value.getClass().isPrimitive()) {
            return String.valueOf(value);
        } else if (asString) {
            return withoutQuotaJSON(value);
        } else {
            return value;
        }
    }

    private static DiffMap validateNull(Object parentKey, Object actual, Object expect, boolean ignoreNull) {
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
