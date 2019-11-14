package org.test4j.tools.datagen;

import org.test4j.module.ICore;
import org.test4j.module.ICore.DataGenerator;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * 从start开始以step步长递增
     *
     * @param start
     * @param step
     * @return
     */
    public M increase(Number start, Number step) {
        this.map.put(this.column, DataGenerator.increase(start, step));
        return this.map;
    }

    /**
     * 从1开始以1步长递增
     *
     * @return
     */
    public M autoIncrease() {
        this.map.put(this.column, DataGenerator.increase(1, 1));
        return this.map;
    }

    /**
     * 按 String.format(format, index)生成内容， index以start开始以step步长递增
     *
     * @param format 字符串格式
     * @param start
     * @param step
     * @return
     */
    public M formatIncrease(String format, Number start, Number step) {
        this.map.put(this.column, DataGenerator.increase(format, start, step));
        return this.map;
    }

    /**
     * 按 String.format(format, index)生成内容， index从1开始以1步长递增
     *
     * @param format
     * @return
     */
    public M formatAutoIncrease(String format) {
        this.map.put(this.column, DataGenerator.increase(format, 1, 1));
        return this.map;
    }

    /**
     * 按function生成内容，function入参index是以start开始以step步长递增
     *
     * @param function
     * @param start
     * @param step
     * @return
     */
    public M functionIncrease(Function<Integer, Object> function, Number start, Number step) {
        this.map.put(this.column, DataGenerator.increase(start, step, function));
        return this.map;
    }

    /**
     * 按function生成内容，function入参index是以1开始以1步长递增
     *ß
     * @param function
     * @return
     */
    public M functionAutoIncrease(Function<Integer, Object> function) {
        this.map.put(this.column, DataGenerator.increase(1, 1, function));
        return this.map;
    }

    /**
     * 对values值进行统一处理
     *
     * @param function
     * @param value
     * @param values
     * @return
     */
    public M functionObjs(Function<Object, Object> function, Object value, Object... values) {
        Object[] items = Stream.of(values)
                .map(function::apply)
                .collect(Collectors.toList())
                .toArray(new Object[0]);
        this.map.put(this.column, function.apply(value), items);
        return this.map;
    }
}
