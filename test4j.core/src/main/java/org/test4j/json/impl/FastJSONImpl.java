package org.test4j.json.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.test4j.json.interal.JSONInterface;

import java.lang.reflect.Type;
import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * fast json 实现
 */
public class FastJSONImpl implements JSONInterface {
    private static SerializerFeature[] TO_JSON_NO_FORMAT = {
            WriteDateUseDateFormat,
            NotWriteDefaultValue,
            SkipTransientField,
            IgnoreNonFieldGetter
    };

    private static SerializerFeature[] TO_JSON_HAS_FORMAT = {
            WriteDateUseDateFormat,
            NotWriteDefaultValue,
            SkipTransientField,
            IgnoreNonFieldGetter,
            PrettyFormat
    };

    @Override
    public String toJSON(Object object, boolean isFormat) {
        if (isFormat) {
            return JSON.toJSONString(object, TO_JSON_HAS_FORMAT);
        } else {
            return JSON.toJSONString(object, TO_JSON_NO_FORMAT);
        }
    }

    @Override
    public <T> T toObject(String json, Type klass) {
        return JSON.parseObject(json, klass);
    }

    @Override
    public <T> T toObject(String json) {
        return (T) JSON.parseObject(json);
    }

    @Override
    public <T> List<T> toList(String json, Type[] klass) {
        return (List<T>) JSON.parseArray(json, klass);
    }

    @Override
    public <T> List<T> toList(String json) {
        return (List<T>) JSON.parseArray(json);
    }
}
