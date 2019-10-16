package org.test4j.tools.datagen;

import java.util.Map;
import java.util.function.Function;

public abstract class AbstractDataGenerator {
    private Map<String, Object> dataMap;

    /**
     * 生成第n个数据<br>
     * index计数从0开始
     *
     * @param index
     * @return
     */
    public abstract Object generate(int index);

    /**
     * 获取已经设置好字段的对应值
     *
     * @param field
     * @return
     */
    public Object value(String field) {
        if (this.dataMap == null) {
            throw new RuntimeException("the data map can't be null.");
        }
        if (this.dataMap.containsKey(field) == false) {
            throw new RuntimeException("unexist the key[" + field + "] of data map.");
        }
        return this.dataMap.get(field);
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    /**
     * 循环遍历objects对象列表
     *
     * @param objects
     * @return
     */
    public static AbstractDataGenerator repeat(Object... objects) {
        return new RepeatDataGenerator(objects);
    }

    /**
     * 生成随机对象
     *
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static AbstractDataGenerator random(Class type) {
        return new RandomDataGenerator(type);
    }

    /**
     * 步进生成数据
     *
     * @param from
     * @param step
     * @return
     */
    public static AbstractDataGenerator increase(Number from, Number step) {
        return new IncreaseDataGenerator(from, step);
    }

    /**
     * 按格式输出
     *
     * @param format 字符串格式， 比如 "abc%sABC",abc是前缀，ABC是后缀，%s是序号占位
     * @param from   序号占位起始值
     * @param step   序号占位增长步长
     * @return
     */
    public static AbstractDataGenerator increase(String format, Number from, Number step) {
        return new IncreaseDataGenerator(from, step) {
            @Override
            public Object generate(int index) {
                int _index = (Integer) super.generate(index);
                return String.format(format, _index);
            }
        };
    }

    public static AbstractDataGenerator increase(Number from, Number step, Function<Integer, Object> function) {
        return new IncreaseDataGenerator(from, step) {
            @Override
            public Object generate(int index) {
                int _index = (Integer) super.generate(index);
                return function.apply(_index);
            }
        };
    }

    /**
     * 从1开始自增（步长为1）
     *
     * @return
     */
    public static AbstractDataGenerator increase() {
        return increase(1, 1);
    }

    /**
     * 从1开始自增（步长为1）
     *
     * @param format 对序号进行格式化处理
     * @return
     */
    public static AbstractDataGenerator increase(String format) {
        return increase(format, 1, 1);
    }

    /**
     * 从1开始自增（步长为1）
     *
     * @param function 对序号进行处理的函数
     * @return
     */
    public static AbstractDataGenerator increase(Function<Integer, Object> function) {
        return increase(1, 1, function);
    }
}
