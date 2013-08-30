package org.jtester.json.decoder.base;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.jtester.json.JSONException;
import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.json.helper.JSONSingle;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class MapPoJoBaseDecoder<E> extends BaseDecoder {

    @Override
    public final <T> T decode(JSONObject json, Type toType, Map<String, Object> references) {
        if (json == null) {
            return null;
        }

        if (json instanceof JSONSingle) {
            Object value = ((JSONSingle) json).toPrimitiveValue();
            return (T) value;
        }
        if (!(json instanceof JSONMap)) {
            throw new JSONException("illegal type for JavaBeanDecoder. the json\n" + json.toString()
                    + "\n isn't a JSONMap");
        }
        JSONMap map = (JSONMap) json;
        String referenceID = map.getReferFromJSONProp();
        if (referenceID != null) {
            Object o = references.get(referenceID);
            return (T) o;
        } else {
            String newID = map.getReferenceID();
            E target = this.getTarget(map, toType);
            if (newID != null) {
                references.put(newID, target);
            }
            this.parseFromJSONMap(target, map, references);
            return (T) target;
        }
    }

    protected abstract E getTarget(final JSONMap map, Type toType);

    /**
     * 根据JSONMap在#class属性或者 Decoder传进来的class创建对象实例<br>
     * 如果实例被标记了ReferenceID，往references中追加一条记录
     * 
     * @param map
     * @param references
     * @return
     */
    protected Class getTargetType(Type toType, final JSONMap map) {
        Type type = map.getClazzFromJSONFProp(toType);
        Class realClaz = this.getRawType(type, HashMap.class);
        return realClaz;
    }

    /**
     * 解析jsonMap属性，填充target对象的值
     * 
     * @param target
     * @param jsonMap
     * @param references
     * @return
     */
    protected abstract void parseFromJSONMap(E target, JSONMap jsonMap, Map<String, Object> references);
}
