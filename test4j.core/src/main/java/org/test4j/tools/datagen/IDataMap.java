package org.test4j.tools.datagen;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DataMap标识接口
 *
 * @param <DM>
 * @author darui.wu
 */
public interface IDataMap<DM> {
    /**
     * 往datamap中赋值
     *
     * @param key
     * @param value
     * @param values
     * @return
     */
    DM kv(String key, Object value, Object... values);


    void put(String key, Object value, Object... values);

    /**
     * json化输出
     *
     * @return
     */
    String json();

    <R> R toObject(Class<R> klass);

    <R> List<R> toList(Class<R> klass);

    /**
     * 展开DataMap对象为List
     *
     * @return
     */
    List<Map<String, ? extends Object>> toList();

    DM putMap(Map map);

    /**
     * 返回key对应的值
     *
     * @param key
     * @return
     */
    Object get(String key);

    long getLong(String key);

    boolean getBoolean(String key);

    String getString(String key);

    List getList(String key);

    DM valueSize(int size);

    /**
     * 返回DataMap的key值列表
     *
     * @return
     */
    Set<String> keySet();

    /**
     * DataMap的value是否为list对象
     *
     * @return
     */
    boolean doesArray();

    /**
     * 获取第index个map对象
     *
     * @param index
     * @return
     */
    Map<String, ?> map(int index);

    /**
     * 返回值列表大小
     *
     * @return
     */
    int getValueSize();

    void setValueSize(int valueSize);
}
