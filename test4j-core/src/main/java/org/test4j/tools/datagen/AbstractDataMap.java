package org.test4j.tools.datagen;

import java.util.*;

import lombok.Getter;
import org.test4j.json.JSON;
import org.test4j.module.ICore;
import org.test4j.tools.commons.ArrayHelper;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AbstractDataMap<DM extends ICore.DataMap>
        extends LinkedHashMap<String, Object>
        implements IDataMap<DM> {

    private static final long serialVersionUID = 1L;

    private transient boolean isArray;

    @Getter
    private int valueSize;

    public AbstractDataMap() {
        this.valueSize = 1;
        this.isArray = false;
    }

    public AbstractDataMap(int valueSize) {
        this.valueSize = valueSize;
        this.isArray = valueSize > 1;
    }

    @Override
    public Map<String, Object> map(int index) {
        if (index < 0 || index >= valueSize) {
            throw new RuntimeException("the index must between 0 and " + (this.valueSize - 1));
        }
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, Object> entry : this.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            data.put(key, this.getIndexValue(value, index));
        }
        return data;
    }

    @Override
    public DM putMap(Map map) {
        if (map != null) {
            map.forEach((k, v) -> this.put(String.valueOf(k), v));
        }
        return (DM) this;
    }

    @Override
    public Object get(String key) {
        return super.get(key);
    }

    @Override
    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    @Override
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    @Override
    public String getString(String key) {
        return String.valueOf(get(key));
    }

    @Override
    public List getList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            return (List) value;
        }
        List list = new ArrayList();
        for (int loop = 0; loop < valueSize; loop++) {
            list.add(this.getIndexValue(value, loop));
        }
        return list;
    }

    @Override
    public DM valueSize(int size) {
        this.valueSize = size;
        if (size > 1) {
            this.isArray = true;
        }
        return (DM) this;
    }

    @Override
    public void setValueSize(int valueSize) {
        this.valueSize(valueSize);
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public DM kv(String key, Object value, Object... values) {
        if (values != null && values.length == 0) {
            this.put(key, value);
        } else {
            this.isArray = true;
            this.put(key, ArrayHelper.arr(value, values));
        }
        return (DM) this;
    }

    @Override
    public void put(String key, Object value, Object... values) {
        this.kv(key, value, values);
    }

    @Override
    public String json() {
        return JSON.toJSON(this, false);
    }

    @Override
    public <R> R toObject(Class<R> klass) {
        return (R) JSON.toObject(json(), klass);
    }

    @Override
    public <R> List<R> toList(Class<R> klass) {
        return (List<R>) JSON.toList(json(), klass);
    }

    @Override
    public List<Map<String, ? extends Object>> toList() {
        List<Map<String, ? extends Object>> list = new ArrayList<>();
        for (int loop = 0; loop < valueSize; loop++) {
            list.add(this.map(loop));
        }
        return list;
    }

    private Object getIndexValue(Object value, int index) {
        if (value == null) {
            return null;
        } else if (this.isArray && value instanceof List) {
            List list = (List) value;
            if (list.size() == 0) {
                return null;
            } else {
                return index < list.size() ? list.get(index) : list.get(list.size() - 1);
            }
        } else if (value instanceof AbstractDataGenerator) {
            return ((AbstractDataGenerator) value).generate(index);
        } else {
            return value;
        }
    }

    @Override
    public boolean doesArray() {
        return this.isArray;
    }
}
