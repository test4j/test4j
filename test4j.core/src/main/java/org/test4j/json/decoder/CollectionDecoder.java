package org.test4j.json.decoder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.test4j.json.JSONException;
import org.test4j.json.decoder.base.BaseDecoder;
import org.test4j.json.decoder.base.DecoderException;
import org.test4j.json.decoder.base.DecoderFactory;
import org.test4j.json.helper.JSONArray;
import org.test4j.json.helper.JSONMap;
import org.test4j.json.helper.JSONObject;
import org.test4j.tools.commons.ClazzHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionDecoder extends BaseDecoder {
    public static final CollectionDecoder toCOLLECTION = new CollectionDecoder();

    @Override
    public <T> T decode(JSONObject json, Type toType, Map<String, Object> references) {
        if (json == null) {
            return null;
        }
        if (json instanceof JSONMap) {
            Collection list = this.parseFromJSONMap((JSONMap) json, toType, references);
            return (T) list;
        } else if (json instanceof JSONArray) {
            Collection list = this.parseFromJSONArray(((JSONArray) json), toType, references);
            return (T) list;
        } else {
            throw new DecoderException(
                    "illegal type for ArrayDecoder. the type can only is JSONArray or JSONMap, but actual is JSONSingle.");
        }
    }

    private Collection parseFromJSONMap(JSONMap map, Type toType, Map<String, Object> references) {
        String referenceID = map.getReferFromJSONProp();
        if (referenceID != null) {
            Object o = references.get(referenceID);
            return (Collection) o;
        }

        Type type = map.getClazzFromJSONFProp(toType);
        if (this.accept(type) == false) {
            throw new JSONException("JSONMap must have property that declared the array type.");
        }

        JSONObject array = map.getValueFromJSONProp();
        if (!(array instanceof JSONArray)) {
            throw new JSONException("illegal type for ArrayDecoder. the type can only be JSONArray, but actual is "
                    + array.getClass().getName());
        }

        Collection list = this.parseFromJSONArray(((JSONArray) array), toType, references);
        String newID = map.getReferenceID();
        if (newID != null) {
            references.put(newID, list);
        }
        return list;
    }

    private final Collection parseFromJSONArray(JSONArray jsonArray, Type toType, Map<String, Object> references) {
        Collection list = this.newInstance(toType);
        for (Iterator<JSONObject> it = jsonArray.iterator(); it.hasNext();) {
            JSONObject jsonObject = it.next();
            Type componentType = super.getComponent(jsonObject, toType, 0);
            IDecoder decoder = DecoderFactory.getDecoder(componentType);
            Object o = decoder.decode(jsonObject, componentType, references);
            list.add(o);
        }
        return list;
    }

    @Override
    protected Collection newInstance(Type toType) {
        Class raw = this.getRawType(toType, null);
        Constructor constructor = null;
        try {
            constructor = raw.getConstructor();
        } catch (Exception e) {
            constructor = null;
        }
        if (constructor != null) {
            Object o = ClazzHelper.newInstance(raw);
            return (Collection) o;
        } else {
            return new ArrayList();
        }
    }

    @Override
    public boolean accept(Type type) {
        Class raw = this.getRawType(type, null);
        if (raw == null) {
            return false;
        } else {
            return Collection.class.isAssignableFrom(raw);
        }
    }
}
