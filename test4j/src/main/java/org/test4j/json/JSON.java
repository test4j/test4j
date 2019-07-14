package org.test4j.json;

import org.test4j.json.interal.JSONFactory;

import java.lang.reflect.Type;
import java.util.List;


/**
 * json解码，编码工具类
 *
 * @author darui.wudr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class JSON {
    /**
     * 将对象编码为json串
     *
     * @param object
     * @return
     */
    public static final String toJSON(Object object, boolean isFormat) {
        return JSONFactory.instance().toJSON(object, isFormat);
    }

    public static final <T> T toObject(String json, Type klass) {
        return JSONFactory.instance().toObject(json, klass);
    }

    /**
     * 将json字符串反序列为对象
     *
     * @param json
     * @return
     */
    public static final <T> T toObject(String json) {
        return JSONFactory.instance().toObject(json);
    }

    /**
     * 反序列化json串
     *
     * @param json
     * @param klass
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Type... klass) {
        return JSONFactory.instance().toList(json, klass);
    }

    /**
     * 反序列化json串
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json) {
        return JSONFactory.instance().toList(json);
    }
}
