package org.test4j.tools.datagen;

import lombok.Getter;
import org.test4j.json.JSON;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.IColData.OneRowValue;
import org.test4j.tools.datagen.IColData.MulRowValue;

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
            data.put(entry.getKey(), entry.getValue().col(row));
        }
        return data;
    }

    @Override
    public DM init(Map map) {
        if (map != null) {
            map.forEach((k, v) -> this.put(String.valueOf(k), v));
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
            list.add(this.get(key).col(row));
        }
        return list;
    }

    @Override
    public DM kv(String key, Object value, Object... values) {
        if (isRowMap) {
            MulRowValue row = new MulRowValue();
            row.add(value);
            Stream.of(values).forEach(row::add);
        } else {
            if (values != null && values.length != 0) {
                throw new RuntimeException("this DataMap isn't a RowMap");
            }
            this.put(key, new OneRowValue(value));
        }
        return (DM) this;
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
}
