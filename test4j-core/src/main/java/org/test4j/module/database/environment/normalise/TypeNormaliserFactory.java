package org.test4j.module.database.environment.normalise;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"rawtypes"})
public class TypeNormaliserFactory {
    private static Map<String, TypeNormaliser> normalisers = new HashMap<>();

    public static TypeNormaliser getNormaliser(String targetClass) {
        return normalisers.get(targetClass);
    }
}