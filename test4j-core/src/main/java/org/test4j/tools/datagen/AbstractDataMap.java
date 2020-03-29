package org.test4j.tools.datagen;

import lombok.Getter;
import org.test4j.json.JSON;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.datagen.IColData.MulRowValue;
import org.test4j.tools.datagen.IColData.OneRowValue;

import java.util.*;
import java.util.stream.Stream;

/**
 * @param <DM>
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AbstractDataMap<DM extends DataMap>
        extends LinkedHashMap<String, IColData>
        implements IDataMap<DM> {

    private static final long serialVersionUID = 1L;

    /**
     * 是否为一个行列式对象
     */
    @Getter
    private final boolean isRowMap;
    /**
     * 记录集大小
     */
    @Getter
    private final int rowSize;

    /**
     * 表示value值非列对象
     */
    public AbstractDataMap() {
        this.rowSize = 1;
        this.isRowMap = false;
    }

    /**
     * 表示value值时一个列对象, 并且设置行数 rowSize
     *
     * @param rowSize
     */
    public AbstractDataMap(int rowSize) {
        this.rowSize = rowSize;
        this.isRowMap = true;
    }

    @Override
    public Map<String, Object> row(int row) {
        if (row < 0 || row >= rowSize) {
            throw new RuntimeException("the index must between 0 and " + (this.rowSize - 1));
        }
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, IColData> entry : this.entrySet()) {
            data.put(entry.getKey(), entry.getValue().row(row));
        }
        return data;
    }

    @Override
    public DM init(Map map) {
        if (map != null) {
            map.forEach((k, v) -> this.kv(String.valueOf(k), v));
        }
        return (DM) this;
    }

    @Override
    public IColData get(String key) {
        return super.get(key);
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public List cols(String key) {
        List list = new ArrayList(rowSize);
        for (int row = 0; row < rowSize; row++) {
            list.add(this.get(key).row(row));
        }
        return list;
    }

    @Override
    public DM arr(String key, Object... arr) {
        if (isRowMap) {
            if (arr == null || arr.length == 0) {
                this.putMulRowValue(key, null, new Object[0]);
            } else {
                this.putMulRowValue(key, arr[0], Arrays.copyOfRange(arr, 1, arr.length));
            }
        } else {
            this.putOneRowValue(key, arr, new Object[0]);
        }
        return (DM) this;
    }

    @Override
    public DM kv(String key, Object value, Object... values) {
        if (isRowMap) {
            this.putMulRowValue(key, value, values);
        } else {
            this.putOneRowValue(key, value, values);
        }
        return (DM) this;
    }

    private void putMulRowValue(String key, Object value, Object[] values) {
        MulRowValue row = new MulRowValue();
        if (value instanceof AbstractDataGenerator) {
            ((AbstractDataGenerator) value).setDataMap(this);
        }
        row.add(value);
        if (values == null) {
            row.add(null);
            return;
        }
        /**
         * 第一个对象是否为数组
         */
        boolean isFirstArray = ArrayHelper.isArray(value);
        /**
         * 是否2维数组
         */
        boolean is2dArray = false;
        for (Object item : values) {
            if (ArrayHelper.isArray(item)) {
                is2dArray = true;
            }
        }
        if (isFirstArray && !is2dArray) {
            row.add(values);
        } else {
            Stream.of(values).forEach(row::add);
        }
        this.put(key, row);
    }

    private void putOneRowValue(String key, Object value, Object[] values) {
        if (values != null && values.length != 0) {
            throw new RuntimeException("this DataMap isn't a RowMap, please use parametrical constructor：new DataMap(size)");
        }
        this.put(key, new OneRowValue(value));
    }

    public void put(String key, Object value) {
        throw new RuntimeException("please use .kv(String, Object, Object ...)");
//        this.kv(key, value, new Object[0]);
    }

    @Override
    public List<Map<String, ?>> rows() {
        List<Map<String, ?>> list = new ArrayList<>(rowSize);
        for (int row = 0; row < rowSize; row++) {
            list.add(this.row(row));
        }
        return list;
    }

    @Override
    public <R> List<R> rows(Class<R> klass) {
        return JSON.toList(JSON.toJSON(rows(), false), klass);
    }

    @Override
    public int getColSize() {
        return this.size();
    }


    @Override
    public boolean containsKey(String key) {
        return super.containsKey(key);
    }

    @Override
    public String toString() {
        if (isRowMap) {
            return JSON.toJSON(this.rows(), false);
        } else {
            return JSON.toJSON(this.row(1), false);
        }
    }
}
