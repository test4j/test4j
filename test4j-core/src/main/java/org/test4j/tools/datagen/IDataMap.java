package org.test4j.tools.datagen;

import org.test4j.json.JSON;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DataMap：行列式对象
 *
 * @param <DM>
 * @author darui.wu
 */
public interface IDataMap<DM> {
    /**
     * 往DataMap中赋值
     *
     * @param key    键值
     * @param value  第一个值
     * @param values 后续值
     * @return
     */
    DM kv(String key, Object value, Object... values);

    /**
     * 往DataMap中赋值, 同 kv 方法
     *
     * @param key    键值
     * @param value  第一个值
     * @param values 后续值
     */
    default void put(String key, Object value, Object... values) {
        this.kv(key, value, values);
    }

    /**
     * 使用普通map对象初始化
     *
     * @param map
     * @return
     */
    DM init(Map map);

    /**
     * 返回key对应的值
     *
     * @param key
     * @return
     */
    IColData get(String key);

    /**
     * 返回单个value值时long的场景
     *
     * @param key
     * @return
     */
    default long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    /**
     * 返回单个value值时布尔值的场景
     *
     * @param key
     * @return
     */
    default boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    /**
     * 返回单个value值时String的场景
     *
     * @param key
     * @return
     */
    default String getString(String key) {
        return String.valueOf(get(key).col(1));
    }


    /**
     * 获取第 row 行所有的对象值
     *
     * @param row
     * @return
     */
    Map<String, ?> row(int row);


    /**
     * 返回记录集列表
     *
     * @return
     */
    List<Map<String, ?>> rows();

    /**
     * 将记录集转换为对象列表
     *
     * @param klass
     * @param <R>
     * @return
     */
    <R> List<R> rows(Class<R> klass);


    /**
     * 返回key列所有的对象值
     *
     * @param key
     * @return
     */
    List cols(String key);

    /**
     * 将row行对象json化输出
     *
     * @param row
     * @return
     */
    default String json(int row) {
        return JSON.toJSON(this.row(row), false);
    }

    /**
     * 将Map对象转换为klass对象
     *
     * @param klass
     * @param <R>
     * @return
     */
    default <R> R toObject(Class<R> klass) {
        return (R) JSON.toObject(json(0), klass);
    }

    /**
     * 返回记录行数量
     *
     * @return
     */
    int getRowSize();

    /**
     * DataMap是否为行列式对象
     *
     * @return
     */
    boolean isRowMap();

    /**
     * 返回DataMap的key值列表
     *
     * @return
     */
    Set<String> keySet();
}
