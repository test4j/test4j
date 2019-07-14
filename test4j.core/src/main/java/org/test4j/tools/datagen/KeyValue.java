package org.test4j.tools.datagen;

public class KeyValue<M extends IDataMap> {
    private final M map;

    private final String column;

    public KeyValue(M map, String column) {
        this.map = map;
        this.column = column;
    }

    public M values(Object value, Object... values) {
        this.map.put(this.column, value, values);
        return this.map;
    }
}
