package org.test4j.hamcrest.diff;

import lombok.Getter;
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.reflector.PropertyAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 按map比较对象
 *
 * @author darui.wu
 * @create 2020/4/13 10:40 上午
 */
@Getter
public class DiffByMap extends BaseDiff<Map> {

    public DiffByMap(BaseDiff diff) {
        super(diff);
    }

    public DiffByMap(EqMode... modes) {
        super(modes);
    }

    /**
     * 对象比较
     *
     * @param parentKey
     * @param actual
     * @param expect
     */
    @Override
    public DiffMap compare(Object parentKey, Object actual, Map expect) {
        if (validateNull(parentKey, actual, expect)) {
            return this.diffMap;
        }
        Map<String, Object> expectMap = this.filterMap(expect);
        Map actualMap = this.toMap(actual, expectMap.keySet());
        for (Map.Entry entry : expectMap.entrySet()) {
            Object _actual = actualMap.get(entry.getKey());
            Object _expect = entry.getValue();
            new DiffFactory(this).compare(parentKey + "." + entry.getKey(), _actual, _expect);
        }
        return this.diffMap;
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
            filter.put(entry.getKey(), value);
        }
        return filter;
    }

    /**
     * 对象转换为Map
     *
     * @param target
     * @param properties
     * @return
     */
    private Map toMap(Object target, Set<String> properties) {
        if (target == null || target instanceof Map) {
            return (Map) target;
        }
        Map<String, Object> map = new HashMap<>();
        for (String property : properties) {
            try {
                Object value = PropertyAccessor.getPropertyByOgnl(target, property);
                map.put(property, value);
            } catch (NoSuchFieldRuntimeException e) {
                map.put(property, "NoSuchFieldRuntimeException(" + property + ")");
            }
        }
        return map;
    }
}