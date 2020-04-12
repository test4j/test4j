package org.test4j.tools.commons;

import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.tools.reflector.PropertyAccessor;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListHelper {
    /**
     * 将对象转换为列表
     *
     * @param objects
     * @return
     */
    public static <T> List toList(T... objects) {
        List list = new ArrayList();
        if (objects == null || objects.length == 0) {
            return list;
        }
        if (objects.length == 1) {
            list = toList(objects[0], false);
            return list;
        } else {
            for (Object o : objects) {
                list.add(o);
            }
            return list;
        }
    }

    /**
     * 把对象转换为列表
     *
     * @param object
     * @param withMap 如果对象是map，是否把map的值转换为列表
     * @return
     */
    public static List toList(Object object, boolean withMap) {
        List list = new ArrayList();
        if (object == null) {
            list.add(null);
            return list;
        }
        if (object instanceof Collection) {
            list.addAll((Collection) object);
            return list;
        } else if (object.getClass().isArray()) {
            Object[] array = ArrayHelper.asArray(object);
            for (Object item : array) {
                list.add(item);
            }
            return list;
        } else if (withMap && object instanceof Map) {
            Collection values = ((Map) object).values();
            list.addAll(values);
            return list;
        } else {
            list.add(object);
            return list;
        }
    }

    // boolean
    // byte
    // char
    // short int long
    // float double
    public static List toList(char values[]) {
        List objs = new ArrayList();
        for (Character value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(float values[]) {
        List objs = new ArrayList();
        for (Float value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(long values[]) {
        List objs = new ArrayList();
        for (Long value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(short values[]) {
        List objs = new ArrayList();
        for (Short value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(int values[]) {
        List objs = new ArrayList();
        for (Integer value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(double values[]) {
        List objs = new ArrayList();
        for (Double value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(boolean values[]) {
        List objs = new ArrayList();
        for (Boolean value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static List toList(byte values[]) {
        List objs = new ArrayList();
        for (Byte value : values) {
            objs.add(value);
        }
        return objs;
    }

    public static boolean isCollection(Object o) {
        if (o == null) {
            return false;
        }
        return o instanceof Collection<?>;
    }

    public static String toString(List list) {
        if (list == null) {
            return "null";
        }
        StringBuilder buff = new StringBuilder("[");
        boolean first = true;
        for (Object o : list) {
            if (first) {
                first = false;
            } else {
                buff.append(", ");
            }
            buff.append(String.valueOf(o));
        }
        buff.append("]");
        return buff.toString();
    }

    /**
     * 对list对象逐一获取指定的properties属性值
     *
     * @param list       对象列表
     * @param keys       属性值列表
     * @param isExpected 属性不存在时，是否抛异常
     * @return 对象属性值列表
     */
    public static List<Map<String, ?>> getProperties(List list, Set<String> keys, boolean isExpected) {
        List<Map<String, ?>> result = new ArrayList<>();
        for (Object o : list) {
            Map<String, Object> map = new HashMap<>();
            for (String key : keys) {
                try {
                    Object value = PropertyAccessor.getPropertyByOgnl(o, key, true);
                    map.put(key, value);
                } catch (NoSuchFieldRuntimeException e) {
                    if (isExpected) {
                        throw e;
                    } else {
                        map.put(key, null);
                    }
                }
            }
            result.add(map);
        }
        return result;
    }
}
