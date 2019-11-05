package org.test4j.json.interal;

import java.lang.reflect.Type;
import java.util.List;

public interface JSONInterface {
    /**
     * 将对象转换为JSON字符串
     *
     * @param object
     * @param isFormat 是否格式化
     * @return
     */
    String toJSON(Object object, boolean isFormat);

    /**
     * 反序列化json串
     *
     * @param json
     * @param klass
     * @param <T>
     * @return
     */
    <T> T toObject(String json, Type klass);

    /**
     * 反序列化json串
     *
     * @param json
     * @param <T>
     * @return
     */
    <T> T toObject(String json);

    /**
     * 反序列化json串
     *
     * @param json
     * @param klass
     * @param <T>
     * @return
     */
    <T> List<T> toList(String json, Type[] klass);

    /**
     * 反序列化json串
     *
     * @param json
     * @param <T>
     * @return
     */
    <T> List<T> toList(String json);
}
