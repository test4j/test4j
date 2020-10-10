package org.test4j.tools.datagen;

import org.test4j.module.ICore.DataGenerator;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @param <M>
 * @author darui.wu
 */
public class KeyValue<M extends IDataMap> {
    private final M map;

    private final String column;

    private final String property;

    private final Supplier<Boolean> isTable;

    public KeyValue(M map, String column, String property, Supplier<Boolean> isTable) {
        this.map = map;
        this.column = column;
        this.property = property;
        this.isTable = isTable;
    }

    public M values(Object value, Object... values) {
        this.map.kv(this.key(), value, values);
        return this.map;
    }

    /**
     * 将记录行号（从0开始）转换为赋值values数组序号（从0到length-1）<br/>
     * o 如果转换后的数组序号小于0，按0处理<br/>
     * o 如果转换后的数组序号大于数组size-1，按size-1处理
     *
     * @param changeIndex
     * @param values
     * @return
     */
    public M values(Function<Integer, Integer> changeIndex, Object... values) {
        // 多态重载时，第一个参数为null时，兼容处理
        if (changeIndex == null) {
            this.map.kv(this.key(), null, values);
            return this.map;
        }
        if (values == null || values.length == 0) {
            throw new RuntimeException("the values should be specified.");
        }
        DataGenerator generator = new DataGenerator() {
            @Override
            public Object generate(int index) {
                Integer _index = changeIndex.apply(index);
                if (_index == null || _index < 0) {
                    _index = 0;
                }
                if (_index >= values.length) {
                    _index = values.length - 1;
                }
                return values[_index];
            }
        };
        this.map.kv(this.key(), generator);
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
        this.map.kv(this.key(), DataGenerator.increase(start, step));
        return this.map;
    }

    /**
     * 从1开始以1步长递增
     *
     * @return
     */
    public M autoIncrease() {
        this.map.kv(this.key(), DataGenerator.increase(1, 1));
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
        this.map.kv(this.key(), DataGenerator.increase(format, start, step));
        return this.map;
    }

    /**
     * 按 String.format(format, index)生成内容， index从1开始以1步长递增
     *
     * @param format
     * @return
     */
    public M formatAutoIncrease(String format) {
        this.map.kv(this.key(), DataGenerator.increase(format, 1, 1));
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
        this.map.kv(this.key(), DataGenerator.increase(start, step, function));
        return this.map;
    }

    /**
     * 按function生成内容，function入参index是以1开始以1步长递增
     *
     * @param function
     * @return
     */
    public M functionAutoIncrease(Function<Integer, Object> function) {
        this.map.kv(this.key(), DataGenerator.increase(1, 1, function));
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
    public <O> M functionObjs(Function<O, Object> function, O value, O... values) {
        Object[] items = Stream.of(values)
            .map(function::apply)
            .collect(toList())
            .toArray(new Object[0]);
        this.map.kv(this.key(), function.apply(value), items);
        return this.map;
    }

    /**
     * 循环赋值
     *
     * @param loops
     * @return
     */
    public M loop(Object... loops) {
        this.map.kv(this.key(), DataGenerator.repeat(loops));
        return this.map;
    }

    /**
     * 以values数组作为入参，结合行序号生成需要的数据
     * 需要在function里面自行处理（注意）序号越界的可能
     *
     * @param generator
     * @param values
     * @param <O>
     * @return
     */
    public <O> M generateBy(BiFunction<Integer, O[], Object> generator, O... values) {
        if (values == null || values.length == 0) {
            throw new RuntimeException("the values should be specified.");
        }
        DataGenerator _generator = new DataGenerator() {
            @Override
            public Object generate(int index) {
                return generator.apply(index, values);
            }
        };
        this.map.kv(this.key(), _generator);
        return this.map;
    }

    /**
     * 根据记录行号生成数据
     *
     * @param generator
     * @return
     */
    public M generate(Function<Integer, Object> generator) {
        DataGenerator _generator = new DataGenerator() {
            @Override
            public Object generate(int index) {
                return generator.apply(index);
            }
        };
        this.map.kv(this.key(), _generator);
        return this.map;
    }

    /**
     * 随机生成
     *
     * @return
     */
    public M random() {
        this.map.kv(this.key(), DataGenerator.random(this.key().getClass()));
        return this.map;
    }

    private static Random random = new Random(System.currentTimeMillis());

    /**
     * 随机从randoms中挑选
     *
     * @param randoms
     * @return
     */
    public M random(Object... randoms) {
        if (randoms == null || randoms.length == 0) {
            throw new RuntimeException("the randoms should be specified.");
        }
        DataGenerator generator = new DataGenerator() {
            @Override
            public Object generate(int index) {
                int _index = Math.abs(random.nextInt()) % randoms.length;
                return randoms[_index];
            }
        };

        this.map.kv(this.key(), generator);
        return this.map;
    }

    private String key() {
        return this.isTable.get() ? this.column : this.property;
    }
}