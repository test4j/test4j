/*
 * Copyright 2013 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package org.test4j.tools.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * 泛型对应的具体类型查找类
 * 
 * @author darui.wudr 2013-10-30 下午2:04:16
 */
@SuppressWarnings("rawtypes")
public class GenericTypeFinder {
    public static GenericTypeMap findGenericTypes(Type type) {
        GenericTypeMap map = new GenericTypeMap();
        while (!Object.class.equals(type)) {
            Class raw = getRawType(type);
            Type[] args = getTypeArgs(type);
            TypeVariable[] vars = raw.getTypeParameters();
            int index = 0;
            for (Type arg : args) {
                if (arg instanceof TypeVariable) {
                    map.putGeneric(vars[index], (TypeVariable) arg);
                } else {
                    map.putType(vars[index], arg);
                }
                index++;
            }
            type = raw.getGenericSuperclass();
        }
        return map;
    }

    private static Type[] getTypeArgs(Type type) {
        if (type instanceof Class) {
            return new Type[0];
        }
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        if (type instanceof GenericArrayType) {

        }
        //  if (type instanceof WildcardType) {
        //
        // }
        return new Type[0];
    }

    private static Class getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof GenericArrayType) {

        }
        //  if (type instanceof WildcardType) {
        //
        // }
        return Object.class;
    }
}
