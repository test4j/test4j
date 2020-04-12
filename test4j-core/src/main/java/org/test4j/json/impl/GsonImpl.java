package org.test4j.json.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.test4j.json.interal.JSONInterface;
import org.test4j.tools.commons.StringHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * gson实现
 *
 * @author wudarui
 */
public class GsonImpl implements JSONInterface {
    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private static Gson gson_pretty = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Override
    public String toJSON(Object object, boolean isFormat) {
        if (isFormat) {
            return gson_pretty.toJson(object);
        } else {
            return gson.toJson(object);
        }
    }

    @Override
    public <T> T toObject(String json, Type klass) {
        return gson.fromJson(json, klass);
    }

    @Override
    public <T> T toObject(String json) {
        if (StringHelper.isBlankOrNull(json)) {
            return null;
        }
        String text = json.trim();
        if (text.startsWith("[") && text.endsWith("]")) {
            return (T) gson.fromJson(text, HashMap.class);
        } else if (text.startsWith("{") && text.endsWith("}")) {
            return (T) gson.fromJson(text, ArrayList.class);
        } else {
            return (T) json;
        }
    }

    @Override
    public <T> List<T> toList(String json, Class<T> klass) {
        return gson.fromJson(json, TypeToken.getParameterized(ArrayList.class, klass).getType());
    }

    @Override
    public <T> List<T> toList(String json) {
        return gson.fromJson(json, ArrayList.class);
    }
}
